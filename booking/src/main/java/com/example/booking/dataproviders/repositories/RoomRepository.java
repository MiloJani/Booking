package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RoomRepository extends JpaRepository<Rooms,Long> {

//    @Query("SELECT COUNT(r) FROM Rooms r WHERE r.businesses.businessId = :businessId " +
//            "AND r.capacity >= :capacity " +
//            "AND NOT EXISTS (" +
//            "   SELECT 1 FROM Booking b " +
//            "   WHERE b.room.roomId = r.roomId " +
//            "   AND (b.checkInDate <= :checkoutDate AND b.checkOutDate >= :checkinDate)" +
//            ")")
//    @Query("SELECT COUNT(r) FROM Rooms r " +
//            "WHERE r.businesses.businessId = :businessId " +
//            "AND (CAST(:checkinDate AS DATE) IS NULL OR CAST(:checkoutDate AS DATE) IS NULL " +
//            "      OR NOT EXISTS (" +
//            "          SELECT 1 FROM Booking b " +
//            "          WHERE b.room.roomId = r.roomId " +
//            "          AND (b.checkInDate < CAST(:checkoutDate AS DATE) AND b.checkOutDate > CAST(:checkinDate AS DATE))" +
//            "          AND b.checkOutDate > CAST(:checkoutDate AS DATE)" +
//            "      )" +
//            "      AND NOT EXISTS (" +
//            "          SELECT 1 FROM Booking b " +
//            "          WHERE b.room.roomId = r.roomId " +
//            "          AND (b.checkInDate = CAST(:checkinDate AS DATE) OR b.checkOutDate = CAST(:checkoutDate AS DATE))" +
//            "      )" +
//            "      AND r.capacity >= :capacity)")
@Query("SELECT COUNT(r) FROM Rooms r " +
        "WHERE r.businesses.businessId = :businessId " +
        "AND (CAST(:checkinDate AS DATE) IS NULL OR CAST(:checkoutDate AS DATE) IS NULL " +
        "      OR NOT EXISTS (" +
        "          SELECT 1 FROM Booking b " +
        "          WHERE b.room.roomId = r.roomId " +
        "          AND (b.checkInDate < CAST(:checkoutDate AS DATE) AND b.checkOutDate > CAST(:checkinDate AS DATE))" +
        "      )" +
        "      AND NOT EXISTS (" +
        "          SELECT 1 FROM Booking b " +
        "          WHERE b.room.roomId = r.roomId " +
        "          AND (b.checkInDate = CAST(:checkinDate AS DATE) OR b.checkOutDate = CAST(:checkoutDate AS DATE))" +
        "      )" +
        "      AND NOT EXISTS (" +
        "          SELECT 1 FROM Booking b " +
        "          WHERE b.room.roomId = r.roomId " +
        "          AND (CAST(:checkinDate AS DATE) = b.checkOutDate OR CAST(:checkoutDate AS DATE) = b.checkInDate)" +
        "      )" +
        "      AND r.capacity >= :capacity)")
int countAvailableRooms(
            @Param("businessId") Long businessId,
            @Param("checkinDate") LocalDate checkinDate,
            @Param("checkoutDate") LocalDate checkoutDate,
            @Param("capacity") int capacity
    );

}
