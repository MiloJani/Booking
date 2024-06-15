package com.example.booking.dataproviders.services;

import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;

import java.util.List;

public interface RoomService {

    List<ResponseRoomDTO> findAllRooms();

    ResponseRoomDTO findRoomById(Long id);

    ResponseRoomDTO createRoom(RequestRoomDTO roomDTO,String username);

    ResponseRoomDTO updateRoom(RequestRoomDTO roomDTO,Long id);

    void deleteRoom(Long id);
}
