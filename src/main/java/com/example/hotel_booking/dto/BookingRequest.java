package com.example.hotel_booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequest {

    @NotNull(message = "Hotel không được để trống")
    private Long hotelId;

    @NotNull(message = "Loại phòng không được để trống")
    private Long roomTypeId;

    @NotNull(message = "Ngày check-in không được để trống")
    private LocalDate checkInDate;

    @NotNull(message = "Ngày check-out không được để trống")
    private LocalDate checkOutDate;
}