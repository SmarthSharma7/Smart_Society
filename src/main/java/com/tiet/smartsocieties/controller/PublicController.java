package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.dto.event.EventResponse;
import com.tiet.smartsocieties.dto.society.SocietyResponse;
import com.tiet.smartsocieties.entity.Category;
import com.tiet.smartsocieties.entity.Event;
import com.tiet.smartsocieties.entity.InterestTag;
import com.tiet.smartsocieties.entity.Society;
import com.tiet.smartsocieties.repository.CategoryRepository;
import com.tiet.smartsocieties.repository.InterestTagRepository;
import com.tiet.smartsocieties.service.EventService;
import com.tiet.smartsocieties.service.SocietyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PublicController {

    private final SocietyService societyService;
    private final EventService eventService;
    private final CategoryRepository categoryRepository;
    private final InterestTagRepository interestTagRepository;

    @GetMapping("/societies")
    public List<SocietyResponse> getSocieties(
            @RequestParam(required = false) String q) {

        return societyService.searchSocieties(q)
                .stream()
                .map(this::toSocietyResponse)
                .toList();
    }

    @GetMapping("/societies/{id}")
    public SocietyResponse getSociety(@PathVariable Long id) {
        return toSocietyResponse(societyService.getSociety(id));
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/tags")
    public List<InterestTag> getTags() {
        return interestTagRepository.findAll();
    }

    @GetMapping("/events/feed")
    public List<EventResponse> getFeed(
            @RequestParam(required = false) String q) {

        return eventService.searchEvents(q)
                .stream()
                .map(this::toEventResponse)
                .toList();
    }

    private SocietyResponse toSocietyResponse(Society society) {
        return new SocietyResponse(
                society.getSocietyId(),
                society.getName(),
                society.getDescription(),
                society.getContactEmail(),
                society.getStatus().name()
        );
    }

    private EventResponse toEventResponse(Event event) {
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
