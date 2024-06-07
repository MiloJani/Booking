package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Rooms,Long> {
}
