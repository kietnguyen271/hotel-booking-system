package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.*;
import com.example.hotel_booking.entity.*;
import com.example.hotel_booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;

    // ── CONVERT ──

    private BookingDetailResponse toDetailResponse(BookingDetail detail) {
        BookingDetailResponse response = new BookingDetailResponse();
        response.setId(detail.getId());
        response.setRoomNumber(detail.getRoom().getRoomNumber());
        response.setRoomTypeName(detail.getRoom().getRoomType().getName());
        response.setFloor(detail.getRoom().getFloor());
        response.setPrice(detail.getPrice());
        return response;
    }

    private BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setHotelName(booking.getHotel().getName());
        response.setHotelAddress(booking.getHotel().getAddress());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus().name());
        response.setCreatedAt(booking.getCreatedAt());
        response.setBookingDetails(
                booking.getBookingDetails()
                        .stream()
                        .map(this::toDetailResponse)
                        .collect(Collectors.toList())
        );
        return response;
    }

    // ── BOOKING ──

    @Transactional
    public BookingResponse createBooking(BookingRequest request, User user) {

        // 1. Validate ngày
        if (!request.getCheckInDate().isBefore(request.getCheckOutDate())) {
            throw new RuntimeException("Ngày check-in phải trước check-out!");
        }

        // 2. Lấy hotel và roomType
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hotel!"));

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng!"));

        // 3. Tìm phòng còn trống
        Room availableRoom = roomRepository
                .findByRoomTypeId(request.getRoomTypeId())
                .stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .filter(room -> bookingRepository.countOverlappingBookings(
                        room.getId(),
                        request.getCheckInDate(),
                        request.getCheckOutDate()) == 0)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Không còn phòng trống trong thời gian này!"));

        // 4. Tính số đêm và tổng tiền
        long nights = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate());
        BigDecimal totalPrice = roomType.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights));

        // 5. Tạo Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        // 6. Tạo BookingDetail
        BookingDetail detail = new BookingDetail();
        detail.setBooking(savedBooking);
        detail.setRoom(availableRoom);
        detail.setPrice(roomType.getPricePerNight());

        savedBooking.setBookingDetails(new ArrayList<>(List.of(detail)));

        bookingRepository.save(savedBooking);

        return toResponse(savedBooking);
    }

    // Xem lịch sử booking của user
    public List<BookingResponse> getMyBookings(User user) {
        return bookingRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Hủy booking
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy booking!"));

        // Chỉ user tạo booking mới được hủy
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền hủy booking này!");
        }

        // Chỉ hủy được booking PENDING hoặc CONFIRMED
        if (booking.getStatus() == BookingStatus.COMPLETED ||
                booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Không thể hủy booking này!");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return toResponse(bookingRepository.save(booking));
    }

    // Hotel Owner xem booking của khách sạn mình
    public List<BookingResponse> getHotelBookings(Long hotelId, User owner) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hotel!"));

        if (!hotel.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Bạn không có quyền xem booking này!");
        }

        return bookingRepository
                .findByHotelIdOrderByCreatedAtDesc(hotelId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
