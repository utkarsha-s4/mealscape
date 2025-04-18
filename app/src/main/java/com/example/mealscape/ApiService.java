package com.example.mealscape;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // Search meals by name (keyword)
    @GET("search.php")
    Call<RecipeResponse> searchMeals(@Query("s") String keyword);
}
