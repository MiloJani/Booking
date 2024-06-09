package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.roomPricingDTOs.RequestRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomPricingDTO;
import com.example.booking.dataproviders.entities.RoomPricing;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.mappers.RoomPricingMapper;
import com.example.booking.dataproviders.repositories.RoomPricingRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.services.RoomPricingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomPricingServiceImpl implements RoomPricingService {

    private final RoomPricingRepository roomPricingRepository;
    private final RoomRepository roomRepository;
    private final RoomPricingMapper roomPricingMapper;

    @Override
    public List<ResponseRoomPricingDTO> findAllRoomPricings() {

        List<RoomPricing> roomPricings = roomPricingRepository.findAll();
        List<ResponseRoomPricingDTO> responseRoomPricingDTOS = new ArrayList<>();
        for (RoomPricing roomPricing : roomPricings) {
            responseRoomPricingDTOS.add(roomPricingMapper.mapToDto(roomPricing));
        }
        return responseRoomPricingDTOS;
    }

    @Override
    public ResponseRoomPricingDTO findRoomPricingById(Long id) {

        RoomPricing roomPricing = roomPricingRepository.findById(id).orElseThrow(() -> new RuntimeException("Room pricing not found"));

        return roomPricingMapper.mapToDto(roomPricing);
    }

    @Override
    public ResponseRoomPricingDTO saveRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO) {

        RoomPricing roomPricing = roomPricingMapper.mapToEntity(requestRoomPricingDTO);
        Rooms room = roomRepository.findById(requestRoomPricingDTO.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        roomPricing.setRoom(room);
        RoomPricing savedRoomPricing = roomPricingRepository.save(roomPricing);
        return roomPricingMapper.mapToDto(savedRoomPricing);
    }

    @Override
    public ResponseRoomPricingDTO updateRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO, Long id) {

        RoomPricing foundRoomPricing = roomPricingRepository.findById(id).orElseThrow(() -> new RuntimeException("Room pricing not found"));
        foundRoomPricing.setDayOfWeek(requestRoomPricingDTO.getDayOfWeek());
        foundRoomPricing.setPrice(requestRoomPricingDTO.getPrice());

        RoomPricing updatedRoomPricing = roomPricingRepository.save(foundRoomPricing);
        return roomPricingMapper.mapToDto(updatedRoomPricing);
    }

    @Override
    public void deleteRoomPricing(Long id) {

        RoomPricing roomPricing = roomPricingRepository.findById(id).orElseThrow(() -> new RuntimeException("Room pricing not found"));

        roomPricingRepository.delete(roomPricing);
    }
}
