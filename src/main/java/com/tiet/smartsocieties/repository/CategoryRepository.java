package com.tiet.smartsocieties.repository;

import com.tiet.smartsocieties.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
