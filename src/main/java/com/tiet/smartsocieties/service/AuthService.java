package com.tiet.smartsocieties.service;

import com.tiet.smartsocieties.dto.auth.AuthResponse;
import com.tiet.smartsocieties.dto.auth.LoginRequest;
import com.tiet.smartsocieties.dto.auth.RegisterRequest;
import com.tiet.smartsocieties.entity.Enums;
import com.tiet.smartsocieties.entity.StudentProfile;
import com.tiet.smartsocieties.entity.User;
import com.tiet.smartsocieties.repository.StudentProfileRepository;
import com.tiet.smartsocieties.repository.UserRepository;
import com.tiet.smartsocieties.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Enums.UserRole.STUDENT);
        user.setActive(true);

        user = userRepository.save(user);

        StudentProfile profile = new StudentProfile();

        profile.setUser(user);
        profile.setRollNumber(request.rollNumber());
        profile.setBranch(request.branch());
        profile.setYear(request.year());
        profile.setSection(request.section());

        studentProfileRepository.save(profile);

        String token = jwtService.generate(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash())) {

            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generate(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}