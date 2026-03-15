package com.example.hotel_booking.repository;

import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

    List<Room> findByRoomTypeId(Long roomTypeId);

    List<Room> findByHotelIdAndStatus(Long hotelId, RoomStatus status);

    boolean existsByHotelIdAndRoomNumber(Long hotelId, String roomNumber);
}