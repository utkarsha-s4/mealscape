package com.example.mealscape;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipeResponse {
    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }
}
