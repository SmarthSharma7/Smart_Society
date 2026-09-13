package com.tiet.smartsocieties.dto.society;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SocietyRequest(
        @NotBlank String name,
        String description,
        @Email String contactEmail,
        String status
) {}
