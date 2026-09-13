package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.entity.*;
import com.tiet.smartsocieties.repository.*;
import com.tiet.smartsocieties.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    record Register(@NotBlank String name, @Email String email, @NotBlank String password, @NotBlank String rollNumber,
                    String branch, Integer year, String section) {
    }

    record Login(@Email String email, @NotBlank String password) {
    }

    private final UserRepository users;
    private final StudentProfileRepository profiles;
    private final PasswordEncoder enc;
    private final JwtService jwt;

    public AuthController(UserRepository u, StudentProfileRepository p, PasswordEncoder e, JwtService j) {
        users = u;
        profiles = p;
        enc = e;
        jwt = j;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Register r) {
        if (users.findByEmail(r.email()).isPresent())
            return ResponseEntity.badRequest().body("Email already registered");
        User u = new User();
        u.setName(r.name());
        u.setEmail(r.email());
        u.setPasswordHash(enc.encode(r.password()));
        u.setRole(Enums.UserRole.STUDENT);
        users.save(u);
        StudentProfile p = new StudentProfile();
        p.setUser(u);
        p.setRollNumber(r.rollNumber());
        p.setBranch(r.branch());
        p.setYear(r.year());
        p.setSection(r.section());
        profiles.save(p);
        return ResponseEntity.ok(java.util.Map.of("token", jwt.generate(u.getEmail(), u.getRole().name()), "userId", u.getUserId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login r) {
        var u = users.findByEmail(r.email()).orElse(null);
        if (u == null || !enc.matches(r.password(), u.getPasswordHash()))
            return ResponseEntity.status(401).body("Invalid credentials");
        return ResponseEntity.ok(java.util.Map.of("token", jwt.generate(u.getEmail(), u.getRole().name()), "userId", u.getUserId(), "role", u.getRole()));
    }
}
