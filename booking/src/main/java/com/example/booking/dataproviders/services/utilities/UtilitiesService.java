package com.example.booking.dataproviders.services.utilities;

import com.example.booking.constants.Constants;
import com.example.booking.core.exceptions.AuthenticationFailedException;
import com.example.booking.core.exceptions.RecordNotFoundException;
import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.entities.RoomPricing;
import com.example.booking.dataproviders.entities.Rooms;
import com.example.booking.dataproviders.entities.User;
import com.example.booking.dataproviders.repositories.RoomPricingRepository;
import com.example.booking.dataproviders.repositories.RoomRepository;
import com.example.booking.dataproviders.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

@Service
//@NoArgsConstructor
@AllArgsConstructor
public class UtilitiesService {

    private UserRepository userRepository;
    private RoomPricingRepository roomPricingRepository;

    public void setStatus(Booking booking) {
        if (booking.getBookingDate().isEqual(booking.getCheckInDate()) ||
                booking.getBookingDate().isEqual(booking.getCheckOutDate()) ||
                (booking.getBookingDate().isAfter(booking.getCheckInDate()) &&
                        booking.getBookingDate().isBefore(booking.getCheckOutDate()))) {
            booking.setStatus("Checked In");
        } else if (booking.getBookingDate().isBefore(booking.getCheckInDate())) {
            booking.setStatus("Booked");
        } else {
            booking.setStatus("Checked Out");
        }
    }

    public double calculateTotalPrice(Rooms room, LocalDate checkInDate, LocalDate checkOutDate) {
        double totalPrice = 0.0;

        LocalDate currentDate = checkInDate;
        while (!currentDate.isAfter(checkOutDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            Optional<RoomPricing> optionalRoomPricing = roomPricingRepository.findByRoomAndDayOfWeek(room, dayOfWeek);
            if (optionalRoomPricing.isPresent()) {
                totalPrice += optionalRoomPricing.get().getPrice();
            } else {
                throw new RecordNotFoundException(Constants.ROOM_PRICING_NOT_FOUND + dayOfWeek);
            }
            currentDate = currentDate.plusDays(1);
        }

        return totalPrice;
    }

    public User validateUser(String username, String role) {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException(Constants.USER_NOT_FOUND));
        if (!user.getRole().getRoleName().equals(role)) {
            throw new AuthenticationFailedException(Constants.INSUFFICIENT_PRIVILEGES);
        }

        return user;
    }

}
