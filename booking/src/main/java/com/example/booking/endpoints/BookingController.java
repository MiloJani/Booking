package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.RequestBookingHistoryDto;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingDTO;
import com.example.booking.dataproviders.dto.bookingDTOs.ResponseBookingHistoryDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.services.BookingService;
import com.example.booking.dataproviders.services.utilities.UtilitiesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@SecurityRequirement(name = "Bearer authentication")
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private UtilitiesService utilitiesService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ResponseBookingDTO>> findAll() {

        return ResponseEntity.ok(bookingService.findAllBookings());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseBookingDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ResponseBookingHistoryDTO>> getBookingHistory(Principal principal) {
//        SecurityContext context = SecurityContextHolder.getContext();
//        String username = context.getAuthentication().getName();
//        String username = UtilitiesService.getCurrentUsername();
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
    public ResponseEntity<?/*ResponseBookingDTO*/> saveBooking(@Valid @RequestBody RequestBookingDTO requestBookingDTO
     ,Principal principal/*,BindingResult bindingResult*/) throws MethodArgumentNotValidException, NoSuchMethodException {

//        if (bindingResult.hasErrors()) {
//            MethodParameter methodParameter = new MethodParameter(this.getClass().getMethod("saveBooking", RequestBookingDTO.class, BindingResult.class), 0);
//            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
//        }


//        String username = UtilitiesService.getCurrentUsername();
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
