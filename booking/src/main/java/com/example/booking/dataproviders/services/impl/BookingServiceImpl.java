package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.services.BookingService;

import java.util.List;

public class BookingServiceImpl implements BookingService {
    @Override
    public List<ResponseBookingDTO> findAllBookings() {
        return List.of();
    }

    @Override
    public ResponseBookingDTO findBookingById(Long id) {
        return null;
    }

    @Override
    public ResponseBookingDTO saveBooking(RequestBookingDTO requestBookingDTO) {
        return null;
    }

    @Override
    public ResponseBookingDTO updateBooking(RequestBookingDTO requestBookingDTO, Long id) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {

    }
}
