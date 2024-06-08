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
@Table(name = "USER_INFO")
public class UserInfo {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "REGISTER_DATE", nullable = false)
    private LocalDate registerDate;

    @Column(name = "DISCOUNT_POINTS", nullable = false)
    private int discountPoints;
}
