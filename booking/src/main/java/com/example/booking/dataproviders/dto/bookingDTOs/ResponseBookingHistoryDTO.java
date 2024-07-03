package com.example.booking.dataproviders.dto.bookingDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBookingHistoryDTO {

    private String hotelName;
    private String roomName;
    private Double totalPrice;
    private LocalDate bookingDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
}
