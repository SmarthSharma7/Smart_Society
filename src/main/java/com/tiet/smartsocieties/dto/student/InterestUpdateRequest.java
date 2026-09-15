package com.tiet.smartsocieties.dto.student;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record InterestUpdateRequest(
        List<Long> tagIds
) {}
