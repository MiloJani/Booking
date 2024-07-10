package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    int countByUser(User user);

//    Page<Booking> findByUser(User user, Pageable pageable);

    List<Booking> findByUser(User user);

    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId AND " +
            "(b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate);
}
