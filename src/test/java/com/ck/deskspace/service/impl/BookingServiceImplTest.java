package com.ck.deskspace.service.impl;

import com.ck.deskspace.dto.bookingdtos.BookingRequestDTO;
import com.ck.deskspace.dto.bookingdtos.BookingResponseDTO;
import com.ck.deskspace.models.Booking;
import com.ck.deskspace.models.User;
import com.ck.deskspace.models.WorkSpace;
import com.ck.deskspace.models.enums.Status;
import com.ck.deskspace.repository.IBookingRepository;
import com.ck.deskspace.repository.IUserRepository;
import com.ck.deskspace.repository.IWorkSpaceRepository;
import com.ck.deskspace.services.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Initializes Mocks
class BookingServiceImplTest {

    // 1. Create Fake Repositories
    @Mock
    private IBookingRepository bookingRepository;
    @Mock
    private IWorkSpaceRepository workspaceRepository;
    @Mock
    private IUserRepository userRepository;

    // 2. Inject them into the Service we are testing
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBooking_Success() {
        // --- ARRANGE (Prepare the data) ---
        String email = "test@user.com";
        Long workspaceId = 1L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        BookingRequestDTO request = new BookingRequestDTO();
        request.setWorkspaceId(workspaceId);
        request.setStartTime(start);
        request.setEndTime(end);

        User mockUser = new User();
        mockUser.setId(100L);
        mockUser.setEmail(email);
        mockUser.setFirstName("Test User First Name");
        mockUser.setLastName("Test User Last Name");

        WorkSpace mockWorkspace = new WorkSpace();
        mockWorkspace.setId(workspaceId);
        mockWorkspace.setName("Lab A");

        // Tell Mockito: "When someone asks for this email, return my fake user"
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(mockWorkspace));

        // Tell Mockito: "Return an EMPTY list, meaning NO CONFLICTS found"
        when(bookingRepository.findConflictingBookings(workspaceId, start, end))
                .thenReturn(Collections.emptyList());

        // Tell Mockito: "When save is called, return a booking with ID 555"
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(555L);
            return b;
        });

        // --- ACT (Run the method) ---
        BookingResponseDTO response = bookingService.createBooking(request, email);

        // --- ASSERT (Verify the results) ---
        assertNotNull(response);
        assertEquals(555L, response.getId());
        assertEquals("Lab A", response.getWorkspaceName());
        assertEquals(Status.CONFIRMED, response.getStatus());

        // Verify that save() was actually called exactly once
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_Failure_Overlap() {
        // --- ARRANGE ---
        String email = "test@user.com";
        Long workspaceId = 1L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        BookingRequestDTO request = new BookingRequestDTO();
        request.setWorkspaceId(workspaceId);
        request.setStartTime(start);
        request.setEndTime(end);

        // Mock basic requirements
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(new WorkSpace()));

        // THE CRITICAL PART: Return a list containing a conflict!
        Booking existingBooking = new Booking(); // Simulate a booking already there
        when(bookingRepository.findConflictingBookings(workspaceId, start, end))
                .thenReturn(List.of(existingBooking));

        // --- ACT & ASSERT ---
        // Expect a RuntimeException when we call the method
        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(request, email);
        });

        assertEquals("Workspace is already booked for this time slot!", exception.getMessage());

        // Verify that save() was NEVER called (The logic should block it before saving)
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
