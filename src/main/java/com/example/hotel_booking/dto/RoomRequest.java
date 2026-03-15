package com.example.hotel_booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RoomRequest {

    @NotBlank(message = "Số phòng không được để trống")
    private String roomNumber;

    private Integer floor;
}