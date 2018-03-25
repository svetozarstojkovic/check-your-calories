package com.codetribe.checkyourcalories.service;

import com.codetribe.checkyourcalories.dtos.DayDto;
import com.codetribe.checkyourcalories.model.Meal;
import com.codetribe.checkyourcalories.repository.MealRepository;
import com.codetribe.checkyourcalories.util.MapUtil;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.Levenshtein;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.util.*;

import static com.codetribe.checkyourcalories.global.DailyCalorieLimit.DAILY_CALORIE_LIMIT;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository mealRepository;

    @Override
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    @Override
    public List<DayDto> getAllMealsWithInDescendingDates() {
        List<Meal> meals = mealRepository.findAll();

        meals.sort(Collections.reverseOrder());

        List<DayDto> days = new ArrayList<>();
        Date tempDate = null;
        DayDto tempDay;

        for (Meal meal : meals) {
            if (meal.getDate().equals(tempDate)) {
                tempDay = days.get(days.size() - 1);
                tempDay.getMeals().add(meal);
                tempDay.setTotalCalories(tempDay.getTotalCalories() + meal.getCalories());
                setDailyStatus(tempDay, tempDay.getTotalCalories() + meal.getCalories());
            } else {
                tempDay = new DayDto();
                tempDay.setDate(meal.getDate());
                tempDay.getMeals().add(meal);
                tempDay.setTotalCalories(meal.getCalories());
                setDailyStatus(tempDay, meal.getCalories());
                days.add(tempDay);

            }
            tempDate = meal.getDate();
        }

        return days;
    }

    @Override
    public Meal addNewMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    @Override
    public Collection<Meal> searchDescription(String description) {
        List<Meal> meals = getAllMeals();
        Levenshtein levenshtein = new Levenshtein();
        Jaccard jaccard = new Jaccard();

        Map<Meal, Double> mealMap = new HashMap();
        for (Meal meal : meals) {
            if (meal.getDescription() != null) {
                mealMap.put(meal, jaccard.distance(description.toLowerCase(), meal.getDescription().toLowerCase()));
            }
        }

        return MapUtil.sortByValue(mealMap).keySet();
    }

    private void setDailyStatus(DayDto dayDto, Integer value) {
        final String statusMessage = "You have%s exceeded the daily limit of %d calories";
        if (value > DAILY_CALORIE_LIMIT) {
            dayDto.setStatus(String.format(statusMessage, "", DAILY_CALORIE_LIMIT));
        } else {
            dayDto.setStatus(String.format(statusMessage, " not", DAILY_CALORIE_LIMIT));
        }
    }
}
