package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.dto.auth.AuthResponse;
import com.tiet.smartsocieties.dto.auth.LoginRequest;
import com.tiet.smartsocieties.dto.auth.RegisterRequest;
import com.tiet.smartsocieties.entity.StudentProfile;
import com.tiet.smartsocieties.entity.Enums;
import com.tiet.smartsocieties.entity.User;
import com.tiet.smartsocieties.repository.StudentProfileRepository;
import com.tiet.smartsocieties.repository.UserRepository;
import com.tiet.smartsocieties.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final StudentProfileRepository profiles;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        if (users.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        user.setRole(Enums.UserRole.STUDENT);
        user.setActive(true);

        user = users.save(user);

        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setRollNumber(request.rollNumber());
        profile.setBranch(request.branch());
        profile.setYear(request.year());
        profile.setSection(request.section());

        profiles.save(profile);

        String token = jwtService.generate(
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        User user = users.findByEmail(request.email())
                .orElse(null);

        if (user == null ||
                !passwordEncoder.matches(
                        request.password(),
                        user.getPasswordHash())) {

            return ResponseEntity.status(401)
                    .body("Invalid credentials");
        }

        String token = jwtService.generate(
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name()
                )
        );
    }
}