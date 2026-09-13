package com.tiet.smartsocieties.controller;

import com.tiet.smartsocieties.entity.*;
import com.tiet.smartsocieties.repository.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    final UserRepository users;
    final StudentProfileRepository profiles;
    final InterestTagRepository tags;
    final StudentInterestRepository interests;
    final EventRepository events;
    final RegistrationRepository regs;
    final TimetableRepository timetable;
    final FeedbackRepository feedback;
    final EventViewRepository views;
    final JdbcTemplate jdbc;

    StudentController(UserRepository u, StudentProfileRepository p, InterestTagRepository t, StudentInterestRepository i, EventRepository e, RegistrationRepository r, TimetableRepository tt, FeedbackRepository f, EventViewRepository v, JdbcTemplate j) {
        users = u;
        profiles = p;
        tags = t;
        interests = i;
        events = e;
        regs = r;
        timetable = tt;
        feedback = f;
        views = v;
        jdbc = j;
    }

    Long me(Authentication a) {
        return users.findByEmail(a.getName()).orElseThrow().getUserId();
    }

    @GetMapping("/profile")
    StudentProfile profile(Authentication a) {
        return profiles.findById(me(a)).orElseThrow();
    }

    record InterestReq(List<Long> tagIds) {
    }

    @PutMapping("/interests")
    List<StudentInterest> setInterests(Authentication a, @RequestBody InterestReq r) {
        Long id = me(a);
        interests.deleteAll(interests.findByIdStudentId(id));
        for (Long tag : r.tagIds()) {
            StudentInterest x = new StudentInterest();
            x.setId(new StudentInterest.StudentInterestId(id, tag));
            x.setWeight(1.0);
            interests.save(x);
        }
        return interests.findByIdStudentId(id);
    }

    @PostMapping("/events/{eventId}/view")
    void view(@PathVariable Long eventId, @RequestParam(defaultValue = "feed") String source, Authentication a) {
        var x = new EventView();
        x.setEvent(events.findById(eventId).orElseThrow());
        x.setStudent(users.findById(me(a)).orElseThrow());
        x.setSource(source);
        views.save(x);
    }

    @GetMapping("/events/{eventId}/clash")
    Map<String, Object> clash(@PathVariable Long eventId, Authentication a) {
        var e = events.findById(eventId).orElseThrow();
        Long sid = me(a);
        boolean classClash = jdbc.queryForObject("select exists(select 1 from timetable t where t.student_id=? and upper(t.day::text)=upper(trim(to_char(?::timestamp,'DAY'))) and t.start_time < ?::time and t.end_time > ?::time)", Boolean.class, sid, e.getStartDatetime(), e.getEndDatetime().toLocalTime(), e.getStartDatetime().toLocalTime());
        boolean eventClash = jdbc.queryForObject("select exists(select 1 from event_registrations r join events x on x.event_id=r.event_id where r.student_id=? and r.status in ('INTERESTED','REGISTERED') and x.event_id<>? and x.start_datetime < ? and x.end_datetime > ?)", Boolean.class, sid, eventId, e.getEndDatetime(), e.getStartDatetime());
        return Map.of("classClash", classClash, "eventClash", eventClash, "hasClash", classClash || eventClash);
    }

    @PostMapping("/events/{eventId}/register")
    Registration register(@PathVariable Long eventId, @RequestParam(defaultValue = "INTERESTED") String status, Authentication a) {
        var e = events.findById(eventId).orElseThrow();
        Long sid = me(a);
        var existing = regs.findByEventEventIdAndStudentUserId(eventId, sid);
        var r = existing.orElseGet(Registration::new);
        r.setEvent(e);
        r.setStudent(users.findById(sid).orElseThrow());
        r.setStatus(Registration.RegistrationStatus.valueOf(status));
        r.setCancelledAt(null);
        return regs.save(r);
    }

    @GetMapping("/registrations")
    List<Registration> registrations(Authentication a) {
        return regs.findByStudentUserId(me(a));
    }

    record FeedbackReq(Integer rating, String comment) {
    }

    @PostMapping("/events/{eventId}/feedback")
    Feedback feedback(@PathVariable Long eventId, @RequestBody FeedbackReq req, Authentication a) {
        Long sid = me(a);
        var f = feedback.findByEventEventIdAndStudentUserId(eventId, sid).orElseGet(Feedback::new);
        f.setEvent(events.findById(eventId).orElseThrow());
        f.setStudent(users.findById(sid).orElseThrow());
        f.setRating(req.rating());
        f.setComment(req.comment());
        return feedback.save(f);
    }

    @PutMapping("/timetable")
    Timetable addClass(@RequestBody Timetable x, Authentication a) {
        x.setStudent(users.findById(me(a)).orElseThrow());
        return timetable.save(x);
    }

    @GetMapping("/timetable")
    List<Timetable> timetable(Authentication a) {
        return jdbc.query("select timetable_id,day,start_time,end_time,course_code,course_name,room from timetable where student_id=? order by day,start_time", (rs, n) -> {
            var x = new Timetable();
            x.setTimetableId(rs.getLong("timetable_id"));
            x.setDay(Timetable.Day.valueOf(rs.getString("day")));
            x.setStartTime(rs.getTime("start_time").toLocalTime());
            x.setEndTime(rs.getTime("end_time").toLocalTime());
            x.setCourseCode(rs.getString("course_code"));
            x.setCourseName(rs.getString("course_name"));
            x.setRoom(rs.getString("room"));
            x.setStudent(users.findById(me(a)).orElseThrow());
            return x;
        }, me(a));
    }

    @GetMapping("/recommendations")
    List<Map<String, Object>> recommendations(Authentication a) {
        Long sid = me(a);
        return jdbc.query("select e.event_id,e.title,e.start_datetime,e.venue,s.name society,c.name category, round((count(et.tag_id)::numeric/nullif((select count(*) from student_interests si where si.student_id=?),0)),4) score from events e join societies s on s.society_id=e.society_id join categories c on c.category_id=e.category_id left join event_tags et on et.event_id=e.event_id and et.tag_id in (select tag_id from student_interests where student_id=?) where e.status='APPROVED' and e.start_datetime>now() group by e.event_id,s.name,c.name order by score desc,e.start_datetime limit 10", (rs, n) -> Map.of("eventId", rs.getLong("event_id"), "title", rs.getString("title"), "startDatetime", rs.getTimestamp("start_datetime").toLocalDateTime(), "venue", rs.getString("venue"), "society", rs.getString("society"), "category", rs.getString("category"), "score", rs.getBigDecimal("score")), sid, sid);
    }
}
