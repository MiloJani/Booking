package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.services.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public List<Booking> getBookingHistory() {

        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return bookingService.getBookingHistory(username);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseBookingDTO> saveBooking(@Valid @RequestBody RequestBookingDTO requestBookingDTO) {

        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
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
