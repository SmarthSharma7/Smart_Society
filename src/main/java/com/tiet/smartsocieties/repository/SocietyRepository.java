package com.tiet.smartsocieties.repository;

import com.tiet.smartsocieties.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocietyRepository extends JpaRepository<Society, Long> {
    List<Society> findByNameContainingIgnoreCase(String name);
}