package com.example.hgjunior.pratorasoproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hgjunior.pratorasoproject.adapters.RecipeAdapter;
import com.example.hgjunior.pratorasoproject.connections.ConnectionFirebase;
import com.example.hgjunior.pratorasoproject.connections.ConnectionPreferences;
import com.example.hgjunior.pratorasoproject.models.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ConnectionPreferences preferences;
    private View headerView;
    private ListView listView;
    private ArrayAdapter<Recipe> adapter;
    private ArrayList<Recipe> recipes;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);

        recipes = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewRecipe);
        adapter = new RecipeAdapter(this, recipes);
        listView.setAdapter(adapter);

        firebase = ConnectionFirebase.getFirebase().child("Recipes");
        valueEventListenerRecipes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipes.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Recipe newRecipe = data.getValue(Recipe.class);
                    recipes.add(newRecipe);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "Nome: " + recipes.get(position).getId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, RecipeViewActivity.class);
                intent.putExtra("id", recipes.get(position).getId());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchRecipeActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            if (preferences.getEmailPreferences() == null) {
                Intent intent_add = new Intent(MainActivity.this, LoginQuestionActivity.class);
                startActivity(intent_add);
            } else {
                Intent intent_add = new Intent(MainActivity.this, RecipeRegister.class);
                startActivity(intent_add);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recipes) {

        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_contacts) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_terms) {

        } else if (id == R.id.nav_exit) {
            preferences.logOut();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerRecipes);

        preferences = new ConnectionPreferences(MainActivity.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_menu = navigationView.getMenu();

        if (preferences.getEmailPreferences() == null) {
            nav_menu.findItem(R.id.nav_exit).setVisible(false);
            nav_menu.findItem(R.id.nav_recipes).setVisible(false);
            nav_menu.findItem(R.id.nav_favorites).setVisible(false);
        } else {
            nav_menu.findItem(R.id.nav_login).setVisible(false);
        }

        TextView textView = (TextView) headerView.findViewById(R.id.txt_nameUser);
        textView.setText(preferences.getEmailPreferences());
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerRecipes);
    }
}