package com.example.mealscape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Meal> {

    // Constructor for the adapter
    public RecipeAdapter(Context context, int resource, List<Meal> meals) {
        super(context, resource, meals);  // Pass the context, resource, and list of meals to the parent constructor
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate the layout for each list item
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_item, parent, false);
        }

        // Get the Meal object for the current position
        Meal meal = getItem(position);

        // Initialize the views for the list item
        TextView mealName = convertView.findViewById(R.id.mealName);
        ImageView mealImage = convertView.findViewById(R.id.mealImage);

        // Set the data for the views
        mealName.setText(meal.getStrMeal());
        Picasso.get().load(meal.getStrMealThumb()).into(mealImage);

        return convertView;  // Return the populated view for the list item
    }
}
