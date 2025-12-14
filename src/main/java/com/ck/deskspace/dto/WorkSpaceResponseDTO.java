package com.ck.deskspace.dto;

import com.ck.deskspace.models.enums.WorkSpaceType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class WorkSpaceResponseDTO {
    private Long id;

    private String name;

    private WorkSpaceType type;

    private Integer capacity;

    private BigDecimal pricePerHour;

    private Set<AmenityDTO> amenities; // Full objects, not just IDs
}
