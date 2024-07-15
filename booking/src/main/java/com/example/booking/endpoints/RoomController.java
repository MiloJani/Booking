package com.example.booking.endpoints;

import com.example.booking.core.exceptions.NotCorrectDataException;
import com.example.booking.core.exceptions.RecordAlreadyExistsException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.roomDTOs.RequestAvailableRoomsDTO;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.services.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/rooms")
@SecurityRequirement(name = "Bearer authentication")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ResponseRoomDTO>> findAll() {

        return ResponseEntity.ok(roomService.findAllRooms());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseRoomDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findRoomById(id));
    }

    @PostMapping("/getAvailableRooms")
    public ResponseEntity<Page<ResponseRoomDTO>> getAvailableRooms(@Valid @RequestBody RequestAvailableRoomsDTO requestAvailableRoomsDTO, Principal principal
                                               /*BindingResult bindingResult*/) throws RecordNotFoundException {

        String username = principal.getName();
        return new ResponseEntity<>(roomService.getAllAvailableRooms(requestAvailableRoomsDTO,username), HttpStatus.OK);

    }

    @PostMapping("/save")
    public ResponseEntity<String> saveRoom(@Valid @ModelAttribute RequestRoomDTO requestRoomDTO/*@Valid @RequestBody RequestRoomDTO requestRoomDTO*/
    ,Principal principal/* BindingResult bindingResult*/) throws NotCorrectDataException,RecordNotFoundException, RecordAlreadyExistsException {


        String username = principal.getName();
        return new ResponseEntity<>(roomService.createRoom(requestRoomDTO,username), HttpStatus.CREATED);


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
