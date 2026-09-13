package com.tiet.smartsocieties.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/analytics")
public class AnalyticsController {
    final JdbcTemplate db;

    AnalyticsController(JdbcTemplate d) {
        db = d;
    }

    @GetMapping("/societies/{id}")
    Map<String, Object> society(@PathVariable Long id) {
        var m = new HashMap<String, Object>();
        m.put("averageRating", db.queryForObject("select coalesce(round(avg(f.rating),2),0) from feedback f join events e on e.event_id=f.event_id where e.society_id=?", Double.class, id));
        m.put("feedbackCount", db.queryForObject("select count(*) from feedback f join events e on e.event_id=f.event_id where e.society_id=?", Long.class, id));
        m.put("registrations", db.queryForObject("select count(*) from event_registrations r join events e on e.event_id=r.event_id where e.society_id=? and r.status in ('INTERESTED','REGISTERED','ATTENDED')", Long.class, id));
        m.put("attendance", db.queryForObject("select count(*) from event_registrations r join events e on e.event_id=r.event_id where e.society_id=? and r.status='ATTENDED'", Long.class, id));
        return m;
    }
}
