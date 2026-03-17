package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.HotelImageResponse;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageService imageService;
    private final UserRepository userRepository;

    // ── PUBLIC ──

    // Xem ảnh của hotel
    @GetMapping("/hotels/{hotelId}/images")
    public ResponseEntity<List<HotelImageResponse>> getHotelImages(
            @PathVariable Long hotelId) {
        return ResponseEntity.ok(
                imageService.getHotelImages(hotelId));
    }

    // ── HOTEL OWNER ──

    // Upload ảnh
    @PostMapping("/hotel-owner/hotels/{hotelId}/images")
    public ResponseEntity<HotelImageResponse> uploadImage(
            @PathVariable Long hotelId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isMain", defaultValue = "false") Boolean isMain,
            Authentication authentication) throws IOException {

        User owner = getOwner(authentication);
        return ResponseEntity.ok(
                imageService.uploadImage(hotelId, file, isMain, owner));
    }

    // Xóa ảnh
    @DeleteMapping("/hotel-owner/images/{imageId}")
    public ResponseEntity<String> deleteImage(
            @PathVariable Long imageId,
            Authentication authentication) throws IOException {

        User owner = getOwner(authentication);
        imageService.deleteImage(imageId, owner);
        return ResponseEntity.ok("Xóa ảnh thành công!");
    }

    // ── HELPER ──
    private User getOwner(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User không tồn tại!"));
    }
}