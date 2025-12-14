package com.ck.deskspace.dto;

import com.ck.deskspace.models.enums.WorkSpaceType;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

@Data
public class WorkSpaceRequestDTO { // What the admin sends to create a room/workspace
    private String name;

    private WorkSpaceType type;

    private Integer capacity;

    private BigDecimal pricePerHour;

    private Set<Long> amenityIds;  // Admin just says ids [1,2,5]...
}
