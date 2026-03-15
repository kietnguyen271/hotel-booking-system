package com.example.hotel_booking.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomTypeResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal pricePerNight;
    private Integer maxGuests;
    private Long hotelId;
    private String hotelName;
}