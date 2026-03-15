package com.example.hotel_booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HotelRequest {

    @NotBlank(message = "Tên khách sạn không được để trống")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Thành phố không được để trống")
    private String city;

    private String description;

    @Min(value = 1, message = "Số sao tối thiểu là 1")
    @Max(value = 5, message = "Số sao tối đa là 5")
    private Integer starRating;
}