package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.roomDTOs.RequestAvailableRoomsDTO;
import com.example.booking.dataproviders.dto.roomDTOs.RequestRoomDTO;
import com.example.booking.dataproviders.dto.roomDTOs.ResponseRoomDTO;
import com.example.booking.dataproviders.services.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/getAvailableRooms")
    public ResponseEntity<?> getAvailableRooms(@Valid @RequestBody RequestAvailableRoomsDTO requestAvailableRoomsDTO,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return new ResponseEntity<>(roomService.getAllAvailableRooms(requestAvailableRoomsDTO,username), HttpStatus.OK);

    }

    @PostMapping("/save")
    public ResponseEntity<?/*ResponseRoomDTO*/> saveRoom(@Valid @ModelAttribute RequestRoomDTO requestRoomDTO/*@Valid @RequestBody RequestRoomDTO requestRoomDTO*/
    , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
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
