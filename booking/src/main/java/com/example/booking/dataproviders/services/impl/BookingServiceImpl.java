package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.entities.Payment;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.mappers.BookingMapper;
import com.example.booking.dataproviders.mappers.PaymentMapper;
import com.example.booking.dataproviders.mappers.RoomMapper;
import com.example.booking.dataproviders.mappers.UserMapper;
import com.example.booking.dataproviders.repositories.BookingRepository;
import com.example.booking.dataproviders.repositories.PaymentRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;
    private PaymentRepository paymentRepository;
    private PaymentMapper paymentMapper;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private RoomRepository roomRepository;
    private RoomMapper roomMapper;

    @Override
    public List<ResponseBookingDTO> findAllBookings() {

        List<Booking> bookings = bookingRepository.findAll();
        List<ResponseBookingDTO> bookingDTOS = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDTOS.add(bookingMapper.mapToDto(booking));
        }
        return bookingDTOS;
    }

    @Override
    public ResponseBookingDTO findBookingById(Long id) {

        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));

        return bookingMapper.mapToDto(booking);
    }

    @Override
    public ResponseBookingDTO saveBooking(RequestBookingDTO requestBookingDTO) {

        Booking booking = bookingMapper.mapToEntity(requestBookingDTO);
        if (booking.getBookingDate().isEqual(booking.getCheckInDate()) ||
                (booking.getBookingDate().isAfter(booking.getCheckInDate()) &&
                        booking.getBookingDate().isBefore(booking.getCheckOutDate()))){
            booking.setStatus("Checked In");
        }else if (booking.getBookingDate().isBefore(booking.getCheckInDate())){
            booking.setStatus("Booked");
        }else {
            booking.setStatus("Checked Out");
        }

        User user = userRepository.findById(requestBookingDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        booking.setUser(user);

        Rooms room = roomRepository.findById(requestBookingDTO.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        booking.setRoom(room);

        Payment payment = paymentMapper.mapToEntity(requestBookingDTO.getRequestPaymentDTO());
        payment.setBooking(booking);
        booking.setPayment(payment);

        Booking bookingSaved = bookingRepository.save(booking);
        return bookingMapper.mapToDto(bookingSaved);
    }

    @Override
    public ResponseBookingDTO updateBooking(RequestBookingDTO requestBookingDTO, Long id) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {

        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(booking);

    }
}
