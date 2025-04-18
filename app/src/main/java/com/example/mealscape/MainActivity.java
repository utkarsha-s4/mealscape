package com.example.mealscape;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText queryEditText;
    private TextView mealNameTextView, instructionsTextView;
    private ImageView mealImageView;
    private Button searchButton, saveButton, viewFavoritesButton, shareButton;

    private Meal currentMeal;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Room DB
        db = AppDatabase.getInstance(this);

        // Bind Views
        queryEditText = findViewById(R.id.queryEditText);
        mealNameTextView = findViewById(R.id.recipeName);
        instructionsTextView = findViewById(R.id.recipeInstructions);
        mealImageView = findViewById(R.id.recipeImage);
        searchButton = findViewById(R.id.searchButton);
        saveButton = findViewById(R.id.saveButton);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);
        shareButton = findViewById(R.id.shareButton);

        // Search Logic
        searchButton.setOnClickListener(v -> {
            String keyword = queryEditText.getText().toString().trim();
            if (!keyword.isEmpty()) {
                fetchRecipe(keyword);
            } else {
                Toast.makeText(this, "Please enter a keyword", Toast.LENGTH_SHORT).show();
            }
        });

        // Save to Favorites
        saveButton.setOnClickListener(v -> {
            if (currentMeal != null) {
                new Thread(() -> {
                    db.recipeDao().insert(currentMeal);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Recipe saved to favorites!", Toast.LENGTH_SHORT).show()
                    );
                }).start();
            } else {
                Toast.makeText(this, "No recipe to save", Toast.LENGTH_SHORT).show();
            }
        });

        // View Favorites
        viewFavoritesButton.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoritesActivity.class));
        });

        // Share Recipe
        shareButton.setOnClickListener(v -> {
            if (currentMeal != null) {
                String shareText = currentMeal.getStrMeal() + "\n\n" + currentMeal.getStrInstructions();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Share Recipe via"));
            } else {
                Toast.makeText(this, "No recipe to share", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecipe(String keyword) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<RecipeResponse> call = apiService.searchMeals(keyword);
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getMeals() != null) {
                    List<Meal> meals = response.body().getMeals();
                    if (!meals.isEmpty()) {
                        currentMeal = meals.get(0);
                        showMealDetails(currentMeal);
                    } else {
                        Toast.makeText(MainActivity.this, "No recipes found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch recipes.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMealDetails(Meal meal) {
        mealNameTextView.setText(meal.getStrMeal());
        instructionsTextView.setText(meal.getStrInstructions());
        Picasso.get().load(meal.getStrMealThumb()).into(mealImageView);
    }
}
