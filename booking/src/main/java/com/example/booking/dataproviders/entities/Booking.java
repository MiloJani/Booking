package com.example.booking.dataproviders.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOKING")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "BOOKING_DATE",nullable = false)
    private LocalDate bookingDate;

    @Column(name = "CHECK_IN_DATE",nullable = false)
    private LocalDate checkInDate;

    @Column(name = "CHECK_OUT_DATE",nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "NO_OF_ADULTS",nullable = false)
    private Integer noOfAdults;

    @Column(name = "NO_OF_CHILDREN",nullable = false)
    private Integer noOfChildren;

    @Column(name = "STATUS",nullable = false)
    private String status;

    //booked for
    @Column(name = "FULL_NAME",nullable = false)
    private String fullName;

    @Column(name = "EMAIL",nullable = false)
    private String email;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "PHONE_NUMBER",nullable = false)
    private String phoneNumber;

    //

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private Rooms room;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

}
