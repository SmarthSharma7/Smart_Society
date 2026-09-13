package com.tiet.smartsocieties.repository;

import com.tiet.smartsocieties.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
