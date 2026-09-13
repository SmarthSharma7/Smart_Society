package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.dto.student.InterestUpdateRequest;
import com.tiet.smartsocieties.entity.Event;
import com.tiet.smartsocieties.entity.EventView;
import com.tiet.smartsocieties.entity.InterestTag;
import com.tiet.smartsocieties.entity.StudentInterest;
import com.tiet.smartsocieties.entity.StudentProfile;
import com.tiet.smartsocieties.entity.User;
import com.tiet.smartsocieties.repository.EventRepository;
import com.tiet.smartsocieties.repository.EventViewRepository;
import com.tiet.smartsocieties.repository.InterestTagRepository;
import com.tiet.smartsocieties.repository.StudentInterestRepository;
import com.tiet.smartsocieties.repository.StudentProfileRepository;
import com.tiet.smartsocieties.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final UserRepository users;
    private final StudentProfileRepository profiles;
    private final InterestTagRepository tags;
    private final StudentInterestRepository interests;
    private final EventRepository events;
    private final EventViewRepository views;

    private Long getCurrentUserId(
            Authentication authentication) {

        return users.findByEmail(
                authentication.getName()
        ).orElseThrow().getUserId();
    }

    // =========================
    // PROFILE
    // =========================

    @GetMapping("/profile")
    public StudentProfile getProfile(
            Authentication authentication) {

        return profiles.findById(
                getCurrentUserId(authentication)
        ).orElseThrow();
    }

    // =========================
    // INTEREST TAGS
    // =========================

    @GetMapping("/interests")
    public List<StudentInterest> getInterests(
            Authentication authentication) {

        return interests.findByIdStudentId(
                getCurrentUserId(authentication)
        );
    }

    @PutMapping("/interests")
    public List<StudentInterest> updateInterests(
            Authentication authentication,
            @RequestBody InterestUpdateRequest request) {

        Long studentId =
                getCurrentUserId(authentication);

        interests.deleteAll(
                interests.findByIdStudentId(studentId)
        );

        for (Long tagId : request.tagIds()) {

            InterestTag tag =
                    tags.findById(tagId).orElseThrow();

            StudentInterest interest =
                    new StudentInterest();

            interest.setId(
                    new StudentInterest.StudentInterestId(
                            studentId,
                            tagId
                    )
            );

            interest.setWeight(1.0);

            interests.save(interest);
        }

        return interests.findByIdStudentId(studentId);
    }

    // =========================
    // EVENT VIEW LOGGING
    // =========================

    @PostMapping("/events/{eventId}/view")
    public void logEventView(
            @PathVariable Long eventId,
            @RequestParam(
                    defaultValue = "feed"
            ) String source,
            Authentication authentication) {

        Event event =
                events.findById(eventId).orElseThrow();

        User student =
                users.findById(
                        getCurrentUserId(authentication)
                ).orElseThrow();

        EventView view = new EventView();

        view.setEvent(event);
        view.setStudent(student);
        view.setSource(source);

        views.save(view);
    }
}