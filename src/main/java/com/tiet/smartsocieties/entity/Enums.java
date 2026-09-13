package com.tiet.smartsocieties.entity;

public final class Enums {
    private Enums() {
    }

    public enum UserRole {STUDENT, SOCIETY_MEMBER, ADMIN}

    public enum SocietyStatus {ACTIVE, INACTIVE}

    public enum EventStatus {DRAFT, PENDING, APPROVED, REJECTED, CANCELLED, COMPLETED}

    public enum RegistrationStatus {INTERESTED, REGISTERED, CANCELLED, ATTENDED, NO_SHOW}

    public enum DayOfWeek {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY}

    public enum NotificationType {NEW_EVENT, EVENT_REMINDER, FEEDBACK_REMINDER, RECOMMENDATION, SYSTEM}

    public enum NotificationStatus {UNREAD, READ}
}
