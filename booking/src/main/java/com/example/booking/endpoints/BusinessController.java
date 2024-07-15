package com.example.booking.endpoints;

import com.example.booking.core.exceptions.*;
import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.services.BusinessService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@SecurityRequirement(name = "Bearer authentication")
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping("/prove")
    public ResponseEntity<String> hi(){
        return ResponseEntity.ok("Hi");
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ResponseBusinessDTO>> findAll() {

        return ResponseEntity.ok(businessService.findAllBusinesses());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseBusinessDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(businessService.findBusinessById(id));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<String>> findAllBusinessesOfAdmin(Principal principal) throws RecordNotFoundException, AuthenticationFailedException, NotCorrectDataException {

        String username = principal.getName();
            List<String> businesses = businessService.findAllBusinessesOfAdmin(username);
            return new ResponseEntity<>(businesses, HttpStatus.OK);

    }

    @PostMapping("/search")
    public ResponseEntity<Page<ResponseSearchDTO>> searchBookings(@Valid @RequestBody RequestSearchDTO searchRequest,Principal principal
                                                                  /*BindingResult bindingResult*/) throws RecordNotFoundException,AuthenticationFailedException,NotCorrectDataException {

        String username = principal.getName();
        Page<ResponseSearchDTO> searchResults = businessService.search(searchRequest,username);
        return ResponseEntity.ok(searchResults);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveBusiness(@Valid @ModelAttribute RequestBusinessDTO requestBusinessDTO,Principal principal
                                                            /*,BindingResult bindingResult*/
            /*@Valid @RequestBody RequestBusinessDTO requestBusinessDTO*/) throws RecordAlreadyExistsException,RecordNotFoundException,AuthenticationFailedException,NotCorrectDataException, FileCouldNotBeSavedException {


        String username = principal.getName();
        return new ResponseEntity<>(businessService.saveBusiness(requestBusinessDTO,username), HttpStatus.CREATED);


    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseBusinessDTO> updateBusiness(@Valid @RequestBody RequestBusinessDTO requestBusinessDTO, @PathVariable Long id) {
        return ResponseEntity.ok(businessService.updateBusiness(requestBusinessDTO,id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.ok().build();
    }
}
