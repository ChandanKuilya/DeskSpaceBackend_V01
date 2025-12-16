package com.ck.deskspace.services;

import com.ck.deskspace.dto.bookingdtos.BookingRequestDTO;
import com.ck.deskspace.dto.bookingdtos.BookingResponseDTO;

import java.util.List;

public interface IBookingService {
    BookingResponseDTO createBooking(BookingRequestDTO request, String userEmail);
    List<BookingResponseDTO> getUserBookings(String userEmail);
}
