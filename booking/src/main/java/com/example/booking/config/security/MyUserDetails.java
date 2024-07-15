package com.example.booking.config.security;

import com.example.booking.dataproviders.entities.Role;
import com.example.booking.dataproviders.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class MyUserDetails implements UserDetails {

    private User userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role userRole = userEntity.getRole();
        Set<SimpleGrantedAuthority> listOfAllAccess = new HashSet<>();

        listOfAllAccess.add(new SimpleGrantedAuthority(userRole.getRoleName()));

        return listOfAllAccess;

//        return Collections.singleton(new SimpleGrantedAuthority(userRole.getRoleName()));
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
