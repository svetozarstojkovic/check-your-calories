package com.codetribe.checkyourcalories.controller;

import com.codetribe.checkyourcalories.dtos.DayDto;
import com.codetribe.checkyourcalories.model.Meal;
import com.codetribe.checkyourcalories.service.MealService;
import com.codetribe.checkyourcalories.util.MapUtil;
import com.sun.deploy.net.HttpResponse;
import info.debatty.java.stringsimilarity.Levenshtein;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping()
    public ResponseEntity<List<DayDto>> getMeals() {
        try {
            return ResponseEntity.ok(mealService.getAllMealsWithInDescendingDates());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Example meal
     * {
         "date":"2018-02-23",
         "time":"23:00:00",
         "description":"Strawberry cheesecake",
         "calories":29
        }
     * @param meal - json representation of model
     * @return - created meal
     */
    @PostMapping(value = "/add")
    public ResponseEntity<Meal> addNewMeal(@RequestBody Meal meal) {
        try {
            return ResponseEntity.ok(mealService.addNewMeal(meal));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Description is done using Jaccard similarity
     * @param description - String by which the search is done
     * @return - collection of Meals
     */
    @GetMapping(value = "/search/{description}")
    public ResponseEntity<Collection<Meal>> searchMeals(@PathVariable("description") String description) {
        try {
            return ResponseEntity.ok(mealService.searchDescription(description));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
