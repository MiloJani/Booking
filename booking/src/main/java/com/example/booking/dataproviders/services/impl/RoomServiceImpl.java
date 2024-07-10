package com.example.booking.dataproviders.services.impl;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.*;
import com.example.booking.dataproviders.dto.roomDTOs.RequestAvailableRoomsDTO;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.RoomPricing;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.mappers.RoomMapper;
import com.example.booking.dataproviders.repositories.BusinessRepository;
import com.example.booking.dataproviders.repositories.RoomPricingRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.RoomService;
import com.example.booking.dataproviders.services.utilities.UtilitiesService;
import com.example.booking.dataproviders.services.utilities.ValidationUtilities;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;
    private BusinessRepository businessRepository;
    private UserRepository userRepository;
    private RoomMapper roomMapper;
    private final RoomPricingServiceImpl roomPricingServiceImpl;
    private final RoomPricingRepository roomPricingRepository;
    private UtilitiesService utilitiesService;


    @Override
    public List<ResponseRoomDTO> findAllRooms() {

        List<Rooms> rooms = roomRepository.findAll();
        List<ResponseRoomDTO> responseRoomDTOS = new ArrayList<>();
        for (Rooms room : rooms) {
            responseRoomDTOS.add(roomMapper.mapToDto(room));
        }
        return responseRoomDTOS;
    }

    @Override
    public ResponseRoomDTO findRoomById(Long id) {

        Rooms room = roomRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException(Constants.ROOM_NOT_FOUND));
        return roomMapper.mapToDto(room);
    }

//    @Override
//    public Page<ResponseRoomDTO> getAllAvailableRooms(RequestAvailableRoomsDTO requestAvailableRoomsDTO,String username){
//
//
//        User user = userRepository.findUserByUsername(username)
//                .orElseThrow(() -> new RecordNotFoundException("User not found"));
//
//        if (!user.getRole().getRoleName().equals("USER")) {
//            throw new AuthenticationFailedException("User does not have sufficient privileges to add a business");
//        }
//
//        int size = 2;
//        Pageable pageable = PageRequest.of(requestAvailableRoomsDTO.getPage(), size, Sort.by("price").ascending());
//        Page<Rooms> rooms = roomRepository.findByBusinesses_BusinessIdAndRoomIdIn(requestAvailableRoomsDTO.getBusinessId(),
//                requestAvailableRoomsDTO.getRoomIds(),pageable);
//
//
//        return rooms.map(room -> roomMapper.mapToDto(room));
//    }

    @Override
    public Page<ResponseRoomDTO> getAllAvailableRooms(RequestAvailableRoomsDTO requestAvailableRoomsDTO, String username) {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!user.getRole().getRoleName().equals("USER")) {
            throw new AuthenticationFailedException(Constants.INSUFFICIENT_PRIVILEGES);
        }

        ValidationUtilities.validateDates(requestAvailableRoomsDTO.getCheckInDate()
                ,requestAvailableRoomsDTO.getCheckOutDate());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(requestAvailableRoomsDTO.getCheckInDate(), formatter);
        LocalDate checkOutDate = LocalDate.parse(requestAvailableRoomsDTO.getCheckOutDate(), formatter);

//        LocalDate checkInDate;
//        LocalDate checkOutDate;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        try {
//            checkInDate = LocalDate.parse(requestAvailableRoomsDTO.getCheckInDate(), formatter);
//            checkOutDate = LocalDate.parse(requestAvailableRoomsDTO.getCheckOutDate(), formatter);
//        } catch (Exception e) {
//            throw new NotCorrectDataException(Constants.INVALID_DATE_FORMAT);
//        }

        int size = 10;
        Sort sort = Sort.by(Sort.Direction.ASC, "price"); // Default sort by price ascending
        if ("desc".equalsIgnoreCase(requestAvailableRoomsDTO.getSortDirection())) {
            sort = Sort.by(Sort.Direction.DESC, requestAvailableRoomsDTO.getSortBy());
        } else if ("asc".equalsIgnoreCase(requestAvailableRoomsDTO.getSortDirection())) {
            sort = Sort.by(Sort.Direction.ASC, requestAvailableRoomsDTO.getSortBy());
        }

        Pageable pageable = PageRequest.of(requestAvailableRoomsDTO.getPage(), size, sort);

        // Assuming roomRepository has a method to find available rooms by business ID and capacity
        List<Long> availableRoomIds = roomRepository.findAvailableRoomIds(
                requestAvailableRoomsDTO.getBusinessId(),
                checkInDate,
                checkOutDate,
                requestAvailableRoomsDTO.getCapacity()
        );

        Page<Rooms> rooms = roomRepository.findByBusinesses_BusinessIdAndRoomIdIn(
                requestAvailableRoomsDTO.getBusinessId(),
                Set.copyOf(availableRoomIds),
                pageable
        );

//        return rooms.map(room -> roomMapper.mapToDto(room));

        return rooms.map(room -> {
            ResponseRoomDTO responseRoomDTO = roomMapper.mapToDto(room);
            double totalPriceForNights = utilitiesService.calculateTotalPrice(room, checkInDate, checkOutDate);
            long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            responseRoomDTO.setTotalPriceForNights(totalPriceForNights);
            responseRoomDTO.setNumberOfNights(numberOfNights);
            return responseRoomDTO;
        });
    }

//    public double calculateTotalPrice(Rooms room, LocalDate checkInDate, LocalDate checkOutDate) {
//        double totalPrice = 0.0;
//
//        LocalDate currentDate = checkInDate;
//        while (!currentDate.isAfter(checkOutDate)) {
//            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
//            Optional<RoomPricing> optionalRoomPricing = roomPricingRepository.findByRoomAndDayOfWeek(room, dayOfWeek);
//            if (optionalRoomPricing.isPresent()) {
//                totalPrice += optionalRoomPricing.get().getPrice();
//            } else {
//                throw new RecordNotFoundException(Constants.ROOM_PRICING_NOT_FOUND + dayOfWeek);
//            }
//            currentDate = currentDate.plusDays(1);
//        }
//
//        return totalPrice;
//    }






    @Override
    @Transactional
    public /*ResponseRoomDTO*/ String createRoom(RequestRoomDTO roomDTO,String username) {

        if (Integer.parseInt(roomDTO.getCapacity())<0 || Double.parseDouble(roomDTO.getPrice())<0 ){
            throw new NotCorrectDataException(Constants.INVALID_DATA);
        }

        Rooms rooms = roomMapper.mapToEntity(roomDTO);

        Businesses businesses = businessRepository.findByBusinessName(roomDTO.getBusinessName())
                .orElseThrow(() -> new RecordNotFoundException(Constants.BUSINESS_NOT_FOUND));

        if (roomRepository.findByRoomNameAndBusinesses(roomDTO.getRoomName(), businesses).isPresent()) {
            throw new RecordAlreadyExistsException(Constants.ROOM_ALREADY_EXISTS);
        }

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException(Constants.USER_NOT_FOUND));

        if (!user.getRole().getRoleName().equals("ADMIN")) {
            throw new AuthenticationFailedException(Constants.INSUFFICIENT_PRIVILEGES);
        }

        String uploadDir = "C:\\Users\\USER\\Desktop\\SavedPhotos\\Rooms\\";
        String fileName = ValidationUtilities.transferImage(roomDTO.getImage(),uploadDir);
        rooms.setImage(fileName);

//        MultipartFile image = roomDTO.getImage();
//        if (image != null && !image.isEmpty()) {
//            if (image.getSize() > 100 * 1024) {
//                throw new FileCouldNotBeSavedException(Constants.FILE_TOO_LARGE);
//            }
//            try {
//
//                String uploadDir = "C:\\Users\\USER\\Desktop\\SavedPhotos\\Rooms\\";
//
//                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//
//                File file = new File(uploadDir + fileName);
//
//                image.transferTo(file);
//
//                rooms.setImage(fileName);
//            } catch (IOException e) {
//                throw new FileCouldNotBeSavedException(Constants.FILE_SAVE_FAILED);
//            }
//        }

        rooms.setBusinesses(businesses);

        Rooms savedRoom = roomRepository.save(rooms);
        roomPricingServiceImpl.createDefaultRoomPricings(savedRoom);
//        return roomMapper.mapToDto(savedRoom);

        return "Room saved successfully";
    }

    @Override
    public ResponseRoomDTO updateRoom(RequestRoomDTO requestRoomDTO, Long id) {

        Rooms foundRoom = roomRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException(Constants.ROOM_NOT_FOUND));

        if (Integer.parseInt(requestRoomDTO.getCapacity())<0 || Double.parseDouble(requestRoomDTO.getPrice())<0 ){
            throw new NotCorrectDataException(Constants.INVALID_DATA);
        }

        foundRoom.setRoomName(requestRoomDTO.getRoomName());
        foundRoom.setCapacity(Integer.parseInt(requestRoomDTO.getCapacity()));
        foundRoom.setPrice(Double.parseDouble(requestRoomDTO.getPrice()));
        foundRoom.setDescription(requestRoomDTO.getDescription());
//        foundRoom.setImage(requestRoomDTO.getImage());
        foundRoom.setRoomType(requestRoomDTO.getRoomType());

        Rooms updatedRoom = roomRepository.save(foundRoom);
        return roomMapper.mapToDto(updatedRoom);
    }

    @Override
    public void deleteRoom(Long id) {

        Rooms room = roomRepository.findById(id).orElseThrow(
                () -> new RuntimeException(Constants.ROOM_NOT_FOUND));

        roomRepository.delete(room);

    }
}
