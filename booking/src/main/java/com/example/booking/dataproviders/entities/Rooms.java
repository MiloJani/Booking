package com.example.booking.dataproviders.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROOM")
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(name = "ROOM_NAME",nullable = false)
    private String roomName;

    @Column(name = "CAPACITY",nullable = false)
    private Integer capacity;

    @Column(name = "PRICE",nullable = false)
    private Double price;

    @Column(name = "DESCRIPTION",nullable = false)
    private String description;

    @Column(name = "ROOM_TYPE",nullable = false)
    private String roomType;

    @Column(name = "IMAGE",nullable = false)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID",nullable = false)
    private Businesses businesses;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RoomPricing> roomPricings;
}
