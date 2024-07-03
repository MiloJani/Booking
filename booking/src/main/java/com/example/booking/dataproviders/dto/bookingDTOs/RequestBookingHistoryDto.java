package com.example.booking.dataproviders.dto.bookingDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestBookingHistoryDto {
    private int page;
    private int size;
}

