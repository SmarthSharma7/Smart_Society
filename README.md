# Smart Societies Backend

Spring Boot 3 + Java 21 + PostgreSQL REST backend for the Smart Societies & Student Engagement Platform.

## Prototype scope implemented
- Student registration/login with JWT authentication
- Student profile and interest-tag selection
- Admin-only society CRUD
- Admin-only event CRUD and approval/status management
- Public society directory and centralized searchable event feed
- Event view logging for discovery-rate measurement
- Mark event as interested/registered/cancelled
- Student timetable storage
- Event-vs-class and event-vs-interested-event clash detection
- Lightweight content-based recommendations using student/event tag overlap
- Post-event rating and optional comment
- Admin society analytics: average rating, feedback count, registrations, attendance

The software document explicitly calls for society profiles, centralized feed, student interest tags, feedback-based recommendations, timetable clash checks, analytics, feed-view logging, and a REST/3-tier architecture. This prototype focuses on those features and intentionally omits society-member management as requested.

## Existing database
This project expects the PostgreSQL `smart_society` schema you already created. Set:

DB_URL=jdbc:postgresql://localhost:5432/smart_society
DB_USERNAME=postgres
DB_PASSWORD=YOUR_PASSWORD
JWT_SECRET=use-a-long-random-secret-at-least-32-bytes

## Important schema update
Because societies now have a category:

```sql
ALTER TABLE societies ADD COLUMN IF NOT EXISTS category_id INT;
ALTER TABLE societies ADD CONSTRAINT fk_society_category FOREIGN KEY (category_id) REFERENCES categories(category_id);
```

Populate `category_id` for your societies before using the admin society API.

## Run
Install JDK 21 and Maven, then:

```bash
mvn spring-boot:run
```

API base URL: `http://localhost:8080/api`

## Main endpoints
### Auth
POST `/auth/register`
POST `/auth/login`

### Public
GET `/societies`
GET `/societies/{id}`
GET `/categories`
GET `/tags`
GET `/events/feed?q=robotics`

### Admin (JWT role ADMIN)
GET/POST/PUT/DELETE `/admin/societies`
GET/POST/PUT/DELETE `/admin/events`
GET `/admin/analytics/societies/{societyId}`

### Student (JWT authenticated)
GET `/student/profile`
PUT `/student/interests`
POST `/student/events/{eventId}/view`
GET `/student/events/{eventId}/clash`
POST `/student/events/{eventId}/register?status=INTERESTED`
GET `/student/registrations`
POST `/student/events/{eventId}/feedback`
PUT `/student/timetable`
GET `/student/timetable`
GET `/student/recommendations`

## Notes
- `student_profiles.student_id` is both PK and FK to `users.user_id`.
- Society members are intentionally not used; the admin owns society/event management in this prototype.
- The current recommendation query uses the `event_tags` table and student interests. Add event tags when creating events if you want richer recommendations.
