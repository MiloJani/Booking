package com.example.booking.dataproviders.dto.roomPricingDTOs;

import com.example.booking.dataproviders.entities.Rooms;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

@Getter
@Setter
public class RequestRoomPricingDTO {

    private DayOfWeek dayOfWeek;

    private Double price;

    private Long roomId;
}
