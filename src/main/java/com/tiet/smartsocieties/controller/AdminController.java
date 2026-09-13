package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.dto.event.EventRequest;
import com.tiet.smartsocieties.dto.event.EventResponse;
import com.tiet.smartsocieties.dto.society.SocietyRequest;
import com.tiet.smartsocieties.dto.society.SocietyResponse;
import com.tiet.smartsocieties.entity.Category;
import com.tiet.smartsocieties.entity.Event;
import com.tiet.smartsocieties.entity.Society;
import com.tiet.smartsocieties.repository.CategoryRepository;
import com.tiet.smartsocieties.service.EventService;
import com.tiet.smartsocieties.service.SocietyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SocietyService societyService;
    private final EventService eventService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/societies")
    public List<SocietyResponse> getSocieties() {
        return societyService.getAllSocieties()
                .stream()
                .map(this::toSocietyResponse)
                .toList();
    }

    @GetMapping("/societies/{id}")
    public SocietyResponse getSociety(@PathVariable Long id) {
        return toSocietyResponse(societyService.getSociety(id));
    }

    @PostMapping("/societies")
    public SocietyResponse createSociety(
            @Valid @RequestBody SocietyRequest request) {
        return toSocietyResponse(societyService.createSociety(request));
    }

    @PutMapping("/societies/{id}")
    public SocietyResponse updateSociety(
            @PathVariable Long id,
            @Valid @RequestBody SocietyRequest request) {
        return toSocietyResponse(
                societyService.updateSociety(id, request)
        );
    }

    @DeleteMapping("/societies/{id}")
    public ResponseEntity<String> deleteSociety(@PathVariable Long id) {
        societyService.deleteSociety(id);
        return ResponseEntity.ok("Society deleted successfully");
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/events")
    public List<EventResponse> getEvents() {
        return eventService.getAllEvents()
                .stream()
                .map(this::toEventResponse)
                .toList();
    }

    @GetMapping("/events/{id}")
    public EventResponse getEvent(@PathVariable Long id) {
        return toEventResponse(eventService.getEvent(id));
    }

    @PostMapping("/events")
    public EventResponse createEvent(
            @Valid @RequestBody EventRequest request) {
        return toEventResponse(eventService.createEvent(request));
    }

    @PutMapping("/events/{id}")
    public EventResponse updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        return toEventResponse(eventService.updateEvent(id, request));
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
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
