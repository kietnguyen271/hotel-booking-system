package com.example.hotel_booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomTypeRequest {

    @NotBlank(message = "Tên loại phòng không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá không được âm")
    private BigDecimal pricePerNight;

    @NotNull(message = "Số khách không được để trống")
    @Min(value = 1, message = "Tối thiểu 1 khách")
    private Integer maxGuests;
}