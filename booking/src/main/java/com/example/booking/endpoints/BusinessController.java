package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.services.BusinessService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ResponseBusinessDTO>> findAll() {

        return ResponseEntity.ok(businessService.findAllBusinesses());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseBusinessDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(businessService.findBusinessById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseBusinessDTO> saveBusiness(@Valid @RequestBody RequestBusinessDTO requestBusinessDTO) {
        return new ResponseEntity<>(businessService.saveBusiness(requestBusinessDTO), HttpStatus.CREATED);
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
