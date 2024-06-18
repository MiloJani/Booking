package com.example.booking.endpoints;

import com.example.booking.dataproviders.dto.authDTOs.AuthenticationRequest;
import com.example.booking.dataproviders.dto.authDTOs.AuthenticationResponse;
import com.example.booking.dataproviders.dto.authDTOs.LogoutDTO;
import com.example.booking.dataproviders.dto.authDTOs.ResponseLogoutDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
import com.example.booking.dataproviders.services.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
//@CrossOrigin(origins = "http://192.168.1.78:3000")
@CrossOrigin(origins = "http://192.168.1.66:3000")
@SecurityRequirement(name = "Bearer authentication")
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<?/*AuthenticationResponse*/> authenticate(
            @Valid @RequestBody AuthenticationRequest request, BindingResult bindingResult
            ){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
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
    public ResponseEntity<ResponseLogoutDTO> logout(@Valid @RequestBody LogoutDTO logoutDTO) {
        ResponseLogoutDTO responseLogoutDTO = authenticationService.logout(logoutDTO.getToken());
        return ResponseEntity.ok(responseLogoutDTO);
    }
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(@RequestBody String token) {
//        authenticationService.logout(token);
//        return ResponseEntity.ok().build();
//    }

}
