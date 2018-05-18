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

public class List_Recipe_Adapter extends ArrayAdapter<Recipe> {

    private final Context context;
    private final ArrayList<Recipe> element;

    public List_Recipe_Adapter(@NonNull Context context, ArrayList<Recipe> element) {
        super(context, R.layout.row_listview, element);

        this.context = context;
        this.element = element;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_listview, null);

        ImageView imageRecipe = (ImageView)rowView.findViewById(R.id.image_recipe);
        TextView nameRecipe = (TextView)rowView.findViewById(R.id.txtNameRecipe);
        TextView favititesRecipe = (TextView)rowView.findViewById(R.id.txtFavorites);

        //imageRecipe.setImageResource(element.get(position).getImage());
        nameRecipe.setText(element.get(position).getName().toString());
        favititesRecipe.setText(element.get(position).getLike() + " pessoas favoritaram");

        return rowView;
    }
}
