package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.roomPricingDTOs.RequestRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomPricingDTO;
import com.example.booking.dataproviders.dto.roomPricingDTOs.ResponseRoomsPricingDTO;
import com.example.booking.dataproviders.services.RoomPricingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "Bearer authentication")
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

    @GetMapping("/rooms/{roomId}/pricings")
    public List<ResponseRoomsPricingDTO> getRoomPricings(@PathVariable Long roomId) {

        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return roomPricingService.getWeekRoomPricings(roomId,username);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseRoomPricingDTO> saveRoomPricing(@Valid @RequestBody RequestRoomPricingDTO requestRoomPricingDTO) {
        return new ResponseEntity<>(roomPricingService.saveRoomPricing(requestRoomPricingDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseRoomPricingDTO> updateRoomPricing(@Valid @RequestBody RequestRoomPricingDTO requestRoomPricingDTO, @PathVariable Long id) {
        return ResponseEntity.ok(roomPricingService.updateRoomPricing(requestRoomPricingDTO,id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoomPricing(@PathVariable Long id) {
        roomPricingService.deleteRoomPricing(id);
        return ResponseEntity.ok().build();
    }
}
