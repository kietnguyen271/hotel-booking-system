package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.*;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.service.RoomService;
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
public class RoomController {

    private final RoomService roomService;
    private final UserRepository userRepository;

    // ── PUBLIC API ──

    // Xem loại phòng theo hotel
    @GetMapping("/hotels/{hotelId}/room-types")
    public ResponseEntity<List<RoomTypeResponse>> getRoomTypes(
            @PathVariable Long hotelId) {
        return ResponseEntity.ok(
                roomService.getRoomTypesByHotel(hotelId));
    }

    // Xem phòng theo loại phòng
    @GetMapping("/room-types/{roomTypeId}/rooms")
    public ResponseEntity<List<RoomResponse>> getRooms(
            @PathVariable Long roomTypeId) {
        return ResponseEntity.ok(
                roomService.getRoomsByRoomType(roomTypeId));
    }

    // ── HOTEL OWNER API ──

    // Tạo loại phòng cho hotel
    @PostMapping("/hotel-owner/hotels/{hotelId}/room-types")
    public ResponseEntity<RoomTypeResponse> createRoomType(
            @PathVariable Long hotelId,
            @Valid @RequestBody RoomTypeRequest request,
            Authentication authentication) {

        User owner = getOwner(authentication);
        return ResponseEntity.ok(
                roomService.createRoomType(hotelId, request, owner));
    }

    // Tạo phòng cụ thể
    @PostMapping("/hotel-owner/room-types/{roomTypeId}/rooms")
    public ResponseEntity<RoomResponse> createRoom(
            @PathVariable Long roomTypeId,
            @Valid @RequestBody RoomRequest request,
            Authentication authentication) {

        User owner = getOwner(authentication);
        return ResponseEntity.ok(
                roomService.createRoom(roomTypeId, request, owner));
    }

    // ── HELPER ──
    private User getOwner(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User không tồn tại!"));
    }
}