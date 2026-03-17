# Hotel Booking System - Project Notes

## 🛠️ Tech Stack
- Backend: Spring Boot 3.5.11 (Java 21)
- Frontend: React (JavaScript)
- Database: PostgreSQL 17
- IDE: IntelliJ IDEA (BE), VS Code (FE)
- Auth: JWT (jjwt 0.11.5)

## 📁 Project Location
- Backend: D:\hotel-booking
- GitHub: https://github.com/kietnguyen271/hotel-booking-system

## 🗄️ Database
- DB Name: hotel_booking
- Username: postgres
- Port: 5432

## 📦 Package Structure
com.example.hotel_booking
├── config/         (SecurityConfig)
├── controller/     (AuthController, HotelController, RoomController, BookingController)
├── dto/            (RegisterRequest, LoginRequest, AuthResponse,
│                    HotelRequest, HotelResponse,
│                    RoomTypeRequest, RoomTypeResponse,
│                    RoomRequest, RoomResponse,
│                    BookingRequest, BookingResponse, BookingDetailResponse)
├── entity/         (User, Role, Hotel, RoomType, Room, RoomStatus,
│                    Booking, BookingDetail, BookingStatus)
├── repository/     (UserRepository, RoleRepository, HotelRepository,
│                    RoomTypeRepository, RoomRepository, BookingRepository)
├── security/       (JwtService, JwtFilter, UserDetailsServiceImpl)
└── service/        (AuthService, HotelService, RoomService, BookingService)

## ✅ Đã hoàn thành

### Phase 1 - Setup
- [x] Tạo Spring Boot project (v3.5.11)
- [x] Kết nối PostgreSQL
- [x] Cấu trúc thư mục
- [x] Push lên GitHub

### Phase 2 - Authentication
- [x] Entity: User, Role
- [x] JWT Authentication (jjwt 0.11.5)
- [x] API: POST /api/auth/register
- [x] API: POST /api/auth/login
- [x] Phân quyền: ROLE_USER, ROLE_ADMIN, ROLE_HOTEL_OWNER
- [x] Data: INSERT roles vào DB

### Phase 3 - Hotel API
- [x] Entity: Hotel (có field owner → HOTEL_OWNER)
- [x] API: GET /api/hotels (public)
- [x] API: GET /api/hotels/{id} (public)
- [x] API: GET /api/hotels/search?city=&starRating=&name= (public)
- [x] API: POST /api/hotel-owner/hotels (HOTEL_OWNER)
- [x] API: PUT /api/hotel-owner/hotels/{id} (chỉ owner)
- [x] API: DELETE /api/hotel-owner/hotels/{id} (chỉ owner)
- [x] API: DELETE /api/admin/hotels/{id} (ADMIN)

### Phase 4 - Room API
- [x] Entity: RoomType, Room, RoomStatus enum
- [x] API: GET /api/hotels/{id}/room-types (public)
- [x] API: GET /api/hotels/{id}/rooms (public, sort by roomNumber)
- [x] API: GET /api/room-types/{id}/rooms (public)
- [x] API: POST /api/hotel-owner/hotels/{id}/room-types (HOTEL_OWNER)
- [x] API: POST /api/hotel-owner/room-types/{id}/rooms (HOTEL_OWNER)

### Phase 5 - Booking API
- [x] Entity: Booking, BookingDetail, BookingStatus enum
- [x] Logic check phòng còn trống theo ngày
- [x] Tự động chọn phòng available
- [x] Tính tiền theo số đêm
- [x] API: POST /api/bookings (USER)
- [x] API: GET /api/bookings/my (USER)
- [x] API: PUT /api/bookings/{id}/cancel (USER)
- [x] API: GET /api/hotel-owner/hotels/{id}/bookings (HOTEL_OWNER)

## 🔜 Đang làm - Tiếp theo

### Phase 6 - Upload ảnh
- [ ] Tích hợp Cloudinary
- [ ] Entity: HotelImage
- [ ] API: POST /api/hotel-owner/hotels/{id}/images
- [ ] API: GET /api/hotels/{id}/images

### Phase 7 - Review API
- [ ] Entity: Review
- [ ] API: POST /api/reviews (USER)
- [ ] API: GET /api/hotels/{id}/reviews (public)
- [ ] API: DELETE /api/admin/reviews/{id} (ADMIN)

## 📋 Còn lại

### Phase 8 - Frontend React
- [ ] Setup React + Axios + React Router
- [ ] Trang Login/Register
- [ ] Trang danh sách hotel
- [ ] Trang chi tiết hotel + phòng
- [ ] Trang đặt phòng
- [ ] Trang lịch sử booking
- [ ] Admin Dashboard
- [ ] Hotel Owner Dashboard

### Phase 9 - Deploy
- [ ] Backend: Railway hoặc Render (free)
- [ ] Frontend: Vercel (free)
- [ ] Database: Supabase (free PostgreSQL)

## ⚠️ Lưu ý quan trọng
- Dùng nativeQuery = true trong HotelRepository (bug lower() với PostgreSQL)
- application.properties đã remove khỏi git (.gitignore)
- Role phải có prefix ROLE_ (ROLE_USER, ROLE_ADMIN, ROLE_HOTEL_OWNER)
- JDK dùng Oracle OpenJDK 21.0.6
- SecurityConfig: permitAll() phải đặt TRƯỚC authenticated()
- List.of() = immutable → dùng new ArrayList<>() thay thế
- Postman Environment: hotel-booking-local
    - {{user_token}}, {{owner_token}}, {{admin_token}}