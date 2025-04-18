package com.example.mealscape;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ListView favoritesListView;
    private RecipeAdapter recipeAdapter;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesListView = findViewById(R.id.favoritesListView);

        // Initialize the RecipeAdapter with the context, layout, and an empty list
        recipeAdapter = new RecipeAdapter(this, R.layout.recipe_item, new ArrayList<>());
        favoritesListView.setAdapter(recipeAdapter);

        // Initialize Room database instance
        appDatabase = AppDatabase.getInstance(this);

        // Load favorite recipes from Room DB
        loadFavoriteRecipes();
    }

    private void loadFavoriteRecipes() {
        // Execute database fetch in a background thread
        new Thread(() -> {
            // Fetch the favorite recipes from the database
            List<Meal> favoriteRecipes = appDatabase.recipeDao().getAllFavorites();

            // Switch back to the main thread to update the UI
            runOnUiThread(() -> {
                // Clear the adapter data and add the new favorites
                recipeAdapter.clear();
                recipeAdapter.addAll(favoriteRecipes); // You might need to implement this method in RecipeAdapter
            });
        }).start();
    }
}
