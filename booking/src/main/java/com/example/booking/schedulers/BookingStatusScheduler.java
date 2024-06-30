package com.example.booking.schedulers;

import com.example.booking.dataproviders.entities.Booking;
import com.example.booking.dataproviders.repositories.BookingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingStatusScheduler {

    private final BookingRepository bookingRepository;

    public BookingStatusScheduler(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Scheduled(cron = "0 0 3 * * *") // Runs every day at 3 am
    @Transactional
    public void updateBookingStatuses() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {
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
            bookingRepository.save(booking);
        }
    }
}
