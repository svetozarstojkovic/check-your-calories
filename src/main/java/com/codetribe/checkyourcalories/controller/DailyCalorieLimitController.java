package com.codetribe.checkyourcalories.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codetribe.checkyourcalories.global.DailyCalorieLimit.DAILY_CALORIE_LIMIT;

@RestController
@RequestMapping(value = "/daily-limit")
public class DailyCalorieLimitController {

    @GetMapping(value = "/set/{value}")
    public ResponseEntity<Integer> setDailyLimit(@PathVariable("value") Integer value) {
        return ResponseEntity.ok(DAILY_CALORIE_LIMIT = value);
    }
}
