package com.example.booking.dataproviders.repositories;

import com.example.booking.dataproviders.entities.Businesses;
import com.example.booking.dataproviders.entities.Rooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Rooms,Long> {


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


    @Query("SELECT r.roomId FROM Rooms r " +
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
    List<Long> findAvailableRoomIds(
            @Param("businessId") Long businessId,
            @Param("checkinDate") LocalDate checkinDate,
            @Param("checkoutDate") LocalDate checkoutDate,
            @Param("capacity") int capacity
    );


    Optional<Rooms> findByRoomNameAndBusinesses(String roomName, Businesses businesses);

    Page<Rooms> findByBusinesses_BusinessIdAndRoomIdIn(Long businessId, Set<Long> roomIds, Pageable pageable);

}
