package com.example.hotel_booking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID tự tăng 1,2,3...
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}