package com.example.booking.dataproviders.dto.searchDTOs;

import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessSearchDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSearchDTO {

    private ResponseBusinessSearchDTO responseBusinessSearchDTO;
    private int freeRooms;
}
