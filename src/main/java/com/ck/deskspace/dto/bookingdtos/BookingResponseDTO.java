package com.ck.deskspace.dto.bookingdtos;

import com.ck.deskspace.models.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {
    private Long id;
    private String workspaceName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Status status;
    private String userName; // Good for Admin views
}
