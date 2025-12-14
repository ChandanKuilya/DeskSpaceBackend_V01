package com.ck.deskspace.repository;

import com.ck.deskspace.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {
    // FETCH: Get all bookings for a specific user
    List<Booking> findByUserId(Long userId);

    // LOGIC: The "Overlap Check" Query (Crucial for later)

    // "Select any booking for this room where the dates overlap with the requested new dates"

    // We will implement this specific @Query in Sprint 4.
    
    // Just creating the interface for now.
}
