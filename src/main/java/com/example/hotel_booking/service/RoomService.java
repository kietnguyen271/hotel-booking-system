package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.*;
import com.example.hotel_booking.entity.*;
import com.example.hotel_booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;

    // ── CONVERT ──

    private RoomTypeResponse toRoomTypeResponse(RoomType roomType) {
        RoomTypeResponse response = new RoomTypeResponse();
        response.setId(roomType.getId());
        response.setName(roomType.getName());
        response.setDescription(roomType.getDescription());
        response.setPricePerNight(roomType.getPricePerNight());
        response.setMaxGuests(roomType.getMaxGuests());
        response.setHotelId(roomType.getHotel().getId());
        response.setHotelName(roomType.getHotel().getName());
        return response;
    }

    private RoomResponse toRoomResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setFloor(room.getFloor());
        response.setStatus(room.getStatus().name());
        response.setRoomTypeId(room.getRoomType().getId());
        response.setRoomTypeName(room.getRoomType().getName());
        response.setHotelId(room.getHotel().getId());
        return response;
    }

    // ── ROOM TYPE ──

    // Xem loại phòng theo hotel (public)
    public List<RoomTypeResponse> getRoomTypesByHotel(Long hotelId) {
        return roomTypeRepository.findByHotelId(hotelId)
                .stream()
                .map(this::toRoomTypeResponse)
                .collect(Collectors.toList());
    }

    // Tạo loại phòng (Hotel Owner)
    public RoomTypeResponse createRoomType(Long hotelId,
                                           RoomTypeRequest request,
                                           User owner) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hotel: " + hotelId));

        // Kiểm tra có phải owner không
        if (!hotel.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Bạn không có quyền thêm phòng cho hotel này!");
        }

        RoomType roomType = new RoomType();
        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        roomType.setPricePerNight(request.getPricePerNight());
        roomType.setMaxGuests(request.getMaxGuests());
        roomType.setHotel(hotel);

        return toRoomTypeResponse(roomTypeRepository.save(roomType));
    }

    // ── ROOM ──

    // Xem phòng theo loại phòng (public)
    public List<RoomResponse> getRoomsByRoomType(Long roomTypeId) {
        return roomRepository.findByRoomTypeId(roomTypeId)
                .stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }

    // Tạo phòng cụ thể (Hotel Owner)
    public RoomResponse createRoom(Long roomTypeId,
                                   RoomRequest request,
                                   User owner) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy loại phòng: " + roomTypeId));

        Hotel hotel = roomType.getHotel();

        // Kiểm tra có phải owner không
        if (!hotel.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Bạn không có quyền thêm phòng!");
        }

        // Kiểm tra số phòng đã tồn tại chưa
        if (roomRepository.existsByHotelIdAndRoomNumber(
                hotel.getId(), request.getRoomNumber())) {
            throw new RuntimeException("Số phòng " + request.getRoomNumber()
                    + " đã tồn tại trong khách sạn này!");
        }

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setFloor(request.getFloor());
        room.setRoomType(roomType);
        room.setHotel(hotel);
        room.setStatus(RoomStatus.AVAILABLE);

        return toRoomResponse(roomRepository.save(room));
    }
    public List<RoomResponse> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelIdOrderByRoomNumber(hotelId)
                .stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }
}