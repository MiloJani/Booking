package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.Businesses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Businesses,Long> {
}
