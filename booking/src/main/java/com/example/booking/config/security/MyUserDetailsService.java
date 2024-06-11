package com.example.booking.config.security;

import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailOrPhoneNumber) throws UsernameNotFoundException {
        User userEntity = userRepository.findByEmailOrPhoneNumberOrUsername(usernameOrEmailOrPhoneNumber, usernameOrEmailOrPhoneNumber,usernameOrEmailOrPhoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with: "+usernameOrEmailOrPhoneNumber));

        return new MyUserDetails(userEntity);
    }


}
