package com.example.hotel_booking.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookingDetailResponse {
    private Long id;
    private String roomNumber;
    private String roomTypeName;
    private Integer floor;
    private BigDecimal price;
}