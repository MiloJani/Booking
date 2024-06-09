package com.example.booking.dataproviders.mappers;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.Rooms;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Component
public class BookingMapper {

        public ResponseBookingDTO mapToDto(Booking booking){

            ResponseBookingDTO responseBookingDTO = new ResponseBookingDTO();
            responseBookingDTO.setBookingId(booking.getBookingId());
            responseBookingDTO.setBookingDate(booking.getBookingDate());
            responseBookingDTO.setCheckInDate(booking.getCheckInDate());
            responseBookingDTO.setCheckOutDate(booking.getCheckOutDate());
            responseBookingDTO.setNoOfAdults(booking.getNoOfAdults());
            responseBookingDTO.setNoOfChildren(booking.getNoOfChildren());
            responseBookingDTO.setStatus(booking.getStatus());
            responseBookingDTO.setUserId(booking.getUser().getUserId());
            responseBookingDTO.setRoomId(booking.getRoom().getRoomId());
            responseBookingDTO.setPaymentId(booking.getPayment().getPaymentId());
            return responseBookingDTO;
        }

        public Booking mapToEntity(RequestBookingDTO requestBookingDTO){

            Booking booking = new Booking();
            booking.setBookingDate(requestBookingDTO.getBookingDate());
            booking.setCheckInDate(requestBookingDTO.getCheckInDate());
            booking.setCheckOutDate(requestBookingDTO.getCheckOutDate());
            booking.setNoOfAdults(requestBookingDTO.getNoOfAdults());
            booking.setNoOfChildren(requestBookingDTO.getNoOfChildren());

            return booking;
        }
}
