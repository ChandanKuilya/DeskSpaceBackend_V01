package com.ck.deskspace.services;

import com.ck.deskspace.dto.bookingdtos.BookingRequestDTO;
import com.ck.deskspace.dto.bookingdtos.BookingResponseDTO;
import com.ck.deskspace.models.Booking;
import com.ck.deskspace.models.User;
import com.ck.deskspace.models.WorkSpace;
import com.ck.deskspace.models.enums.Status;
import com.ck.deskspace.repository.IBookingRepository;
import com.ck.deskspace.repository.IUserRepository;
import com.ck.deskspace.repository.IWorkSpaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private final IBookingRepository bookingRepository;
    private final IWorkSpaceRepository workspaceRepository;
    private final IUserRepository userRepository;

    @Override
    @Transactional // Ensures atomicity (all or nothing)
    public BookingResponseDTO createBooking(BookingRequestDTO request, String userEmail) {

        // 1. Get User and Workspace
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkSpace workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        // 2. Validate Dates (Start must be before End)
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("Start time must be before End time");
        }

        // 3. CHECK FOR OVERLAPS (The critical check)
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                request.getWorkspaceId(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Workspace is already booked for this time slot!");
        }

        // 4. Save Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setWorkspace(workspace);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus(Status.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);

        return mapToDTO(savedBooking);
    }

    @Override
    public List<BookingResponseDTO> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private BookingResponseDTO mapToDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setWorkspaceName(booking.getWorkspace().getName());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setStatus(booking.getStatus());
        dto.setUserName(booking.getUser().getFirstName());
        dto.setUserName(booking.getUser().getLastName());

        return dto;
    }
}