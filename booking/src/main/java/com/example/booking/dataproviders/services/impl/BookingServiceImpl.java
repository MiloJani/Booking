package com.example.booking.dataproviders.services.impl;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.NotCorrectDataException;
import com.example.booking.core.exceptions.RecordAlreadyExistsException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.bookingDTOs.*;
import com.example.booking.dataproviders.entities.*;
import com.example.booking.dataproviders.mappers.*;
import com.example.booking.dataproviders.repositories.*;
import com.example.booking.dataproviders.services.BookingService;
import com.example.booking.dataproviders.services.utilities.UtilitiesService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;
    private PaymentRepository paymentRepository;
    private PaymentMapper paymentMapper;
    private UserRepository userRepository;
    private UserInfoRepository userInfoRepository;
    private RoomRepository roomRepository;
    private UtilitiesService utilitiesService;

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

        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException(Constants.BOOKING_NOT_FOUND));

        return bookingMapper.mapToDto(booking);
    }

    @Override
    @Transactional
    public List<ResponseBookingHistoryDTO> getBookingHistory(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new RecordNotFoundException(Constants.USER_NOT_FOUND));
        List<Booking> bookings = bookingRepository.findByUser(user);

        return bookings.stream()
                .map(bookingMapper::mapToHistoryDto)
                .collect(Collectors.toList());
    }

//    @Override
//    @Transactional
//    public Page<ResponseBookingHistoryDTO> getBookingHistory(String username, RequestBookingHistoryDto requestDto) {
//        User user = userRepository.findUserByUsername(username).orElseThrow(
//                () -> new RecordNotFoundException(Constants.USER_NOT_FOUND));
//        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
//        Page<Booking> bookings = bookingRepository.findByUser(user, pageable);
//
//        return bookings.map(bookingMapper::mapToHistoryDto);
//    }


    @Override
    @Transactional
    public /*ResponseBookingDTO*/BookingResponseDTO saveBooking(RequestBookingDTO requestBookingDTO,String username) {



//        User user = userRepository.findUserByUsername(username)
//                .orElseThrow(() -> new RecordNotFoundException(Constants.USER_NOT_FOUND));
//
//        if (!user.getRole().getRoleName().equals("USER")) {
//            throw new AuthenticationFailedException(Constants.INSUFFICIENT_PRIVILEGES);
//        }

        User user = utilitiesService.validateUser(username,"USER");

        Booking booking = bookingMapper.mapToEntity(requestBookingDTO);

        boolean isForAnother = Boolean.parseBoolean(requestBookingDTO.getIsForAnother());

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                requestBookingDTO.getRoomId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (!overlappingBookings.isEmpty()) {
            throw new RecordAlreadyExistsException(Constants.BOOKING_ALREADY_EXISTS);
        }

//        if (booking.getBookingDate().isEqual(booking.getCheckInDate()) ||
//                booking.getBookingDate().isEqual(booking.getCheckOutDate()) ||
//                (booking.getBookingDate().isAfter(booking.getCheckInDate()) &&
//                        booking.getBookingDate().isBefore(booking.getCheckOutDate()))){
//            booking.setStatus("Checked In");
//        }else if (booking.getBookingDate().isBefore(booking.getCheckInDate())){
//            booking.setStatus("Booked");
//        }else {
//            booking.setStatus("Checked Out");
//        }

        utilitiesService.setStatus(booking);
        
        booking.setUser(user);

        int points = user.getUserInfo().getDiscountPoints()+3;
        user.getUserInfo().setDiscountPoints(points);
        userInfoRepository.save(user.getUserInfo());

        Rooms room = roomRepository.findById(requestBookingDTO.getRoomId()).orElseThrow(
                () -> new RecordNotFoundException(Constants.ROOM_NOT_FOUND));
        booking.setRoom(room);

        Payment payment = paymentMapper.mapToEntity(requestBookingDTO);
        double totalPrice = utilitiesService.calculateTotalPrice(room, booking.getCheckInDate(), booking.getCheckOutDate());
        payment.setTotalPrice(totalPrice);


        if (!isForAnother && !booking.getFullName().equals(payment.getCardHolderName())) {
            throw new NotCorrectDataException(Constants.DIFFERENT_USER_CARD_NAME);
        }

        payment.setBooking(booking);
        booking.setPayment(payment);

        Booking bookingSaved = bookingRepository.save(booking);

        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setPoints(user.getUserInfo().getDiscountPoints());
        bookingResponseDTO.setBooks(bookingRepository.countByUser(user));

//        return bookingMapper.mapToDto(bookingSaved);

        return bookingResponseDTO;
    }

    @Override
    public ResponseBookingDTO updateBooking(RequestBookingDTO requestBookingDTO, Long id) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {

        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException(Constants.BOOKING_NOT_FOUND));
        bookingRepository.delete(booking);

    }
}
