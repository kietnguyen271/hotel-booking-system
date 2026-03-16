package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.*;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    // ── USER API ──

    // Tạo booking
    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {

        User user = getUser(authentication);
        return ResponseEntity.ok(
                bookingService.createBooking(request, user));
    }

    // Xem lịch sử booking của mình
    @GetMapping("/bookings/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            Authentication authentication) {

        User user = getUser(authentication);
        return ResponseEntity.ok(
                bookingService.getMyBookings(user));
    }

    // Hủy booking
    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getUser(authentication);
        return ResponseEntity.ok(
                bookingService.cancelBooking(id, user));
    }

    // ── HOTEL OWNER API ──

    // Xem booking của khách sạn mình
    @GetMapping("/hotel-owner/hotels/{hotelId}/bookings")
    public ResponseEntity<List<BookingResponse>> getHotelBookings(
            @PathVariable Long hotelId,
            Authentication authentication) {

        User owner = getUser(authentication);
        return ResponseEntity.ok(
                bookingService.getHotelBookings(hotelId, owner));
    }

    // ── HELPER ──
    private User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User không tồn tại!"));
    }
}