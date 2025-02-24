package com.example.booking.schedulers;

import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.repositories.BookingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookingStatusScheduler {

    private final BookingRepository bookingRepository;

    public BookingStatusScheduler(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

//    @Scheduled(cron = "0 0 3 * * *") // Runs every day at 3 am
@Scheduled(cron = "0 17 13 * * *")
@Transactional
    public void updateBookingStatuses() {

        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {
            if (booking.getCheckInDate().isAfter(LocalDate.now())) {
                booking.setStatus("Booked");
            } else if (LocalDate.now().isAfter(booking.getCheckOutDate())) {
                booking.setStatus("CheckedOut");
            } else {
                booking.setStatus("CheckedIn");
            }

            bookingRepository.save(booking);
        }
    }
}
