package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.dto.event.EventResponse;
import com.tiet.smartsocieties.dto.society.SocietyResponse;
import com.tiet.smartsocieties.entity.Category;
import com.tiet.smartsocieties.entity.InterestTag;
import com.tiet.smartsocieties.entity.Event;
import com.tiet.smartsocieties.entity.Society;
import com.tiet.smartsocieties.repository.CategoryRepository;
import com.tiet.smartsocieties.repository.EventRepository;
import com.tiet.smartsocieties.repository.InterestTagRepository;
import com.tiet.smartsocieties.repository.SocietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PublicController {

    private final SocietyRepository societies;
    private final EventRepository events;
    private final CategoryRepository categories;
    private final InterestTagRepository tags;

    @GetMapping("/societies")
    public List<SocietyResponse> getSocieties() {

        return societies.findAll()
                .stream()
                .map(this::toSocietyResponse)
                .toList();
    }

    @GetMapping("/societies/{id}")
    public SocietyResponse getSociety(
            @PathVariable Long id) {

        return societies.findById(id)
                .map(this::toSocietyResponse)
                .orElseThrow();
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categories.findAll();
    }

    @GetMapping("/tags")
    public List<InterestTag> getTags() {
        return tags.findAll();
    }

    @GetMapping("/events/feed")
    public List<EventResponse> getFeed(
            @RequestParam(required = false) String q) {

        List<Event> result;

        if (q == null || q.isBlank()) {

            result = events
                    .findByStatusAndStartDatetimeAfterOrderByStartDatetimeAsc(
                            Event.EventStatus.APPROVED,
                            LocalDateTime.now()
                    );

        } else {

            result = events
                    .findByStatusAndTitleContainingIgnoreCaseOrderByStartDatetimeAsc(
                            Event.EventStatus.APPROVED,
                            q
                    );
        }

        return result.stream()
                .map(this::toEventResponse)
                .toList();
    }

    private SocietyResponse toSocietyResponse(
            Society society) {

        return new SocietyResponse(
                society.getSocietyId(),
                society.getName(),
                society.getDescription(),
                society.getContactEmail(),
                society.getStatus().name()
        );
    }

    private EventResponse toEventResponse(
            Event event) {

        return new EventResponse(
                event.getEventId(),
                event.getSociety().getSocietyId(),
                event.getSociety().getName(),
                event.getCategory().getCategoryId(),
                event.getCategory().getName(),
                event.getTitle(),
                event.getDescription(),
                event.getVenue(),
                event.getStartDatetime(),
                event.getEndDatetime(),
                event.getRegistrationDeadline(),
                event.getMaxCapacity(),
                event.getStatus().name()
        );
    }
}