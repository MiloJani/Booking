package com.example.booking.dataproviders.dto.roomDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RequestAvailableRoomsDTO {

    @NotNull
    private Long businessId;

    @NotNull
    private Set<Long> roomIds;

    private int page;
}
