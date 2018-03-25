package com.codetribe.checkyourcalories.repository;

import com.codetribe.checkyourcalories.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Meal findByDescription(String description);
}
