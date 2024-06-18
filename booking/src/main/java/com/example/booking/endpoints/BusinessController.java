package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.services.BusinessService;
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
    public ResponseEntity<List<String>> findAllBusinessesOfAdmin() {

            SecurityContext context = SecurityContextHolder.getContext();
            String username = context.getAuthentication().getName();
            List<String> businesses = businessService.findAllBusinessesOfAdmin(username);
            return new ResponseEntity<>(businesses, HttpStatus.OK);

    }

    @PostMapping("/save")
    public ResponseEntity<?/*ResponseBusinessDTO*/> saveBusiness(@Valid @ModelAttribute RequestBusinessDTO requestBusinessDTO,
                                                            BindingResult bindingResult
            /*@Valid @RequestBody RequestBusinessDTO requestBusinessDTO*/) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }


        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
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
