package com.example.booking.dataproviders.dto.searchDTOs;

import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSearchDTO {

    private ResponseBusinessDTO responseBusinessDTO;
    private int freeRooms;
}
