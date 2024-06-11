package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.services.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rooms")
@SecurityRequirement(name = "Bearer authentication")
public class RoomController {

    private RoomService roomService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ResponseRoomDTO>> findAll() {

        return ResponseEntity.ok(roomService.findAllRooms());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseRoomDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findRoomById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseRoomDTO> saveRoom(@Valid @RequestBody RequestRoomDTO requestRoomDTO) {
        return new ResponseEntity<>(roomService.createRoom(requestRoomDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseRoomDTO> updateRoom(@Valid @RequestBody RequestRoomDTO requestRoomDTO, @PathVariable Long id) {
        return ResponseEntity.ok(roomService.updateRoom(requestRoomDTO,id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }
}
