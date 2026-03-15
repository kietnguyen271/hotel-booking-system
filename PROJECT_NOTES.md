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
├── controller/     (AuthController, HotelController)
├── dto/            (RegisterRequest, LoginRequest, AuthResponse, HotelRequest, HotelResponse)
├── entity/         (User, Role, Hotel, RoomType, Room, RoomStatus)
├── repository/     (UserRepository, RoleRepository, HotelRepository, RoomTypeRepository)
├── security/       (JwtService, JwtFilter, UserDetailsServiceImpl)
└── service/        (AuthService, HotelService)

## ✅ Đã hoàn thành

### Phase 1 - Setup
- [x] Tạo Spring Boot project
- [x] Kết nối PostgreSQL
- [x] Cấu trúc thư mục

### Phase 2 - Authentication
- [x] Entity: User, Role
- [x] JWT Authentication
- [x] API: POST /api/auth/register
- [x] API: POST /api/auth/login
- [x] Phân quyền: ROLE_USER, ROLE_ADMIN, ROLE_HOTEL_OWNER
- [x] Data: INSERT roles vào DB

### Phase 3 - Hotel API
- [x] Entity: Hotel, RoomType, Room, RoomStatus
- [x] Hotel có field owner (HOTEL_OWNER)
- [x] API: GET /api/hotels (public)
- [x] API: GET /api/hotels/{id} (public)
- [x] API: GET /api/hotels/search?city=&starRating=&name= (public)
- [x] API: POST /api/hotel-owner/hotels (HOTEL_OWNER)
- [x] API: PUT /api/hotel-owner/hotels/{id} (chỉ owner)
- [x] API: DELETE /api/hotel-owner/hotels/{id} (chỉ owner)
- [x] API: DELETE /api/admin/hotels/{id} (ADMIN)

## 🔜 Đang làm - Phase 3 tiếp theo
- [ ] DTO: RoomTypeRequest, RoomTypeResponse, RoomRequest, RoomResponse
- [ ] Repository: RoomRepository (đã có RoomTypeRepository)
- [ ] Service: RoomService
- [ ] Controller: RoomController
- [ ] API: POST /api/hotel-owner/hotels/{hotelId}/room-types
- [ ] API: POST /api/hotel-owner/room-types/{roomTypeId}/rooms
- [ ] API: GET /api/hotels/{hotelId}/room-types (public)

## 📋 Còn lại

### Phase 4 - Booking API
- [ ] Entity: Booking, BookingDetail
- [ ] Logic check phòng còn trống
- [ ] API tạo booking
- [ ] API xem lịch sử booking
- [ ] API hủy booking

### Phase 5 - Review API
- [ ] Entity: Review
- [ ] API viết review
- [ ] API xem review theo hotel

### Phase 6 - Frontend React
- [ ] Setup React + Axios + React Router
- [ ] Trang Login/Register
- [ ] Trang danh sách hotel
- [ ] Trang chi tiết hotel
- [ ] Trang đặt phòng
- [ ] Trang lịch sử booking
- [ ] Admin Dashboard

### Phase 7 - Deploy
- [ ] Backend: Railway hoặc Render
- [ ] Frontend: Vercel
- [ ] Database: Supabase

## ⚠️ Lưu ý quan trọng
- Dùng nativeQuery = true trong HotelRepository vì bug lower() với PostgreSQL
- application.properties đã bị remove khỏi git (.gitignore)
- Role phải có prefix ROLE_ (ROLE_USER, ROLE_ADMIN, ROLE_HOTEL_OWNER)
- JDK dùng Oracle OpenJDK 21.0.6