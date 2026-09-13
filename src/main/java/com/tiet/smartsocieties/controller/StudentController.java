package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.dto.student.InterestUpdateRequest;
import com.tiet.smartsocieties.dto.student.StudentProfileResponse;
import com.tiet.smartsocieties.entity.StudentInterest;
import com.tiet.smartsocieties.entity.StudentProfile;
import com.tiet.smartsocieties.entity.User;
import com.tiet.smartsocieties.repository.UserRepository;
import com.tiet.smartsocieties.service.EventService;
import com.tiet.smartsocieties.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final UserRepository userRepository;
    private final StudentService studentService;
    private final EventService eventService;

    private Long getCurrentUserId(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getUserId();
    }

    @GetMapping("/profile")
    public StudentProfileResponse getProfile(Authentication authentication) {

        Long studentId = Long.valueOf(authentication.getName());

        StudentProfile profile = studentService.getProfile(studentId);

        return new StudentProfileResponse(
                profile.getStudentId(),
                profile.getRollNumber(),
                profile.getBranch(),
                profile.getYear(),
                profile.getSection(),
                profile.getProfilePicture()
        );
    }

    @GetMapping("/interests")
    public List<StudentInterest> getInterests(
            Authentication authentication) {
        return studentService.getInterests(getCurrentUserId(authentication));
    }

    @PutMapping("/interests")
    public List<StudentInterest> updateInterests(
            Authentication authentication,
            @Valid @RequestBody InterestUpdateRequest request) {

        Long studentId = getCurrentUserId(authentication);
        studentService.updateInterests(studentId, request);
        return studentService.getInterests(studentId);
    }

    @PostMapping("/events/{eventId}/view")
    public void logEventView(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "feed") String source,
            Authentication authentication) {

        eventService.logEventView(
                getCurrentUserId(authentication),
                eventId,
                source
        );
    }
}
