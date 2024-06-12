package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.authDTOs.AuthenticationRequest;
import com.example.booking.dataproviders.dto.authDTOs.AuthenticationResponse;
import com.example.booking.dataproviders.dto.authDTOs.LogoutDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
import com.example.booking.dataproviders.services.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://192.168.1.78:3000")
@SecurityRequirement(name = "Bearer authentication")
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutDTO logoutDTO) {
        String message = authenticationService.logout(logoutDTO.getToken());
        return ResponseEntity.ok(message);
    }
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(@RequestBody String token) {
//        authenticationService.logout(token);
//        return ResponseEntity.ok().build();
//    }

}
