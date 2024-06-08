package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;

import java.util.List;

public interface BookingService {

    List<ResponseBookingDTO> findAllBookings();

    ResponseBookingDTO findBookingById(Long id);

    ResponseBookingDTO saveBooking(RequestBookingDTO requestBookingDTO);

    ResponseBookingDTO updateBooking(RequestBookingDTO requestBookingDTO,Long id);

    void deleteBooking(Long id);
}
