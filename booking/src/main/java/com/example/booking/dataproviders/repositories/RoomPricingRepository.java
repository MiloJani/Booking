package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.RoomPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomPricingRepository extends JpaRepository<RoomPricing, Long> {

    List<RoomPricing> findByRoom_RoomId(Long roomId);
}
