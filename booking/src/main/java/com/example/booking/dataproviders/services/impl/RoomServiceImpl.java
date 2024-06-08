package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.mappers.RoomMapper;
import com.example.booking.dataproviders.repositories.BusinessRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public ResponseRoomDTO createRoom(RequestRoomDTO roomDTO) {

        Rooms rooms = roomMapper.mapToEntity(roomDTO);

        Businesses businesses = businessRepository.findById(roomDTO.getBusinessId()).orElseThrow(() -> new RuntimeException("Business not found"));

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
        foundRoom.setImage(requestRoomDTO.getImage());
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
