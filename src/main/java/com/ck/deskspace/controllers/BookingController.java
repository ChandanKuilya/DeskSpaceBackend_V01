package com.ck.deskspace.controllers;

import com.ck.deskspace.dto.bookingdtos.BookingRequestDTO;
import com.ck.deskspace.dto.bookingdtos.BookingResponseDTO;
import com.ck.deskspace.services.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final IBookingService bookingService;

    // POST: Create a new booking
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestBody BookingRequestDTO request,
            Authentication authentication // <--- Spring injects the logged-in user here
    ) {
        String userEmail = authentication.getName(); // Extract email from Token
        return new ResponseEntity<>(bookingService.createBooking(request, userEmail), HttpStatus.CREATED);
    }

    // GET: Get my history
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(bookingService.getUserBookings(userEmail));
    }
}