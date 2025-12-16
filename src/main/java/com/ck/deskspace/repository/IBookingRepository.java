package com.ck.deskspace.repository;

import com.ck.deskspace.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {
    // FETCH: Get all bookings for a specific user
    List<Booking> findByUserId(Long userId);

    // LOGIC: The "Overlap Check" Query (Crucial for later)

    // "Select any booking for this room where the dates overlap with the requested new dates"


    // THE OVERLAP CHECK
    @Query("SELECT b FROM Booking b WHERE b.workspace.id = :workspaceId " +
            "AND b.status = 'CONFIRMED' " +
            "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    List<Booking> findConflictingBookings(
            @Param("workspaceId") Long workspaceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
