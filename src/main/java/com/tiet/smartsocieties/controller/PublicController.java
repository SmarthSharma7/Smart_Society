package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.entity.*;
import com.tiet.smartsocieties.repository.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PublicController {
    final SocietyRepository societies;
    final EventRepository events;
    final CategoryRepository cats;
    final InterestTagRepository tags;
    final EventViewRepository views;

    PublicController(SocietyRepository s, EventRepository e, CategoryRepository c, InterestTagRepository t, EventViewRepository v) {
        societies = s;
        events = e;
        cats = c;
        tags = t;
        views = v;
    }

    @GetMapping("/societies")
    List<Society> societies() {
        return societies.findAll();
    }

    @GetMapping("/societies/{id}")
    Society society(@PathVariable Long id) {
        return societies.findById(id).orElseThrow();
    }

    @GetMapping("/categories")
    List<Category> categories() {
        return cats.findAll();
    }

    @GetMapping("/tags")
    List<InterestTag> tags() {
        return tags.findAll();
    }

    @GetMapping("/events/feed")
    List<Event> feed(@RequestParam(required = false) String q) {
        if (q == null || q.isBlank())
            return events.findByStatusAndStartDatetimeAfterOrderByStartDatetimeAsc(Event.EventStatus.APPROVED, LocalDateTime.now());
        return events.findByStatusAndTitleContainingIgnoreCaseOrderByStartDatetimeAsc(Event.EventStatus.APPROVED, q);
    }
}
