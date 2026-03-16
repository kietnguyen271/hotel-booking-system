package com.example.hotel_booking.repository;

import com.example.hotel_booking.entity.Booking;
import com.example.hotel_booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Lịch sử booking của user
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Booking theo hotel (cho Hotel Owner xem)
    List<Booking> findByHotelIdOrderByCreatedAtDesc(Long hotelId);

    // Kiểm tra phòng có bị đặt trong khoảng thời gian không
    @Query(value = "SELECT COUNT(*) FROM booking_details bd " +
            "JOIN bookings b ON bd.booking_id = b.id " +
            "WHERE bd.room_id = :roomId " +
            "AND b.status NOT IN ('CANCELLED') " +
            "AND NOT (:checkOut <= b.check_in_date OR :checkIn >= b.check_out_date)",
            nativeQuery = true)
    Long countOverlappingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}