package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.entity.*;
import com.tiet.smartsocieties.repository.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final SocietyRepository societies;
    private final EventRepository events;
    private final CategoryRepository cats;

    AdminController(SocietyRepository s, EventRepository e, CategoryRepository c) {
        societies = s;
        events = e;
        cats = c;
    }

    record SocietyReq(String name, String description, String contactEmail, Long categoryId, String status) {
    }

    record EventReq(Long societyId, Long categoryId, String title, String description, String venue,
                    String startDatetime, String endDatetime, String registrationDeadline, Integer maxCapacity,
                    String status) {
    }

    @GetMapping("/societies")
    public List<Society> societies() {
        return societies.findAll();
    }

    @PostMapping("/societies")
    public Society addSociety(@RequestBody SocietyReq r) {
        var s = new Society();
        s.setName(r.name());
        s.setDescription(r.description());
        s.setContactEmail(r.contactEmail());
        s.setCategory(cats.findById(r.categoryId()).orElseThrow());
        if (r.status() != null) s.setStatus(Society.SocietyStatus.valueOf(r.status()));
        return societies.save(s);
    }

    @PutMapping("/societies/{id}")
    public Society updateSociety(@PathVariable Long id, @RequestBody SocietyReq r) {
        var s = societies.findById(id).orElseThrow();
        s.setName(r.name());
        s.setDescription(r.description());
        s.setContactEmail(r.contactEmail());
        s.setCategory(cats.findById(r.categoryId()).orElseThrow());
        if (r.status() != null) s.setStatus(Society.SocietyStatus.valueOf(r.status()));
        return societies.save(s);
    }

    @DeleteMapping("/societies/{id}")
    public void deleteSociety(@PathVariable Long id) {
        societies.deleteById(id);
    }

    @PostMapping("/events")
    public Event addEvent(@RequestBody EventReq r) {
        var e = new Event();
        e.setSociety(societies.findById(r.societyId()).orElseThrow());
        e.setCategory(cats.findById(r.categoryId()).orElseThrow());
        e.setTitle(r.title());
        e.setDescription(r.description());
        e.setVenue(r.venue());
        e.setStartDatetime(LocalDateTime.parse(r.startDatetime()));
        e.setEndDatetime(LocalDateTime.parse(r.endDatetime()));
        if (r.registrationDeadline() != null) e.setRegistrationDeadline(LocalDateTime.parse(r.registrationDeadline()));
        e.setMaxCapacity(r.maxCapacity());
        e.setStatus(r.status() == null ? Event.EventStatus.APPROVED : Event.EventStatus.valueOf(r.status()));
        return events.save(e);
    }

    @PutMapping("/events/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody EventReq r) {
        var e = events.findById(id).orElseThrow();
        e.setSociety(societies.findById(r.societyId()).orElseThrow());
        e.setCategory(cats.findById(r.categoryId()).orElseThrow());
        e.setTitle(r.title());
        e.setDescription(r.description());
        e.setVenue(r.venue());
        e.setStartDatetime(LocalDateTime.parse(r.startDatetime()));
        e.setEndDatetime(LocalDateTime.parse(r.endDatetime()));
        e.setMaxCapacity(r.maxCapacity());
        if (r.status() != null) e.setStatus(Event.EventStatus.valueOf(r.status()));
        return events.save(e);
    }

    @DeleteMapping("/events/{id}")
    public void deleteEvent(@PathVariable Long id) {
        events.deleteById(id);
    }

    @GetMapping("/events")
    public List<Event> events() {
        return events.findAll();
    }
}
