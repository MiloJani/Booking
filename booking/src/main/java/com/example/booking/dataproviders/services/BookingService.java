package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.bookingDTOs.*;

import java.util.List;

public interface BookingService {

    List<ResponseBookingDTO> findAllBookings();

    ResponseBookingDTO findBookingById(Long id);

//    Page<ResponseBookingHistoryDTO> getBookingHistory(String username, RequestBookingHistoryDto requestDto);

    List<ResponseBookingHistoryDTO> getBookingHistory(String username);

    BookingResponseDTO saveBooking(RequestBookingDTO requestBookingDTO, String username);

//    ResponseBookingDTO updateBooking(RequestBookingDTO requestBookingDTO,Long id);

    void deleteBooking(Long id);
}
