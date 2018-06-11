package com.example.hgjunior.pratorasoproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hgjunior.pratorasoproject.adapters.RecipeAdapter;
import com.example.hgjunior.pratorasoproject.connections.ConnectionFirebase;
import com.example.hgjunior.pratorasoproject.connections.ConnectionPreferences;
import com.example.hgjunior.pratorasoproject.models.Recipe;
import com.example.hgjunior.pratorasoproject.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 *@author Mateus Dias Pardinho
 */
public class SearchRecipeActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    ConnectionPreferences preferences;
    private View headerView;
    private ListView listView;
    private ArrayAdapter<Recipe> adapter;
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private ArrayList<Recipe> recipesAll = new ArrayList<>();
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerRecipes;
    Spinner category, difficulty;
    String searchText;
    private Button btn_search_ingredient;
    private EditText edt_name, edt_time, edt_portion, edt_ingredient;
    private AlertDialog.Builder alertDialog;
    private ListView lv_search_ingredients, lv_preparation;
    private ArrayAdapter<String> adapter_ingredients, adapter_preparation;
    private ArrayList<String> list_ingredients, list_preparation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        Utils.darkenStatusBar(this, 0x7f06002c);

        EditText editText = (EditText) findViewById(R.id.editTextSearch);
        editText.setOnEditorActionListener(this);

        if (verificaConexao()){
            firebase = ConnectionFirebase.getFirebase().child("Recipes");
            valueEventListenerRecipes = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    recipesAll.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Recipe newRecipe = data.getValue(Recipe.class);
                        recipesAll.add(newRecipe);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            listView = (ListView) findViewById(R.id.listViewRecipe);
            adapter = new RecipeAdapter(this, new ArrayList<Recipe>());
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(MainActivity.this, "Nome: " + recipes.get(position).getId(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SearchRecipeActivity.this, RecipeViewActivity.class);
                    intent.putExtra("id", recipes.get(position).getId());
                    startActivity(intent);
                }
            });
        }else{
         System.out.println("Flha de conexão com a internet");
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerRecipes);
        preferences = new ConnectionPreferences(SearchRecipeActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerRecipes);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        searchText = new String();
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            ArrayList<Recipe> recipesAux = new ArrayList<Recipe>();
            recipes.clear();
            for (Recipe newRecipe : recipesAll) {
                EditText editText = (EditText) findViewById(R.id.editTextSearch);
                searchText = editText.getText().toString().toLowerCase();
                if (!searchText.isEmpty() && newRecipe.getName().toLowerCase().contains(searchText)){
                    recipes.add(newRecipe);
                }
            }

            listView = (ListView) findViewById(R.id.listViewRecipe);
            adapter = new RecipeAdapter(this, recipes);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SearchRecipeActivity.this, RecipeViewActivity.class);
                    intent.putExtra("id", recipes.get(position).getId());
                    startActivity(intent);
                }
            });

            if (recipes.size() == 0){
                System.out.println("Não foram encontradas receitas na busca");
            }
        }
        return true;
    }

    /* Função para verificar existência de conexão com a internet*/
    public  boolean verificaConexao() {
/*        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;*/
        return true;
    }

    public void onBackPressed(View v){
        startActivity(new Intent(this, MainActivity.class));
        return;
    }

    public void onFilterPressed(View v){
        EditText editText = (EditText) findViewById(R.id.editTextSearch);
        searchText = editText.getText().toString().toLowerCase();
        setContentView(R.layout.filter);
        category = (Spinner) findViewById(R.id.spin_Category2);
        difficulty = (Spinner) findViewById(R.id.spin_Difficulty2);
        list_ingredients = new ArrayList<String>();
        lv_search_ingredients = (ListView) findViewById(R.id.lv_search_ingredients);
        adapter_ingredients = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list_ingredients);
        lv_search_ingredients.setAdapter(adapter_ingredients);

        ArrayAdapter adapter_category = ArrayAdapter.createFromResource(SearchRecipeActivity.this, R.array.category_array, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter adapter_difficulty = ArrayAdapter.createFromResource(SearchRecipeActivity.this, R.array.difficulty_array, android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(adapter_category);
        difficulty.setAdapter(adapter_difficulty);
        btn_search_ingredient = (Button) findViewById(R.id.btn_search_addIngredient);

        clickEvents();
        return;
    }

    public void onConfirmationPressed(View v){
        setContentView(R.layout.activity_search_recipe);
        ArrayList<Recipe> recipesAux = new ArrayList<Recipe>();
        ArrayList<Recipe> recipesFind = new ArrayList<Recipe>();
        recipes.clear();
        if (searchText != null && !searchText.isEmpty()){
            EditText editText = (EditText) findViewById(R.id.editTextSearch);
            editText.setText(searchText);
            recipesFind.addAll(recipes);
        }else{
            recipesFind.addAll(recipesAll);
        }
/*        for (Recipe newRecipe : recipesAll) {
            EditText editText = (EditText) findViewById(R.id.editTextSearch);
            if (newRecipe.getName().toLowerCase().contains(searchText)){
                if(!difficulty.getSelectedItem().toString().equals("Dificuldade") && newRecipe.getDifficulty().equals(difficulty.getSelectedItem().toString())){
                    recipesAux.add(newRecipe);
                }
            }
        }*/

        /*Dificuldade*/
        if (!difficulty.getSelectedItem().toString().equals("Dificuldade")){
            recipesAux.clear();
            for (Recipe newRecipe : recipesFind) {
                if(newRecipe.getDifficulty() != null && newRecipe.getDifficulty().equals(difficulty.getSelectedItem().toString())){
                    recipesAux.add(newRecipe);
                }
            }
            recipesFind.clear();
            recipesFind.addAll(recipesAux);
        }else{

        }

        /*Categoria*/
        if (!category.getSelectedItem().toString().equals("Categoria")){
            recipesAux.clear();
            for (Recipe newRecipe : recipesFind) {
                if(newRecipe.getCategory() != null && newRecipe.getCategory().equals(category.getSelectedItem().toString())){
                    recipesAux.add(newRecipe);
                }
            }
            recipesFind.clear();
            recipesFind.addAll(recipesAux);
        }

        /*Ingredientes*/
        if (list_ingredients != null && !list_ingredients.isEmpty()){
            recipesAux.clear();
            for (Recipe newRecipe : recipesFind) {
                for (String ingredient : list_ingredients) {
                    for (String ingredientRecipe : newRecipe.getIngredientsList()) {
                        if(ingredientRecipe.equals(ingredient)){
                            recipesAux.add(newRecipe);
                        }
                    }
                }
            }
            recipesFind.clear();
            recipesFind.addAll(recipesAux);
        }



        if (!recipesFind.isEmpty()){
            recipes.clear();
            recipes.addAll(recipesFind);
        }

        listView = (ListView) findViewById(R.id.listViewRecipe);
        adapter = new RecipeAdapter(this, recipes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchRecipeActivity.this, RecipeViewActivity.class);
                intent.putExtra("id", recipes.get(position).getId());
                startActivity(intent);
            }
        });

        if (recipes.size() == 0){
            System.out.println("Não foram encontradas receitas na busca");
        }
        return;
    }

    public void onBackSearchPressed(View v){
        setContentView(R.layout.activity_search_recipe);
        recipes.clear();
        if (!searchText.isEmpty()){
            EditText editText = (EditText) findViewById(R.id.editTextSearch);
            editText.setText(searchText);
        }
        for (Recipe newRecipe : recipesAll) {
            EditText editText = (EditText) findViewById(R.id.editTextSearch);
            if (!searchText.isEmpty() && newRecipe.getName().toLowerCase().contains(searchText)){
                recipes.add(newRecipe);
            }
        }

        listView = (ListView) findViewById(R.id.listViewRecipe);
        adapter = new RecipeAdapter(this, recipes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchRecipeActivity.this, RecipeViewActivity.class);
                intent.putExtra("id", recipes.get(position).getId());
                startActivity(intent);
            }
        });
        return;
    }


    private void clickEvents() {
        btn_search_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(SearchRecipeActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog, null);
                edt_ingredient = (EditText) view.findViewById(R.id.edtIngredient);
                final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.cl_ingredient);

                alertDialog.setView(view)
                        .setCancelable(false).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!edt_ingredient.getText().toString().isEmpty()) {
                            String result = edt_ingredient.getText().toString();
                            list_ingredients.add(result);
                            adapter_ingredients.notifyDataSetChanged();
                            lv_search_ingredients.setVisibility(View.VISIBLE);

                            constraintLayout.setMinHeight(constraintLayout.getHeight() + 150);
                        }
                    }
                });
                AlertDialog ad = alertDialog.create();
                ad.show();
            }
        });

    }
}
