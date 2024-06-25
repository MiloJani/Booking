package com.example.booking.dataproviders.services.impl;

import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.FileCouldNotBeSavedException;
import com.example.booking.core.exceptions.RecordAlreadyExistsException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.roomDTOs.RequestAvailableRoomsDTO;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.mappers.RoomMapper;
import com.example.booking.dataproviders.repositories.BusinessRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.RoomService;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;
    private BusinessRepository businessRepository;
    private UserRepository userRepository;
    private RoomMapper roomMapper;
    private final RoomPricingServiceImpl roomPricingServiceImpl;


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

        Rooms room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
        return roomMapper.mapToDto(room);
    }

    @Override
    public Page<ResponseRoomDTO> getAllAvailableRooms(RequestAvailableRoomsDTO requestAvailableRoomsDTO,String username){


        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!user.getRole().getRoleName().equals("USER")) {
            throw new AuthenticationFailedException("User does not have sufficient privileges to add a business");
        }

        int size = 2;
        Pageable pageable = PageRequest.of(requestAvailableRoomsDTO.getPage(), size, Sort.by("price").ascending());
        Page<Rooms> rooms = roomRepository.findByBusinesses_BusinessIdAndRoomIdIn(requestAvailableRoomsDTO.getBusinessId(),
                requestAvailableRoomsDTO.getRoomIds(),pageable);


        return rooms.map(room -> roomMapper.mapToDto(room));
    }

    @Override
    @Transactional
    public /*ResponseRoomDTO*/ String createRoom(RequestRoomDTO roomDTO,String username) {

        if (Integer.parseInt(roomDTO.getCapacity())<0 || Double.parseDouble(roomDTO.getPrice())<0 ){
            throw new RuntimeException("Invalid data");
        }

        Rooms rooms = roomMapper.mapToEntity(roomDTO);

        Businesses businesses = businessRepository.findByBusinessName(roomDTO.getBusinessName())
                .orElseThrow(() -> new RecordNotFoundException("Business not found"));

        if (roomRepository.findByRoomNameAndBusinesses(roomDTO.getRoomName(), businesses).isPresent()) {
            throw new RecordAlreadyExistsException("Room with the same name already exists under this business");
        }

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!user.getRole().getRoleName().equals("ADMIN")) {
            throw new AuthenticationFailedException("User does not have sufficient privileges to add a business");
        }

        MultipartFile image = roomDTO.getImage();
        if (image != null && !image.isEmpty()) {
            if (image.getSize() > 100 * 1024) {
                throw new FileCouldNotBeSavedException("File size must be less than or equal to 100KB");
            }
            try {

                String uploadDir = "C:\\Users\\USER\\Desktop\\SavedPhotos\\Rooms\\";

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                File file = new File(uploadDir + fileName);

                image.transferTo(file);

                rooms.setImage(fileName);
            } catch (IOException e) {
                throw new FileCouldNotBeSavedException("Failed to save image file");
            }
        }

        rooms.setBusinesses(businesses);

        Rooms savedRoom = roomRepository.save(rooms);
        roomPricingServiceImpl.createDefaultRoomPricings(savedRoom);
//        return roomMapper.mapToDto(savedRoom);

        return "Room saved successfully";
    }

    @Override
    public ResponseRoomDTO updateRoom(RequestRoomDTO requestRoomDTO, Long id) {

        Rooms foundRoom = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));

        if (Integer.parseInt(requestRoomDTO.getCapacity())<0 || Double.parseDouble(requestRoomDTO.getPrice())<0 ){
            throw new RuntimeException("Invalid data");
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

        Rooms room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));

        roomRepository.delete(room);

    }
}
