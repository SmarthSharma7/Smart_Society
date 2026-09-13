package com.tiet.smartsocieties.dto.society;

public record SocietyResponse(
        Long societyId,
        String name,
        String description,
        String contactEmail,
        String status
) {}
