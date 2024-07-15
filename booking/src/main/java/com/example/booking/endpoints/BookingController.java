package com.example.booking.endpoints;

import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.NotCorrectDataException;
import com.example.booking.core.exceptions.RecordAlreadyExistsException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.bookingDTOs.*;
import com.example.booking.dataproviders.services.BookingService;
import com.example.booking.dataproviders.services.utilities.UtilitiesService;
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
    private final UtilitiesService utilitiesService;

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

//    @PostMapping("/history")
//    public Page<ResponseBookingHistoryDTO> getBookingHistory(@RequestBody RequestBookingHistoryDto requestDto) {
//        SecurityContext context = SecurityContextHolder.getContext();
//        String username = context.getAuthentication().getName();
//        return bookingService.getBookingHistory(username, requestDto);
//    }

    @PostMapping("/save")
    public ResponseEntity<BookingResponseDTO> saveBooking(@Valid @RequestBody RequestBookingDTO requestBookingDTO
     , Principal principal/*,BindingResult bindingResult*/) throws RecordNotFoundException, RecordAlreadyExistsException,AuthenticationFailedException, NotCorrectDataException {

        String username = principal.getName();
        return new ResponseEntity<>(bookingService.saveBooking(requestBookingDTO,username), HttpStatus.CREATED);
    }


//    @PutMapping("/update/{id}")
//    public ResponseEntity<ResponseBookingDTO> updateBusiness(@Valid @RequestBody RequestBookingDTO requestBookingDTO, @PathVariable Long id) {
//        return ResponseEntity.ok();
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }
}
