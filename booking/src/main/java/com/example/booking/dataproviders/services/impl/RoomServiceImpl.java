package com.example.booking.dataproviders.services.impl;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.*;
import com.example.booking.dataproviders.dto.roomDTOs.RequestAvailableRoomsDTO;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.mappers.RoomMapper;
import com.example.booking.dataproviders.repositories.BusinessRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.services.RoomService;
import com.example.booking.dataproviders.services.utilities.UtilitiesService;
import com.example.booking.dataproviders.services.utilities.ValidationUtilities;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BusinessRepository businessRepository;
    private final RoomMapper roomMapper;
    private final RoomPricingServiceImpl roomPricingServiceImpl;
    private final UtilitiesService utilitiesService;


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

    @Override
    public Page<ResponseRoomDTO> getAllAvailableRooms(RequestAvailableRoomsDTO requestAvailableRoomsDTO, String username)
    throws RecordNotFoundException{

        //validate dates and conversion
        ValidationUtilities.validateDates(requestAvailableRoomsDTO.getCheckInDate()
                ,requestAvailableRoomsDTO.getCheckOutDate());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(requestAvailableRoomsDTO.getCheckInDate(), formatter);
        LocalDate checkOutDate = LocalDate.parse(requestAvailableRoomsDTO.getCheckOutDate(), formatter);

        int size = 10;

        Pageable pageable = PageRequest.of(requestAvailableRoomsDTO.getPage(), size);

        //get all available rooms ids
        List<Long> availableRoomIds = roomRepository.findAvailableRoomIds(
                requestAvailableRoomsDTO.getBusinessId(),
                checkInDate,
                checkOutDate,
                requestAvailableRoomsDTO.getCapacity()
        );

        // Fetch all rooms from their id and the businessId
        List<Rooms> rooms = roomRepository.findByBusinesses_BusinessIdAndRoomIdIn(
                requestAvailableRoomsDTO.getBusinessId(),
                Set.copyOf(availableRoomIds)
        );

        // Calculate total price for each room
        Map<Rooms, Double> roomTotalPriceMap = rooms.stream()
                .collect(Collectors.toMap(
                        room -> room,
                        room -> utilitiesService.calculateTotalPrice(room, checkInDate, checkOutDate)
                ));

        // Sort rooms based on total price for nights
        if ("desc".equalsIgnoreCase(requestAvailableRoomsDTO.getSortDirection())) {
            rooms.sort(Comparator.comparingDouble(roomTotalPriceMap::get).reversed());
        } else {
            rooms.sort(Comparator.comparingDouble(roomTotalPriceMap::get));
        }

        // (Creating a sub list)Paginate the sorted list starting from the first element of the current page to the last element of the page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), rooms.size());
        List<Rooms> paginatedRooms = rooms.subList(start, end);

        // Map paginated rooms to DTOs and set total price for nights/ number of nights
        List<ResponseRoomDTO> responseRoomDTOS = paginatedRooms.stream()
                .map(room -> {
                    ResponseRoomDTO responseRoomDTO = roomMapper.mapToDto(room);
                    responseRoomDTO.setTotalPriceForNights(roomTotalPriceMap.get(room));
                    responseRoomDTO.setNumberOfNights(ChronoUnit.DAYS.between(checkInDate, checkOutDate));
                    return responseRoomDTO;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(responseRoomDTOS, pageable, rooms.size());

    }



    @Override
    @Transactional
    public  String createRoom(RequestRoomDTO roomDTO,String username)
    throws NotCorrectDataException,RecordNotFoundException,RecordAlreadyExistsException{

        //checking capacity and price
        if (Integer.parseInt(roomDTO.getCapacity())<0 || Double.parseDouble(roomDTO.getPrice())<0 ){
            throw new NotCorrectDataException(Constants.INVALID_DATA);
        }

        Rooms rooms = roomMapper.mapToEntity(roomDTO);

        //find the business where we are trying to add the room
        Businesses businesses = businessRepository.findByBusinessName(roomDTO.getBusinessName())
                .orElseThrow(() -> new RecordNotFoundException(Constants.BUSINESS_NOT_FOUND));

        //check if there is a room with the same name for that business
        if (roomRepository.findByRoomNameAndBusinesses(roomDTO.getRoomName(), businesses).isPresent()) {
            throw new RecordAlreadyExistsException(Constants.ROOM_ALREADY_EXISTS);
        }

        //change based on your directory
        String uploadDir = "C:\\Users\\USER\\Desktop\\SavedPhotos\\Rooms\\";//Constants.ROOM_UPLOAD_DIR
        String fileName = ValidationUtilities.transferImage(roomDTO.getImage(),uploadDir);
        rooms.setImage(fileName);

        rooms.setBusinesses(businesses);

        Rooms savedRoom = roomRepository.save(rooms);

        //create roomPricing for all days of week
        roomPricingServiceImpl.createDefaultRoomPricings(savedRoom);

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
