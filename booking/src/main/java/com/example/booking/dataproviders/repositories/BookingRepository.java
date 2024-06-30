package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    int countByUser(User user);

    List<Booking> findByUser(User user);
}
