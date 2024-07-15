package com.example.booking.endpoints;

import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.NotCorrectDataException;
import com.example.booking.core.exceptions.RecordAlreadyExistsException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.bookingDTOs.*;
import com.example.booking.dataproviders.services.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "Bearer authentication")
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ResponseBookingDTO>> findAll() {

        return ResponseEntity.ok(bookingService.findAllBookings());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseBookingDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ResponseBookingHistoryDTO>> getBookingHistory(Principal principal) throws RecordNotFoundException, AuthenticationFailedException {

        String username = principal.getName();
        return ResponseEntity.ok(bookingService.getBookingHistory(username));
    }

    @PostMapping("/save")
    public ResponseEntity<BookingResponseDTO> saveBooking(@Valid @RequestBody RequestBookingDTO requestBookingDTO
     , Principal principal/*,BindingResult bindingResult*/) throws RecordNotFoundException, RecordAlreadyExistsException,AuthenticationFailedException, NotCorrectDataException {

        String username = principal.getName();
        return new ResponseEntity<>(bookingService.saveBooking(requestBookingDTO,username), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }
}
