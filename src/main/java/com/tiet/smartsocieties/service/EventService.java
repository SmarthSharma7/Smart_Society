package com.tiet.smartsocieties.service;

import com.tiet.smartsocieties.dto.event.EventRequest;
import com.tiet.smartsocieties.entity.Category;
import com.tiet.smartsocieties.entity.Event;
import com.tiet.smartsocieties.entity.EventView;
import com.tiet.smartsocieties.entity.Society;
import com.tiet.smartsocieties.entity.User;
import com.tiet.smartsocieties.repository.CategoryRepository;
import com.tiet.smartsocieties.repository.EventRepository;
import com.tiet.smartsocieties.repository.EventViewRepository;
import com.tiet.smartsocieties.repository.SocietyRepository;
import com.tiet.smartsocieties.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SocietyRepository societyRepository;
    private final CategoryRepository categoryRepository;
    private final EventViewRepository eventViewRepository;
    private final UserRepository userRepository;

    public List<Event> getFeed() {

        return eventRepository
                .findByStatusAndStartDatetimeAfterOrderByStartDatetimeAsc(
                        Event.EventStatus.APPROVED,
                        LocalDateTime.now()
                );
    }

    public List<Event> searchEvents(String title) {

        if (title == null || title.isBlank()) {
            return getFeed();
        }

        return eventRepository
                .findByStatusAndTitleContainingIgnoreCaseOrderByStartDatetimeAsc(
                        Event.EventStatus.APPROVED,
                        title
                );
    }

    public Event getEvent(Long eventId) {

        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Event not found"));
    }

    public List<Event> getEventsBySociety(Long societyId) {

        return eventRepository
                .findBySocietySocietyIdOrderByStartDatetimeDesc(
                        societyId
                );
    }

    @Transactional
    public Event createEvent(EventRequest request) {

        Society society = societyRepository.findById(
                        request.societyId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Society not found"));

        Category category = categoryRepository.findById(
                        request.categoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found"));

        validateDates(request);

        Event event = new Event();

        event.setSociety(society);
        event.setCategory(category);
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setVenue(request.venue());
        event.setStartDatetime(request.startDatetime());
        event.setEndDatetime(request.endDatetime());
        event.setRegistrationDeadline(
                request.registrationDeadline());
        event.setMaxCapacity(request.maxCapacity());

        if (request.status() != null &&
                !request.status().isBlank()) {

            event.setStatus(
                    Event.EventStatus.valueOf(
                            request.status().toUpperCase()
                    )
            );

        } else {
            event.setStatus(Event.EventStatus.DRAFT);
        }

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(
            Long eventId,
            EventRequest request) {

        Event event = getEvent(eventId);

        Society society = societyRepository.findById(
                        request.societyId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Society not found"));

        Category category = categoryRepository.findById(
                        request.categoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Category not found"));

        validateDates(request);

        event.setSociety(society);
        event.setCategory(category);
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setVenue(request.venue());
        event.setStartDatetime(request.startDatetime());
        event.setEndDatetime(request.endDatetime());
        event.setRegistrationDeadline(
                request.registrationDeadline());
        event.setMaxCapacity(request.maxCapacity());

        if (request.status() != null &&
                !request.status().isBlank()) {

            event.setStatus(
                    Event.EventStatus.valueOf(
                            request.status().toUpperCase()
                    )
            );
        }

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) {

        Event event = getEvent(eventId);

        eventRepository.delete(event);
    }

    @Transactional
    public void logEventView(
            Long studentId,
            Long eventId,
            String source) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Student not found"));

        Event event = getEvent(eventId);

        EventView eventView = new EventView();

        eventView.setStudent(student);
        eventView.setEvent(event);
        eventView.setViewedAt(LocalDateTime.now());
        eventView.setSource(source);

        eventViewRepository.save(eventView);
    }

    private void validateDates(EventRequest request) {

        if (request.endDatetime()
                .isBefore(request.startDatetime())) {

            throw new IllegalArgumentException(
                    "Event end time cannot be before start time");
        }

        if (request.registrationDeadline() != null &&
                request.registrationDeadline()
                        .isAfter(request.startDatetime())) {

            throw new IllegalArgumentException(
                    "Registration deadline cannot be after event start");
        }
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

}