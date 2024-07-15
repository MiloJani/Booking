package com.example.booking.dataproviders.services.impl;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.AuthenticationFailedException;
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

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final PaymentMapper paymentMapper;
    private final UserInfoRepository userInfoRepository;
    private final RoomRepository roomRepository;
    private final UtilitiesService utilitiesService;

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

    //returns the booking history of user as list
    @Override
    @Transactional
    public List<ResponseBookingHistoryDTO> getBookingHistory(String username) throws RecordNotFoundException, AuthenticationFailedException {

        User user = utilitiesService.validateUser(username,Constants.USER);
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
    public BookingResponseDTO saveBooking(RequestBookingDTO requestBookingDTO,String username)
            throws RecordNotFoundException,RecordAlreadyExistsException,AuthenticationFailedException,NotCorrectDataException {

        User user = utilitiesService.validateUser(username,Constants.USER);

        Booking booking = bookingMapper.mapToEntity(requestBookingDTO);

        boolean isForAnother = Boolean.parseBoolean(requestBookingDTO.getIsForAnother());

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                requestBookingDTO.getRoomId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        //checking if booking exists(technically we don't need this because when we search we won't get the rooms that are booked but better be safe than sorry)
        if (!overlappingBookings.isEmpty()) {
            throw new RecordAlreadyExistsException(Constants.BOOKING_ALREADY_EXISTS);
        }

        //set status booked,checked in or checked out
        utilitiesService.setStatus(booking);
        
        booking.setUser(user);

        //if points >=10 return discountPoints*2 else 0 also remove the points used
        int discountPoints = user.getUserInfo().getDiscountPoints();
        double discount = discountPoints >= Constants.DISCOUNT_THRESHOLD ? discountPoints * Constants.DISCOUNT_MULTIPLIER : 0;
        if (discountPoints>=Constants.DISCOUNT_THRESHOLD) user.getUserInfo().setDiscountPoints(0);

        //after booking add 3 points to user
        int points = user.getUserInfo().getDiscountPoints()+3;
        user.getUserInfo().setDiscountPoints(points);
        userInfoRepository.save(user.getUserInfo());

        Rooms room = roomRepository.findById(requestBookingDTO.getRoomId()).orElseThrow(
                () -> new RecordNotFoundException(Constants.ROOM_NOT_FOUND));
        booking.setRoom(room);

        //add payment and calculate totalPrice
        Payment payment = paymentMapper.mapToEntity(requestBookingDTO);
        double totalPrice = utilitiesService.calculateTotalPrice(room, booking.getCheckInDate(), booking.getCheckOutDate());
        payment.setTotalPrice(totalPrice+totalPrice*room.getBusinesses().getTax()-discount);//totalPrice+totalPrice*tax-discount


        if (!isForAnother && !booking.getFullName().equals(payment.getCardHolderName())) {
            throw new NotCorrectDataException(Constants.DIFFERENT_USER_CARD_NAME);
        }

        payment.setBooking(booking);
        booking.setPayment(payment);

        Booking bookingSaved = bookingRepository.save(booking);

        //response
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setPoints(user.getUserInfo().getDiscountPoints());
        bookingResponseDTO.setBooks(bookingRepository.countByUser(user));

        return bookingResponseDTO;
    }

//    @Override
//    public ResponseBookingDTO updateBooking(RequestBookingDTO requestBookingDTO, Long id) {
//        return null;
//    }

    @Override
    public void deleteBooking(Long id) {

        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException(Constants.BOOKING_NOT_FOUND));
        bookingRepository.delete(booking);

    }
}
