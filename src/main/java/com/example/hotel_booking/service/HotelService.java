package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.HotelRequest;
import com.example.hotel_booking.dto.HotelResponse;
import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.repository.HotelRepository;
import com.example.hotel_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    // ── CONVERT ──

    private HotelResponse toResponse(Hotel hotel) {
        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setAddress(hotel.getAddress());
        response.setCity(hotel.getCity());
        response.setDescription(hotel.getDescription());
        response.setStarRating(hotel.getStarRating());
        return response;
    }

    private Hotel toEntity(HotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setDescription(request.getDescription());
        hotel.setStarRating(request.getStarRating());
        return hotel;
    }

    // ── PUBLIC ──

    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hotel: " + id));
        return toResponse(hotel);
    }

    public List<HotelResponse> searchHotels(String city,
                                            Integer starRating,
                                            String name) {
        return hotelRepository.searchHotels(city, starRating, name)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── HOTEL OWNER ──

    public HotelResponse createHotel(HotelRequest request, User owner) {
        Hotel hotel = toEntity(request);
        hotel.setOwner(owner);
        return toResponse(hotelRepository.save(hotel));
    }

    public HotelResponse updateHotel(Long id,
                                     HotelRequest request,
                                     User owner) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hotel: " + id));

        // Chỉ owner mới được sửa
        if (!hotel.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Bạn không có quyền sửa hotel này!");
        }

        hotel.setName(request.getName());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setDescription(request.getDescription());
        hotel.setStarRating(request.getStarRating());
        return toResponse(hotelRepository.save(hotel));
    }

    public void deleteHotel(Long id, User owner) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hotel: " + id));

        // Chỉ owner mới được xóa
        if (!hotel.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa hotel này!");
        }

        hotelRepository.delete(hotel);
    }

    // ── ADMIN ──

    public void adminDeleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hotel: " + id));
        hotelRepository.delete(hotel);
    }
}