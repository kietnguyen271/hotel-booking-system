package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.*;
import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.service.HotelService;
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
public class HotelController {

    private final HotelService hotelService;
    private final UserRepository userRepository;

    // ── PUBLIC API ──

    // Lấy tất cả hotel
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    // Xem chi tiết hotel
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    // Tìm kiếm hotel
    @GetMapping("/hotels/search")
    public ResponseEntity<List<HotelResponse>> searchHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer starRating,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(
                hotelService.searchHotels(city, starRating, name));
    }

    // ── HOTEL OWNER API ──

    // Tạo hotel mới (Hotel Owner)
    @PostMapping("/hotel-owner/hotels")
    public ResponseEntity<HotelResponse> createHotel(
            @Valid @RequestBody HotelRequest request,
            Authentication authentication) {

        // Lấy user đang login
        String email = authentication.getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        return ResponseEntity.ok(hotelService.createHotel(request, owner));
    }

    // Sửa hotel (Hotel Owner)
    @PutMapping("/hotel-owner/hotels/{id}")
    public ResponseEntity<HotelResponse> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        return ResponseEntity.ok(hotelService.updateHotel(id, request, owner));
    }

    // Xóa hotel (Hotel Owner)
    @DeleteMapping("/hotel-owner/hotels/{id}")
    public ResponseEntity<String> deleteHotel(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        hotelService.deleteHotel(id, owner);
        return ResponseEntity.ok("Xóa hotel thành công!");
    }

    // ── ADMIN API ──

    // Admin xóa bất kỳ hotel nào
    @DeleteMapping("/admin/hotels/{id}")
    public ResponseEntity<String> adminDeleteHotel(@PathVariable Long id) {
        hotelService.adminDeleteHotel(id);
        return ResponseEntity.ok("Admin đã xóa hotel!");
    }
}