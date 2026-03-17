package com.example.hotel_booking.dto;

import lombok.Data;

@Data
public class HotelImageResponse {
    private Long id;
    private String url;
    private Boolean isMain;
    private Long hotelId;
}