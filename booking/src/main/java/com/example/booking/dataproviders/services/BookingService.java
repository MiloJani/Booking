package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {

    List<ResponseBookingDTO> findAllBookings();

    ResponseBookingDTO findBookingById(Long id);

    List<Booking> getBookingHistory(String username);

    ResponseBookingDTO saveBooking(RequestBookingDTO requestBookingDTO,String username);

    ResponseBookingDTO updateBooking(RequestBookingDTO requestBookingDTO,Long id);

    void deleteBooking(Long id);
}
