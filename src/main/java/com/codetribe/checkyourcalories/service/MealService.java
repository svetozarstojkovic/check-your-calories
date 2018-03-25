package com.codetribe.checkyourcalories.service;

import com.codetribe.checkyourcalories.dtos.DayDto;
import com.codetribe.checkyourcalories.model.Meal;

import java.util.Collection;
import java.util.List;

public interface MealService {

    List<Meal> getAllMeals();

    List<DayDto> getAllMealsWithInDescendingDates();

    Meal addNewMeal(Meal meal);

    Collection<Meal> searchDescription(String description);
}
