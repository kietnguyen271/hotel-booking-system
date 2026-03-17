package com.example.hotel_booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "hotels")
public class Hotel {

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelImage> images;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // chủ khách sạn

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Integer starRating; // 1 - 5 sao

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<RoomType> roomTypes;
}