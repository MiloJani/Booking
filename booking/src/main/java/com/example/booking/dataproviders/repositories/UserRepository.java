package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrPhoneNumberOrUsername(String email, String phoneNumber,String username);
    Optional<User> findUserByUsername(String username);
}
