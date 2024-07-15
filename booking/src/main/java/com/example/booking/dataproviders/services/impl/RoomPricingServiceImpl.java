package com.example.booking.dataproviders.services.impl;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.roomPricingDTOs.*;
import com.example.booking.dataproviders.entities.RoomPricing;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.mappers.RoomPricingMapper;
import com.example.booking.dataproviders.repositories.RoomPricingRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.services.RoomPricingService;
import com.example.booking.dataproviders.services.utilities.UtilitiesService;
import com.example.booking.dataproviders.services.utilities.ValidationUtilities;
import jakarta.transaction.Transactional;
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
    private final UtilitiesService utilitiesService;

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
    @Transactional
    public WeekRoomPricingResponseDTO getWeekRoomPricings(RequestRoomPricingsDTO roomPricingsDTO, String username) {

        User user = utilitiesService.validateUser(username,Constants.USER);

        //the discount which will be deducted from final price
        int discountPoints = user.getUserInfo().getDiscountPoints();
        double discount = discountPoints >= Constants.DISCOUNT_THRESHOLD ? discountPoints * Constants.DISCOUNT_MULTIPLIER : 0;

        Rooms room = roomRepository.findById(roomPricingsDTO.getRoomId())
                .orElseThrow(() -> new RecordNotFoundException(Constants.ROOM_NOT_FOUND));

        //get all roomPricing for the given room
        List<RoomPricing> roomPricings = roomPricingRepository.findByRoom_RoomId(roomPricingsDTO.getRoomId());

        //validating dates and converting them from string to local date
        ValidationUtilities.validateDates(roomPricingsDTO.getCheckInDate()
                ,roomPricingsDTO.getCheckOutDate());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(roomPricingsDTO.getCheckInDate(), formatter);
        LocalDate checkOutDate = LocalDate.parse(roomPricingsDTO.getCheckOutDate(), formatter);

        //from checkIn Date to check out Date return roomPricing for every date in between(could limit it to max 7(.limit(7)) but front said he would handle it)
        List<ResponseRoomsPricingDTO> responsePricings = checkInDate.datesUntil(checkOutDate.plusDays(1))
                .map(date -> {
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    RoomPricing pricing = roomPricings.stream()
                            .filter(p -> p.getDayOfWeek() == dayOfWeek)
                            .findFirst()
                            .orElseThrow(() -> new RecordNotFoundException(Constants.ROOM_PRICING_NOT_FOUND + dayOfWeek));
                    return new ResponseRoomsPricingDTO(date.format(formatter), pricing.getPrice());
                })
                .collect(Collectors.toList());


        return roomPricingMapper.mapToWeekRoomPricingResponseDTO(room,responsePricings,discount);
    }


    @Override
    public ResponseRoomPricingDTO findRoomPricingById(Long id) {

        RoomPricing roomPricing = roomPricingRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Constants.ROOM_PRICING_NOT_FOUND));

        return roomPricingMapper.mapToDto(roomPricing);
    }



    @Override
    public ResponseRoomPricingDTO saveRoomPricing(RequestRoomPricingDTO requestRoomPricingDTO) {

        RoomPricing roomPricing = roomPricingMapper.mapToEntity(requestRoomPricingDTO);
        Rooms room = roomRepository.findById(requestRoomPricingDTO.getRoomId()).orElseThrow(() -> new RecordNotFoundException(Constants.ROOM_NOT_FOUND));
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

        RoomPricing roomPricing = roomPricingRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Constants.ROOM_PRICING_NOT_FOUND));

        roomPricingRepository.delete(roomPricing);
    }

    //default methods to generate roomPricing for each day of the week when a room is saved
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
//        return Math.max(adjustedPrice, 0);
    }

}
