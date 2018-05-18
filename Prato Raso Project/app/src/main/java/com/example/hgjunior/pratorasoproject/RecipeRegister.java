package com.example.hgjunior.pratorasoproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hgjunior.pratorasoproject.connections.ConnectionFirebase;
import com.example.hgjunior.pratorasoproject.core.Functions;
import com.example.hgjunior.pratorasoproject.models.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static com.example.hgjunior.pratorasoproject.core.Functions.encodeBase64;

public class RecipeRegister extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Recipe recipes;
    private EditText edt_name, edt_time, edt_portion, edt_ingredient;
    Spinner category, difficulty;
    private Button btn_ingredient, btn_preparation, btn_addRecipe;
    private ImageButton ib_addImage;
    private ListView lv_ingredients, lv_preparation;
    private ArrayList<String> list_ingredients, list_preparation;
    private ArrayAdapter<String> adapter_ingredients, adapter_preparation;
    private AlertDialog.Builder alertDialog;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private LinearLayout ln_imageRecipe;

    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_register);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Adicionar nova receita");

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        category = (Spinner) findViewById(R.id.spin_Category);
        difficulty = (Spinner) findViewById(R.id.spin_Difficulty);

        ArrayAdapter adapter_category = ArrayAdapter.createFromResource(RecipeRegister.this, R.array.category_array, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter adapter_difficulty = ArrayAdapter.createFromResource(RecipeRegister.this, R.array.difficulty_array, android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(adapter_category);
        difficulty.setAdapter(adapter_difficulty);

        btn_ingredient = (Button) findViewById(R.id.btn_addIngredient);
        btn_preparation = (Button) findViewById(R.id.btn_addPreparation);
        ib_addImage = (ImageButton) findViewById(R.id.ib_addImage);
        lv_ingredients = (ListView) findViewById(R.id.lv_ingredients);
        lv_preparation = (ListView) findViewById(R.id.lv_preparation);
        list_ingredients = new ArrayList<String>();
        list_preparation = new ArrayList<String>();
        adapter_ingredients = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list_ingredients);
        adapter_preparation = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list_preparation);
        lv_ingredients.setAdapter(adapter_ingredients);
        lv_preparation.setAdapter(adapter_preparation);
        edt_name = (EditText) findViewById(R.id.edtNameRecipe);
        edt_time = (EditText) findViewById(R.id.edtTime);
        edt_portion = (EditText) findViewById(R.id.edtPortion);
        imageView = (ImageView) findViewById(R.id.imgRecipe);
        ln_imageRecipe = (LinearLayout) findViewById(R.id.lv_imageRecipe);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btn_addRecipe = (Button) findViewById(R.id.btn_addNewRecipe);

        clickEvents();
    }

    private void clickEvents() {
        btn_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(RecipeRegister.this);
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
                            lv_ingredients.setVisibility(View.VISIBLE);

                            constraintLayout.setMinHeight(constraintLayout.getHeight() + 150);
                        }
                    }
                });
                AlertDialog ad = alertDialog.create();
                ad.show();
            }
        });

        btn_preparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(RecipeRegister.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog, null);
                edt_ingredient = (EditText) view.findViewById(R.id.edtIngredient);
                final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.cl_preparation);

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
                            list_preparation.add(result);
                            adapter_preparation.notifyDataSetChanged();
                            lv_preparation.setVisibility(View.VISIBLE);

                            constraintLayout.setMinHeight(constraintLayout.getHeight() + 150);
                        }
                    }
                });
                AlertDialog ad = alertDialog.create();
                ad.show();
            }
        });

        ib_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                ib_addImage.setVisibility(View.GONE);
                ln_imageRecipe.setVisibility(View.VISIBLE);
            }
        });

        btn_addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione a imagem"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {

            if (filePath != null) {

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Por favor aguarde...");
                progressDialog.show();

                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        recipes = new Recipe();
                        recipes.setId(encodeBase64(edt_name.getText().toString()));
                        recipes.setName(edt_name.getText().toString());
                        recipes.setPreparation(Integer.parseInt(edt_time.getText().toString()));
                        recipes.setPortion(Integer.parseInt(edt_portion.getText().toString()));
                        recipes.setDifficulty(difficulty.getSelectedItem().toString());
                        recipes.setIngredients_qtd(list_ingredients.size());
                        recipes.setIngredientsList(list_ingredients);
                        recipes.setPreparationList(list_preparation);
                        recipes.setImage(taskSnapshot.getDownloadUrl().toString());
                        recipes.setLike("0");

                        registerRecipe(recipes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RecipeRegister.this, "Falha!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.setMessage("Salvando receita!");
                    }
                });
            }
    }

    private void registerRecipe(Recipe recipes) {
        try {
            databaseReference = ConnectionFirebase.getFirebase().child("Recipes");
            databaseReference.child(recipes.getName()).setValue(recipes);

            Intent intent = new Intent(RecipeRegister.this, MainActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
