package com.example.booking.dataproviders.services.impl;

import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.entities.*;
import com.example.booking.dataproviders.mappers.*;
import com.example.booking.dataproviders.repositories.*;
import com.example.booking.dataproviders.services.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
    private UserMapper userMapper;
    private UserInfoRepository userInfoRepository;
    private RoomRepository roomRepository;
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


//    //Search begin
//@Override
//public Page<ResponseSearchDTO> searchBookings(RequestSearchDTO searchRequest) {
//    // Add sorting by room price in ascending order. For descending order, use Sort.by("price").descending()
//    Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by("rooms.price").ascending());
//    Page<Businesses> businessPage = businessRepository.findAll(pageable);
//
//    List<ResponseSearchDTO> bookingDTOs = businessPage.getContent().stream()
//            .map(business -> {
//                long freeRooms = business.getRooms().stream()
//                        .filter(room -> isRoomFree(room, searchRequest))
//                        .count();
//
//                ResponseBusinessDTO responseBusinessDTO = businessMapper.mapToDto(business);
//
//                ResponseSearchDTO responseSearchDTO = new ResponseSearchDTO();
//                responseSearchDTO.setResponseBusinessDTO(responseBusinessDTO);
//                responseSearchDTO.setFreeRooms((int) freeRooms);
//
//                return responseSearchDTO;
//            })
//            .sorted(Comparator.comparing(dto -> dto.getResponseBusinessDTO().getRooms().stream()
//                    .mapToDouble(ResponseRoomDTO::getPrice)
//                    .min()
//                    .orElse(Double.MAX_VALUE)))  // Sorting by minimum room price in each business
//            .collect(Collectors.toList());
//
//    return new PageImpl<>(bookingDTOs, pageable, businessPage.getTotalElements());
//}
//
//    private boolean isRoomFree(Rooms room, RequestSearchDTO searchRequest) {
//        int totalGuests = (searchRequest.getNoOfAdults() != null ? searchRequest.getNoOfAdults() : 0) +
//                (searchRequest.getNoOfChildren() != null ? searchRequest.getNoOfChildren() : 0);
//
//        if (room.getCapacity() < totalGuests) {
//            return false;
//        }
//
//        LocalDate checkInDate = searchRequest.getCheckInDate();
//        LocalDate checkOutDate = searchRequest.getCheckOutDate();
//
//        Specification<Booking> roomSpec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
//                criteriaBuilder.equal(root.get("room"), room),
//                criteriaBuilder.or(
//                        criteriaBuilder.lessThan(root.get("checkOutDate"), checkInDate),
//                        criteriaBuilder.greaterThan(root.get("checkInDate"), checkOutDate)
//                )
//        );
//
//        boolean isFree = bookingRepository.findAll(roomSpec).isEmpty();
//        System.out.println("Room: " + room.getRoomName() + ", Is Free: " + isFree);
//
//        return isFree;
//    }
//    //Search end

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

        User user = userRepository.findById(requestBookingDTO.getUserId()).orElseThrow(() -> new RecordNotFoundException("User not found"));
        booking.setUser(user);
        int points = user.getUserInfo().getDiscountPoints()+3;
        user.getUserInfo().setDiscountPoints(points);
        userInfoRepository.save(user.getUserInfo());

        Rooms room = roomRepository.findById(requestBookingDTO.getRoomId()).orElseThrow(() -> new RecordNotFoundException("Room not found"));
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

        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Booking not found"));
        bookingRepository.delete(booking);

    }
}
