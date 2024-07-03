package com.example.booking.dataproviders.services.impl;

import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.roomPricingDTOs.RequestRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomsPricingDTO;
import com.example.booking.dataproviders.entities.RoomPricing;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.mappers.RoomPricingMapper;
import com.example.booking.dataproviders.repositories.RoomPricingRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.RoomPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomPricingServiceImpl implements RoomPricingService {

    private final RoomPricingRepository roomPricingRepository;
    private final RoomRepository roomRepository;
    private final RoomPricingMapper roomPricingMapper;
    private final UserRepository userRepository;

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
    public List<ResponseRoomsPricingDTO> getWeekRoomPricings(Long roomId,String username) {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        if (!user.getRole().getRoleName().equals("USER")) {
            throw new AuthenticationFailedException("User does not have sufficient privileges to add a business");
        }

        List<RoomPricing> roomPricings = roomPricingRepository.findByRoom_RoomId(roomId);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        return today.datesUntil(today.plusDays(7))
                .map(date -> {
                    RoomPricing pricing = roomPricings.stream()
                            .filter(p -> p.getDayOfWeek() == date.getDayOfWeek())
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Room pricing not found for " + date.getDayOfWeek()));
                    return new ResponseRoomsPricingDTO(date.format(formatter), pricing.getPrice());
                })
                .collect(Collectors.toList());
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

    public void createDefaultRoomPricings(Rooms room) {
        EnumSet.allOf(DayOfWeek.class).forEach(dayOfWeek -> {
            RoomPricing roomPricing = new RoomPricing();
            roomPricing.setRoom(room);
            roomPricing.setDayOfWeek(dayOfWeek);
            roomPricing.setPrice(getAdjustedPriceForDay(room.getPrice(), dayOfWeek));
            roomPricingRepository.save(roomPricing);
        });
    }

    private Double getAdjustedPriceForDay(Double basePrice, DayOfWeek dayOfWeek) {
        double adjustedPrice = switch (dayOfWeek) {
            case MONDAY -> basePrice - 100.0;
            case TUESDAY -> basePrice - 50.0;
            case WEDNESDAY -> basePrice + 20.0;
            case THURSDAY -> basePrice + 30.0;
            case FRIDAY -> basePrice + 50.0;
            case SATURDAY -> basePrice + 70.0;
            case SUNDAY -> basePrice + 60.0;
//            default -> throw new IllegalArgumentException("Invalid day of week");
        };
        if (adjustedPrice < 0) {
            adjustedPrice = basePrice;
        }
        return adjustedPrice;
    }

}
