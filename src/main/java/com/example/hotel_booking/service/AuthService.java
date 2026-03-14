package com.example.hotel_booking.service;

import com.example.hotel_booking.dto.*;
import com.example.hotel_booking.entity.*;
import com.example.hotel_booking.repository.*;
import com.example.hotel_booking.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // ── ĐĂNG KÝ ──
    public AuthResponse register(RegisterRequest request) {

        // 1. Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // 2. Lấy role USER từ DB
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));

        // 3. Tạo user mới
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // mã hóa password
        user.setPhone(request.getPhone());
        user.setRole(role);

        // 4. Lưu vào DB
        userRepository.save(user);

        // 5. Tạo JWT token
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, user.getEmail(),
                user.getFullName(), role.getName());
    }

    // ── ĐĂNG NHẬP ──
    public AuthResponse login(LoginRequest request) {

        // 1. Kiểm tra email + password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Lấy user từ DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        // 3. Tạo JWT token
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, user.getEmail(),
                user.getFullName(), user.getRole().getName());
    }
}
