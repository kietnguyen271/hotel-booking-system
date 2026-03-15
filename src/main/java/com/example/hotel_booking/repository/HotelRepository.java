package com.example.hotel_booking.repository;

import com.example.hotel_booking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query(value = "SELECT * FROM hotels h WHERE " +
            "(:city IS NULL OR LOWER(h.city) = LOWER(CAST(:city AS VARCHAR))) AND " +
            "(:starRating IS NULL OR h.star_rating = :starRating) AND " +
            "(:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', CAST(:name AS VARCHAR), '%')))",
            nativeQuery = true)
    List<Hotel> searchHotels(
            @Param("city") String city,
            @Param("starRating") Integer starRating,
            @Param("name") String name
    );
}