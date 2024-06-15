package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.mappers.RoomMapper;
import com.example.booking.dataproviders.repositories.BusinessRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.services.RoomService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;
    private BusinessRepository businessRepository;
    private RoomMapper roomMapper;
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
    @Transactional
    public ResponseRoomDTO createRoom(RequestRoomDTO roomDTO,String email) {

        Rooms rooms = roomMapper.mapToEntity(roomDTO);

        Businesses businesses = businessRepository.findByBusinessName(roomDTO.getBusinessName()).orElseThrow(() -> new RuntimeException("Business not found"));

        MultipartFile image = roomDTO.getImage();
        if (image != null && !image.isEmpty()) {
            if (image.getSize() > 100 * 1024) {
                throw new RuntimeException("File size must be less than or equal to 100KB");
            }
            try {

                String uploadDir = "C:\\Users\\USER\\Desktop\\BookingProject\\Booking\\booking\\src\\main\\resources\\images\\rooms\\";

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                File file = new File(uploadDir + fileName);

                image.transferTo(file);

                rooms.setImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image file", e);
            }
        }

        rooms.setBusinesses(businesses);

        Rooms savedRoom = roomRepository.save(rooms);

        return roomMapper.mapToDto(savedRoom);
    }

    @Override
    public ResponseRoomDTO updateRoom(RequestRoomDTO requestRoomDTO, Long id) {

        Rooms foundRoom = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));

        foundRoom.setRoomName(requestRoomDTO.getRoomName());
        foundRoom.setCapacity(requestRoomDTO.getCapacity());
        foundRoom.setPrice(requestRoomDTO.getPrice());
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
