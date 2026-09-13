package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.dto.event.EventRequest;
import com.tiet.smartsocieties.dto.event.EventResponse;
import com.tiet.smartsocieties.dto.society.SocietyRequest;
import com.tiet.smartsocieties.dto.society.SocietyResponse;
import com.tiet.smartsocieties.entity.Category;
import com.tiet.smartsocieties.entity.Event;
import com.tiet.smartsocieties.entity.Society;
import com.tiet.smartsocieties.repository.CategoryRepository;
import com.tiet.smartsocieties.repository.EventRepository;
import com.tiet.smartsocieties.repository.SocietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SocietyRepository societies;
    private final EventRepository events;
    private final CategoryRepository categories;

    // =========================
    // SOCIETIES
    // =========================

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

        Society society = societies.findById(id)
                .orElseThrow();

        return toSocietyResponse(society);
    }

    @PostMapping("/societies")
    public SocietyResponse createSociety(
            @RequestBody SocietyRequest request) {

        Society society = new Society();

        society.setName(request.name());
        society.setDescription(request.description());
        society.setContactEmail(request.contactEmail());

        if (request.status() != null) {
            society.setStatus(
                    Society.SocietyStatus.valueOf(
                            request.status()
                    )
            );
        }

        return toSocietyResponse(
                societies.save(society)
        );
    }

    @PutMapping("/societies/{id}")
    public SocietyResponse updateSociety(
            @PathVariable Long id,
            @RequestBody SocietyRequest request) {

        Society society = societies.findById(id)
                .orElseThrow();

        society.setName(request.name());
        society.setDescription(request.description());
        society.setContactEmail(request.contactEmail());

        if (request.status() != null) {
            society.setStatus(
                    Society.SocietyStatus.valueOf(
                            request.status()
                    )
            );
        }

        return toSocietyResponse(
                societies.save(society)
        );
    }

    @DeleteMapping("/societies/{id}")
    public ResponseEntity<?> deleteSociety(
            @PathVariable Long id) {

        societies.deleteById(id);

        return ResponseEntity.ok(
                "Society deleted successfully"
        );
    }

    // =========================
    // CATEGORIES
    // =========================

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categories.findAll();
    }

    // =========================
    // EVENTS
    // =========================

    @GetMapping("/events")
    public List<EventResponse> getEvents() {

        return events.findAll()
                .stream()
                .map(this::toEventResponse)
                .toList();
    }

    @GetMapping("/events/{id}")
    public EventResponse getEvent(
            @PathVariable Long id) {

        Event event = events.findById(id)
                .orElseThrow();

        return toEventResponse(event);
    }

    @PostMapping("/events")
    public EventResponse createEvent(
            @RequestBody EventRequest request) {

        Society society = societies.findById(
                request.societyId()
        ).orElseThrow();

        Category category = categories.findById(
                request.categoryId()
        ).orElseThrow();

        Event event = new Event();

        event.setSociety(society);
        event.setCategory(category);
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setVenue(request.venue());
        event.setStartDatetime(request.startDatetime());
        event.setEndDatetime(request.endDatetime());
        event.setRegistrationDeadline(
                request.registrationDeadline()
        );
        event.setMaxCapacity(request.maxCapacity());

        if (request.status() != null) {
            event.setStatus(
                    Event.EventStatus.valueOf(
                            request.status()
                    )
            );
        } else {
            event.setStatus(
                    Event.EventStatus.APPROVED
            );
        }

        return toEventResponse(
                events.save(event)
        );
    }

    @PutMapping("/events/{id}")
    public EventResponse updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequest request) {

        Event event = events.findById(id)
                .orElseThrow();

        event.setSociety(
                societies.findById(
                        request.societyId()
                ).orElseThrow()
        );

        event.setCategory(
                categories.findById(
                        request.categoryId()
                ).orElseThrow()
        );

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setVenue(request.venue());
        event.setStartDatetime(request.startDatetime());
        event.setEndDatetime(request.endDatetime());
        event.setRegistrationDeadline(
                request.registrationDeadline()
        );
        event.setMaxCapacity(request.maxCapacity());

        if (request.status() != null) {
            event.setStatus(
                    Event.EventStatus.valueOf(
                            request.status()
                    )
            );
        }

        return toEventResponse(
                events.save(event)
        );
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> deleteEvent(
            @PathVariable Long id) {

        events.deleteById(id);

        return ResponseEntity.ok(
                "Event deleted successfully"
        );
    }

    // =========================
    // DTO MAPPERS
    // =========================

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