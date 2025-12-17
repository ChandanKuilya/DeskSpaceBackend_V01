package com.ck.deskspace.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AmenityDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;
}
