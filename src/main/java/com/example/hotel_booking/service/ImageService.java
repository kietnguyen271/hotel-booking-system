package com.example.hotel_booking.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.hotel_booking.dto.HotelImageResponse;
import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.entity.HotelImage;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.repository.HotelImageRepository;
import com.example.hotel_booking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;
    private final HotelRepository hotelRepository;
    private final HotelImageRepository hotelImageRepository;

    // Convert Entity → DTO
    private HotelImageResponse toResponse(HotelImage image) {
        HotelImageResponse response = new HotelImageResponse();
        response.setId(image.getId());
        response.setUrl(image.getUrl());
        response.setIsMain(image.getIsMain());
        response.setHotelId(image.getHotel().getId());
        return response;
    }

    // Upload ảnh lên Cloudinary
    public HotelImageResponse uploadImage(Long hotelId,
                                          MultipartFile file,
                                          Boolean isMain,
                                          User owner) throws IOException {
        // Kiểm tra hotel
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hotel!"));

        // Kiểm tra owner
        if (!hotel.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Bạn không có quyền upload ảnh!");
        }

        // Upload lên Cloudinary
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "hotel-booking/hotels/" + hotelId,
                        "resource_type", "image"
                )
        );

        // Lấy URL từ Cloudinary
        String url = (String) uploadResult.get("secure_url");

        // Nếu isMain = true → set tất cả ảnh cũ thành false
        if (Boolean.TRUE.equals(isMain)) {
            hotelImageRepository.findByHotelId(hotelId)
                    .forEach(img -> {
                        img.setIsMain(false);
                        hotelImageRepository.save(img);
                    });
        }

        // Lưu vào DB
        HotelImage image = new HotelImage();
        image.setHotel(hotel);
        image.setUrl(url);
        image.setIsMain(isMain != null ? isMain : false);

        return toResponse(hotelImageRepository.save(image));
    }

    // Xem ảnh của hotel
    public List<HotelImageResponse> getHotelImages(Long hotelId) {
        return hotelImageRepository.findByHotelId(hotelId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Xóa ảnh
    public void deleteImage(Long imageId, User owner) throws IOException {
        HotelImage image = hotelImageRepository.findById(imageId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy ảnh!"));

        if (!image.getHotel().getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa ảnh này!");
        }

        // Xóa trên Cloudinary
        String publicId = extractPublicId(image.getUrl());
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        // Xóa trong DB
        hotelImageRepository.delete(image);
    }

    // Lấy public_id từ URL Cloudinary
    private String extractPublicId(String url) {
        String[] parts = url.split("/");
        String filename = parts[parts.length - 1];
        return "hotel-booking/hotels/" +
                parts[parts.length - 2] + "/" +
                filename.split("\\.")[0];
    }
}