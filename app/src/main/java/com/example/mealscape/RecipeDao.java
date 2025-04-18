package com.example.mealscape;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    // Insert a new recipe (Meal)
    @Insert
    void insert(Meal meal);  // ✅ Correct: insert Meal object

    // Retrieve all favorite recipes
    @Query("SELECT * FROM favorite_recipes")
    List<Meal> getAllFavorites();
}
