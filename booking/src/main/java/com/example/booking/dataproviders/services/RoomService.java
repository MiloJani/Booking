package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.roomDTOs.RequestAvailableRoomsDTO;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {

    List<ResponseRoomDTO> findAllRooms();

    ResponseRoomDTO findRoomById(Long id);

    Page<ResponseRoomDTO> getAllAvailableRooms(RequestAvailableRoomsDTO requestAvailableRoomsDTO, String username);

    String createRoom(RequestRoomDTO roomDTO,String username);

    ResponseRoomDTO updateRoom(RequestRoomDTO roomDTO,Long id);

    void deleteRoom(Long id);
}
