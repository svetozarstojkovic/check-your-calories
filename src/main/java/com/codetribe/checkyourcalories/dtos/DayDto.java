package com.codetribe.checkyourcalories.dtos;

import com.codetribe.checkyourcalories.model.Meal;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DayDto {

    private Date date;
    private List<Meal> meals = new ArrayList<>();
    private Integer totalCalories;
    private String status;

    public DayDto() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public Integer getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Integer totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
