package com.codetribe.checkyourcalories;

import com.codetribe.checkyourcalories.dtos.DayDto;
import com.codetribe.checkyourcalories.model.Meal;
import com.codetribe.checkyourcalories.repository.MealRepository;
import com.codetribe.checkyourcalories.service.MealService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckYourCaloriesApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

	@Autowired
	private MealService mealService;

	@Autowired
	private MealRepository mealRepository;

	@Test
	public void testAddingMeal() {
		Meal meal = new Meal();
		meal.setDate(new Date(System.currentTimeMillis()));
		meal.setTime(new Time(System.currentTimeMillis()));
		meal.setDescription("Cheesecake with strawberries");
		meal.setCalories(1000);

		mealRepository.save(meal);

		Meal repositoryMeal = mealRepository.findByDescription("Cheesecake with strawberries");

		mealRepository.delete(repositoryMeal);

		assertEquals(meal.getDescription(), repositoryMeal.getDescription());
	}

	@Test
	public void testDailyDescendingOrder() {
		// given
		for (int i = 0; i < 4; i++) {
			Meal meal = new Meal();
			meal.setDate(new Date(new GregorianCalendar(2014, Calendar.FEBRUARY, i / 2 + 1).getTimeInMillis()));
			meal.setTime(new Time(new GregorianCalendar(2014, Calendar.FEBRUARY, i / 2 + 1).getTimeInMillis()));
			meal.setDescription("Cheesecake with strawberries " + Integer.toString(i / 2 + 1));
			meal.setCalories(i * 10 + 1000);

			mealService.addNewMeal(meal);
		}

		List<String> expectedDescriptions = new ArrayList<>();

		for (int i=1; i >= 0; i--){
			expectedDescriptions.add("Cheesecake with strawberries " + Integer.toString(i + 1));
			expectedDescriptions.add("Cheesecake with strawberries " + Integer.toString(i + 1));
		}

		List<DayDto> actualDays = mealService.getAllMealsWithInDescendingDates();

		List<String> actualDescriptions = new ArrayList<>();
		for (DayDto dayDto : actualDays) {
			for (Meal meal : dayDto.getMeals()) {
				actualDescriptions.add(meal.getDescription());
			}
		}

		for (DayDto dayDto : actualDays) {
		    for (Meal meal : dayDto.getMeals()) {
                mealRepository.delete(meal);
            }
        }


		assertArrayEquals(expectedDescriptions.toArray(), actualDescriptions.toArray());
	}

	@Test
	public void testSearchingMeals() {
		String expectedDescription = "Cheesecake with strawberries3";
		for (int i=0; i < 5; i++) {
			Meal meal = new Meal();
			meal.setDate(new Date(System.currentTimeMillis()));
			meal.setTime(new Time(System.currentTimeMillis()));
			meal.setDescription("Cheesecake with strawberries"+Integer.toString(i));
			meal.setCalories(1000);

			mealService.addNewMeal(meal);
		}

		Collection<Meal> actualMeals = mealService.searchDescription("strawberries3");

        for (Meal meal : actualMeals) {
            mealRepository.delete(meal);
        }

		assertEquals(actualMeals.iterator().next().getDescription(), expectedDescription);
	}

    @Test
    public void testExceededDailyLimitOfCalories() {
        for (int i=0; i < 3; i++) {
            Meal meal = new Meal();
            meal.setDate(new Date(new GregorianCalendar(2014, Calendar.FEBRUARY, 2).getTimeInMillis()));
            meal.setTime(new Time(new GregorianCalendar(2014, Calendar.FEBRUARY, 2).getTimeInMillis()));
            meal.setDescription("Cheesecake with strawberriesExceeded");
            meal.setCalories(30);

            mealService.addNewMeal(meal);
        }

        List<DayDto> actualDays = mealService.getAllMealsWithInDescendingDates();

        for (DayDto dayDto : actualDays) {
            for (Meal meal : dayDto.getMeals()) {
                mealRepository.delete(meal);
            }
        }

        assertEquals(actualDays.get(0).getStatus(), "You have exceeded the daily limit of 50 calories");
    }

    @Test
    public void testNotExceededDailyLimitOfCalories() {
        for (int i=0; i < 3; i++) {
            Meal meal = new Meal();
            meal.setDate(new Date(new GregorianCalendar(2018, Calendar.FEBRUARY, 1).getTimeInMillis()));
            meal.setTime(new Time(new GregorianCalendar(2018, Calendar.FEBRUARY, 1).getTimeInMillis()));
            meal.setDescription("Cheesecake with strawberriesExceededNotExceeded");
            meal.setCalories(10);

            mealService.addNewMeal(meal);
        }

        List<DayDto> actualDays = mealService.getAllMealsWithInDescendingDates();

        for (DayDto dayDto : actualDays) {
            for (Meal meal : dayDto.getMeals()) {
                mealRepository.delete(meal);
            }
        }

        assertEquals(actualDays.get(0).getStatus(), "You have not exceeded the daily limit of 50 calories");
    }
}
