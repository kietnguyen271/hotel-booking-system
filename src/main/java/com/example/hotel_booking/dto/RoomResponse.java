package com.example.hotel_booking.dto;

import lombok.Data;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private Integer floor;
    private String status;
    private Long roomTypeId;
    private String roomTypeName;
    private Long hotelId;
}