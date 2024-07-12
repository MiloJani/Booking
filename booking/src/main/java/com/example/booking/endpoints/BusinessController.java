package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.businessDTOs.RequestBusinessDTO;
import com.example.booking.dataproviders.dto.businessDTOs.ResponseBusinessDTO;
import com.example.booking.dataproviders.dto.searchDTOs.RequestSearchDTO;
import com.example.booking.dataproviders.dto.searchDTOs.ResponseSearchDTO;
import com.example.booking.dataproviders.services.BusinessService;
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
    public ResponseEntity<List<String>> findAllBusinessesOfAdmin(Principal principal) {

//            SecurityContext context = SecurityContextHolder.getContext();
//            String username = context.getAuthentication().getName();
//        String username = UtilitiesService.getCurrentUsername();
        String username = principal.getName();
            List<String> businesses = businessService.findAllBusinessesOfAdmin(username);
            return new ResponseEntity<>(businesses, HttpStatus.OK);

    }

    @PostMapping("/search")
    public ResponseEntity<?/*Page<ResponseSearchDTO>*/> searchBookings(@Valid @RequestBody RequestSearchDTO searchRequest,Principal principal
                                                                  /*BindingResult bindingResult*/) throws MethodArgumentNotValidException, NoSuchMethodException {
//        if (bindingResult.hasErrors()) {
//            MethodParameter methodParameter = new MethodParameter(this.getClass().getMethod("searchBookings", RequestSearchDTO.class, BindingResult.class), 0);
//            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
//        }

//        String username = UtilitiesService.getCurrentUsername();
        String username = principal.getName();
        Page<ResponseSearchDTO> searchResults = businessService.search(searchRequest,username);
        return ResponseEntity.ok(searchResults);
    }

    @PostMapping("/save")
    public ResponseEntity<?/*ResponseBusinessDTO*/> saveBusiness(@Valid @ModelAttribute RequestBusinessDTO requestBusinessDTO,Principal principal
                                                            /*,BindingResult bindingResult*/
            /*@Valid @RequestBody RequestBusinessDTO requestBusinessDTO*/) throws MethodArgumentNotValidException, NoSuchMethodException {

//        if (bindingResult.hasErrors()) {
//            MethodParameter methodParameter = new MethodParameter(this.getClass().getMethod("saveBusiness", RequestBusinessDTO.class, BindingResult.class), 0);
//            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
//        }


//        SecurityContext context = SecurityContextHolder.getContext();
//        String username = context.getAuthentication().getName();
//        String username = UtilitiesService.getCurrentUsername();
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
