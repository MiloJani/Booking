package com.example.booking.dataproviders.services.impl;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.dto.authDTOs.AuthenticationRequest;
import com.example.booking.dataproviders.dto.authDTOs.AuthenticationResponse;
import com.example.booking.dataproviders.dto.authDTOs.ResponseLogoutDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.repositories.BookingRepository;
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
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            if (userDetails == null) {
                throw new RecordNotFoundException(Constants.USER_NOT_FOUND);
            }

            var jwtToken = jwtService.generateToken(userDetails);
            String roleName = userDetails.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .findFirst()
                    .orElseThrow(() -> new AuthenticationFailedException(Constants.INSUFFICIENT_PRIVILEGES));


            return generateAuthenticationResponse(userDetails, jwtToken, roleName);

        } catch (Exception e) {
            throw new AuthenticationFailedException(Constants.UNABLE_TO_LOGIN);
        }
    }

    private AuthenticationResponse generateAuthenticationResponse(UserDetails userDetails, String jwtToken, String roleName) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtToken);
        response.setRoleName(roleName);

        if (Objects.equals(roleName, "USER")) {
            User user = userRepository.findUserByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RecordNotFoundException(Constants.USER_NOT_FOUND));
            response.setFullName(user.getUserInfo().getFullName());
            response.setPoints(user.getUserInfo().getDiscountPoints());
            response.setBooks(bookingRepository.countByUser(user));
        } else if (Objects.equals(roleName, "ADMIN")) {
            response.setFullName("ADMIN");
        } else {
            throw new AuthenticationFailedException(Constants.INSUFFICIENT_PRIVILEGES);
        }

        return response;
    }

    @Override
    public ResponseUserDTO registerUser(RequestUserDTO requestUserDTO) {
        return userService.saveUser(requestUserDTO);
    }

    @Override
    public ResponseAdminDTO registerAdmin(RequestAdminDTO requestAdminDTO) {
        return userService.saveAdmin(requestAdminDTO);
    }

    @Override
    public ResponseLogoutDTO logout(String token) {
        jwtService.invalidateToken(token);
        //        SecurityContextHolder.clearContext();
        ResponseLogoutDTO responseLogoutDTO = new ResponseLogoutDTO();
        responseLogoutDTO.setMessage(Constants.LOGOUT);
        return responseLogoutDTO;
    }

}
