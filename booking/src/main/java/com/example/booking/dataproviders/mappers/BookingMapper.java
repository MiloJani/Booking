package com.example.booking.dataproviders.mappers;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.NotCorrectDataException;
import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingHistoryDTO;
import com.example.booking.dataproviders.entities.Booking;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

            LocalDate checkInDate;
            LocalDate checkOutDate;
            LocalDate bookingDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                checkInDate = LocalDate.parse(requestBookingDTO.getCheckInDate(), formatter);
                checkOutDate = LocalDate.parse(requestBookingDTO.getCheckOutDate(), formatter);
                bookingDate = LocalDate.parse(requestBookingDTO.getBookingDate(), formatter);
            } catch (Exception e) {
                throw new NotCorrectDataException(Constants.INVALID_DATE_FORMAT);
            }

            Booking booking = new Booking();
            booking.setBookingDate(bookingDate);
            booking.setCheckInDate(checkInDate);
            booking.setCheckOutDate(checkOutDate);
            booking.setNoOfAdults(requestBookingDTO.getNoOfAdults());
            booking.setNoOfChildren(requestBookingDTO.getNoOfChildren());
            booking.setFullName(requestBookingDTO.getFullName());
            booking.setEmail(requestBookingDTO.getEmail());
            booking.setAddress(requestBookingDTO.getAddress());
            booking.setPhoneNumber(requestBookingDTO.getPhoneNumber());

            return booking;
        }

    public ResponseBookingHistoryDTO mapToHistoryDto(Booking booking) {
        ResponseBookingHistoryDTO dto = new ResponseBookingHistoryDTO();
        dto.setHotelName(booking.getRoom().getBusinesses().getBusinessName());
        dto.setRoomName(booking.getRoom().getRoomName());
        dto.setTotalPrice(booking.getPayment().getTotalPrice());
        dto.setBookingDate(booking.getBookingDate());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
