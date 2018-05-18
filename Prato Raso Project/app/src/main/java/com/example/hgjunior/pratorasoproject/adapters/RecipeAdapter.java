package com.example.hgjunior.pratorasoproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hgjunior.pratorasoproject.R;
import com.example.hgjunior.pratorasoproject.models.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

private ArrayList<Recipe> recipe;
private Context context;

    public RecipeAdapter(@NonNull Context context, ArrayList<Recipe> object) {
        super(context, 0, object);
        this.context = context;
        this.recipe = object;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(recipe != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.row_listview, parent, false);

            ImageView imageRecipe = (ImageView)view.findViewById(R.id.image_recipe);
            TextView nameRecipe = (TextView)view.findViewById(R.id.txtNameRecipe);
            TextView favititesRecipe = (TextView)view.findViewById(R.id.txtFavorites);

            Recipe recipes = recipe.get(position);

            nameRecipe.setText(recipes.getName());
            favititesRecipe.setText(recipes.getLike() + " pessoas favoritaram");
        }

        return view;
    }
}
