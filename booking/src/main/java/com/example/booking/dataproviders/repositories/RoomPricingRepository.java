package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.RoomPricing;
import com.example.booking.dataproviders.entities.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomPricingRepository extends JpaRepository<RoomPricing, Long> {

    List<RoomPricing> findByRoom_RoomId(Long roomId);
    Optional<RoomPricing> findByRoomAndDayOfWeek(Rooms room, DayOfWeek dayOfWeek);
}
