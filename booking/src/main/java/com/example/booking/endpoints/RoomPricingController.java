package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.RequestRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomPricingDTO;
import com.example.booking.dataproviders.services.RoomPricingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/roomPricing")
public class RoomPricingController {

    private final RoomPricingService roomPricingService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ResponseRoomPricingDTO>> findAll() {

        return ResponseEntity.ok(roomPricingService.findAllRoomPricings());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseRoomPricingDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomPricingService.findRoomPricingById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseRoomPricingDTO> saveRoom(@Valid @RequestBody RequestRoomPricingDTO requestRoomPricingDTO) {
        return new ResponseEntity<>(roomPricingService.saveRoomPricing(requestRoomPricingDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseRoomPricingDTO> updateBusiness(@Valid @RequestBody RequestRoomPricingDTO requestRoomPricingDTO, @PathVariable Long id) {
        return ResponseEntity.ok(roomPricingService.updateRoomPricing(requestRoomPricingDTO,id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id) {
        roomPricingService.deleteRoomPricing(id);
        return ResponseEntity.ok().build();
    }
}
