package com.example.booking.dataproviders.dto.bookingDTOs;

import com.example.booking.dataproviders.dto.paymentDTOs.RequestPaymentDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestBookingDTO {


    private LocalDate bookingDate;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer noOfAdults;

    private Integer noOfChildren;

    private Long userId;

    private Long roomId;

    private RequestPaymentDTO requestPaymentDTO;
}
