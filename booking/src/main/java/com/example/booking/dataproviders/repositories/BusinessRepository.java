package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Businesses,Long>, JpaSpecificationExecutor<Businesses> {
    Optional<Businesses> findByBusinessName(String businessName);
    List<Businesses> findByAdmin(User admin);
}
