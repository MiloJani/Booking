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
@Table(name = "BUSINESS")
public class Businesses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessId;

    @Column(name = "BUSINESS_NAME",nullable = false,unique = true)
    private String businessName;

    @Column(name = "FREE_PARKING",nullable = false)
    private boolean freeParking;

    @Column(name = "FREE_WIFI",nullable = false)
    private boolean freeWifi;

    @Column(name = "INSIDE_POOL",nullable = false)
    private boolean insidePool;

    @Column(name = "FREE_BREAKFAST",nullable = false)
    private boolean freeBreakfast;

    @Column(name = "IMAGE",nullable = false)
    private String image;

    @Column(name = "TAX",nullable = false)
    private Double tax=0.07;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private User admin;

    @OneToMany(mappedBy = "businesses", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Rooms> rooms; //lazy

}
