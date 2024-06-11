package com.example.booking.dataproviders.services.impl;

import com.example.booking.dataproviders.dto.authDTOs.AuthenticationRequest;
import com.example.booking.dataproviders.dto.authDTOs.AuthenticationResponse;
import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
import com.example.booking.dataproviders.entities.Role;
import com.example.booking.dataproviders.repositories.RoleRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import com.example.booking.dataproviders.services.AuthenticationService;
import com.example.booking.dataproviders.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmailOrPhoneNumber(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmailOrPhoneNumber());

            if (userDetails == null) {
                throw new RuntimeException("User not found");
            }

            var jwtToken = jwtService.generateToken(userDetails);
            String roleName = userDetails.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User has no roles assigned"));

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(jwtToken);
            authenticationResponse.setRoleName(roleName);
            return authenticationResponse;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed for user: " + request.getEmailOrPhoneNumber());
        }
    }

    @Override
    public ResponseUserDTO registerUser(RequestUserDTO requestUserDTO) {
        return userService.saveUser(requestUserDTO);
    }

    @Override
    public ResponseAdminDTO registerAdmin(RequestAdminDTO requestAdminDTO) {
        return userService.saveAdmin(requestAdminDTO);
    }
}
