package com.example.mealscape;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
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
        recipeAdapter = new RecipeAdapter(this, R.layout.recipe_item, new ArrayList<>());
        favoritesListView.setAdapter(recipeAdapter);
        appDatabase = AppDatabase.getInstance(this);
        loadFavoriteRecipes();

        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Meal selectedRecipe = (Meal) recipeAdapter.getItem(position);
                if (selectedRecipe != null) {
                    Log.d("FavoritesActivity", "Selected recipe: " + selectedRecipe.getStrMeal());
                    Intent intent = new Intent(FavoritesActivity.this, MainActivity.class); // Or RecipeDetailActivity
                    intent.putExtra("selected_recipe", (Serializable) selectedRecipe); // Cast to Serializable
                    startActivity(intent);
                } else {
                    Log.e("FavoritesActivity", "Selected recipe is null at position: " + position);
                }
            }
        });
    }

    private void loadFavoriteRecipes() {
        new Thread(() -> {
            try {
                List<Meal> favoriteRecipes = appDatabase.recipeDao().getAllFavorites();
                runOnUiThread(() -> {
                    recipeAdapter.clear();
                    recipeAdapter.addAll(favoriteRecipes);
                    Log.d("FavoritesActivity", "Loaded " + favoriteRecipes.size() + " favorite recipes");
                });
            } catch (Exception e) {
                Log.e("FavoritesActivity", "Error loading favorites: ", e);
            }
        }).start();
    }
}