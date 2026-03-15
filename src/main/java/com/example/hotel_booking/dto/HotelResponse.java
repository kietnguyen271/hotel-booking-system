package com.example.hotel_booking.dto;

import lombok.Data;

@Data
public class HotelResponse {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String description;
    private Integer starRating;
}