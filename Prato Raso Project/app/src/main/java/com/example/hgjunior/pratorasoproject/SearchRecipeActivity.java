package com.example.hgjunior.pratorasoproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
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
import android.widget.EditText;
import android.widget.ListView;
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
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            ArrayList<Recipe> recipesAux = new ArrayList<Recipe>();
            recipes.clear();
            for (Recipe newRecipe : recipesAll) {
                EditText editText = (EditText) findViewById(R.id.editTextSearch);
                String text = editText.getText().toString();
                if (newRecipe.getName().contains(text)){
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
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    public void onBackPressed(View v){
        startActivity(new Intent(this, MainActivity.class));
        return;
    }

}
