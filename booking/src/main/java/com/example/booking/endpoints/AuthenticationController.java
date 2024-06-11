package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.authDTOs.AuthenticationRequest;
import com.example.booking.dataproviders.dto.authDTOs.AuthenticationResponse;
import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
import com.example.booking.dataproviders.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseUserDTO> saveUser(@Valid @RequestBody RequestUserDTO requestUserDTO) {
        return new ResponseEntity<>(authenticationService.registerUser(requestUserDTO), HttpStatus.CREATED);
    }

    @PostMapping("/saveAdmin")
    public ResponseEntity<ResponseAdminDTO> saveAdmin(@Valid @RequestBody RequestAdminDTO requestAdminDTO) {
        return new ResponseEntity<>(authenticationService.registerAdmin(requestAdminDTO), HttpStatus.CREATED);
    }

}
