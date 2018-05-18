package com.example.hgjunior.pratorasoproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hgjunior.pratorasoproject.connections.ConnectionFirebase;
import com.example.hgjunior.pratorasoproject.connections.ConnectionPreferences;
import com.example.hgjunior.pratorasoproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.hgjunior.pratorasoproject.core.Functions.encodeBase64;
import static com.example.hgjunior.pratorasoproject.core.Functions.pleaseWait;
import static com.example.hgjunior.pratorasoproject.core.Functions.pleaseWaitClose;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_loginEmail, edt_loginPassword;
    private Button btn_login;
    private User user;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_loginEmail = (EditText) findViewById(R.id.edt_loginEmail);
        edt_loginPassword = (EditText) findViewById(R.id.edt_loginPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        firebaseAuth = ConnectionFirebase.getFirebaseAuth();

        clickEvents();

    }

    private void clickEvents() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_loginEmail.getText().toString().isEmpty() && !edt_loginPassword.getText().toString().isEmpty()) {
                    user = new User();
                    user.setEmail(edt_loginEmail.getText().toString());
                    user.setPassword(edt_loginPassword.getText().toString());

                    pleaseWait(LoginActivity.this, "Por favor, aguarde!", "Validando dados ...");
                    validUserLogin();
                } else {
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validUserLogin() {
        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                pleaseWaitClose();

                if (task.isSuccessful()) {

                    ConnectionPreferences preferences = new ConnectionPreferences(LoginActivity.this);
                    preferences.saveUserPreferences(encodeBase64(edt_loginEmail.getText().toString()), edt_loginEmail.getText().toString());

                    Toast.makeText(LoginActivity.this, "Seja Bem-Vindo!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "E-mail e ou Senha inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
