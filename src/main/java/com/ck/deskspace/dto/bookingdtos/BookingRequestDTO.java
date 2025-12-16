package com.ck.deskspace.dto.bookingdtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDTO {
    private Long workspaceId;
    private LocalDateTime startTime; // Format: "2023-12-16T10:00:00"
    private LocalDateTime endTime;
}