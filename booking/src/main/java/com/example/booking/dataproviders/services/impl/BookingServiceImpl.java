package com.example.booking.dataproviders.services.impl;

import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.NotCorrectDataException;
import com.example.booking.core.exceptions.RecordAlreadyExistsException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingHistoryDto;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingHistoryDTO;
import com.example.booking.dataproviders.entities.*;
import com.example.booking.dataproviders.mappers.*;
import com.example.booking.dataproviders.repositories.*;
import com.example.booking.dataproviders.services.BookingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;
    private PaymentRepository paymentRepository;
    private PaymentMapper paymentMapper;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserInfoRepository userInfoRepository;
    private RoomRepository roomRepository;
    private RoomPricingRepository roomPricingRepository;
    private BusinessRepository businessRepository;
    private BusinessMapper businessMapper;
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

        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Booking not found"));

        return bookingMapper.mapToDto(booking);
    }

    @Override
    @Transactional
    public Page<ResponseBookingHistoryDTO> getBookingHistory(String username, RequestBookingHistoryDto requestDto) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new RecordNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        Page<Booking> bookings = bookingRepository.findByUser(user, pageable);

        return bookings.map(bookingMapper::mapToHistoryDto);
    }


    @Override
    @Transactional
    public ResponseBookingDTO saveBooking(RequestBookingDTO requestBookingDTO,String username) {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!user.getRole().getRoleName().equals("USER")) {
            throw new AuthenticationFailedException("User does not have sufficient privileges to view available rooms");
        }

        Booking booking = bookingMapper.mapToEntity(requestBookingDTO);

        boolean isForAnother = Boolean.parseBoolean(requestBookingDTO.getIsForAnother());

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                requestBookingDTO.getRoomId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (!overlappingBookings.isEmpty()) {
            throw new RecordAlreadyExistsException("A booking already exists for the given date range");
        }

        if (booking.getBookingDate().isEqual(booking.getCheckInDate()) ||
                booking.getBookingDate().isEqual(booking.getCheckOutDate()) ||
                (booking.getBookingDate().isAfter(booking.getCheckInDate()) &&
                        booking.getBookingDate().isBefore(booking.getCheckOutDate()))){
            booking.setStatus("Checked In");
        }else if (booking.getBookingDate().isBefore(booking.getCheckInDate())){
            booking.setStatus("Booked");
        }else {
            booking.setStatus("Checked Out");
        }
        
        booking.setUser(user);

        int points = user.getUserInfo().getDiscountPoints()+3;
        user.getUserInfo().setDiscountPoints(points);
        userInfoRepository.save(user.getUserInfo());

        Rooms room = roomRepository.findById(requestBookingDTO.getRoomId()).orElseThrow(() -> new RecordNotFoundException("Room not found"));
        booking.setRoom(room);

        Payment payment = paymentMapper.mapToEntity(requestBookingDTO);
        double totalPrice = calculateTotalPrice(room, booking.getCheckInDate(), booking.getCheckOutDate());
        payment.setTotalPrice(totalPrice);


        if (!isForAnother && !booking.getFullName().equals(payment.getCardHolderName())) {
            throw new NotCorrectDataException("Card holder name is different from user name");
        }

        payment.setBooking(booking);
        booking.setPayment(payment);

        Booking bookingSaved = bookingRepository.save(booking);
        return bookingMapper.mapToDto(bookingSaved);
    }

    public double calculateTotalPrice(Rooms room, LocalDate checkInDate, LocalDate checkOutDate) {
        double totalPrice = 0.0;

        LocalDate currentDate = checkInDate;
        while (!currentDate.isAfter(checkOutDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            Optional<RoomPricing> optionalRoomPricing = roomPricingRepository.findByRoomAndDayOfWeek(room, dayOfWeek);
            if (optionalRoomPricing.isPresent()) {
                totalPrice += optionalRoomPricing.get().getPrice();
            } else {
                throw new RecordNotFoundException("No pricing found for day: " + dayOfWeek);
            }
            currentDate = currentDate.plusDays(1);
        }

        return totalPrice;
    }

    @Override
    public ResponseBookingDTO updateBooking(RequestBookingDTO requestBookingDTO, Long id) {
        return null;
    }

    @Override
    public void deleteBooking(Long id) {

        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Booking not found"));
        bookingRepository.delete(booking);

    }
}
