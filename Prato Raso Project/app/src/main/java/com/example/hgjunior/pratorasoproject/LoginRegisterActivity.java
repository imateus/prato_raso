package com.example.hgjunior.pratorasoproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hgjunior.pratorasoproject.connections.ConnectionFirebase;
import com.example.hgjunior.pratorasoproject.connections.ConnectionPreferences;
import com.example.hgjunior.pratorasoproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import static com.example.hgjunior.pratorasoproject.core.Functions.encodeBase64;
import static com.example.hgjunior.pratorasoproject.core.Functions.pleaseWait;
import static com.example.hgjunior.pratorasoproject.core.Functions.pleaseWaitClose;

public class LoginRegisterActivity extends AppCompatActivity {

    private EditText edt_newUserEmail, edt_newUserName, edt_newUserPassword;
    private Button btn_registrNewUser;
    private FirebaseAuth firebaseAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        edt_newUserEmail = (EditText) findViewById(R.id.edt_newUserEmail);
        edt_newUserName = (EditText) findViewById(R.id.edt_newUserName);
        edt_newUserPassword = (EditText) findViewById(R.id.edt_newUserPassword);
        btn_registrNewUser = (Button) findViewById(R.id.btn_registerNewUser);
        firebaseAuth = ConnectionFirebase.getFirebaseAuth();
        user = new User();

        clickEvents();
    }

    private void clickEvents() {

        btn_registrNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName(edt_newUserName.getText().toString());
                user.setEmail(edt_newUserEmail.getText().toString());
                user.setPassword(edt_newUserPassword.getText().toString());

                pleaseWait(LoginRegisterActivity.this, "Por favor, aguarde!", "Validando dados ...");
                registerNewUser();
            }
        });

    }

    private void registerNewUser() {

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user.setId(encodeBase64(user.getEmail()));
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    user.registerUser();

                    ConnectionPreferences preferences = new ConnectionPreferences(LoginRegisterActivity.this);
                    preferences.saveUserPreferences(encodeBase64(user.getEmail()), user.getEmail());

                    Toast.makeText(LoginRegisterActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    pleaseWaitClose();
                    String errorException = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException ex) {
                        errorException = "Digite uma senha masi forte como no minimo 6 caracteres.";
                    } catch (FirebaseAuthInvalidCredentialsException ex) {
                        errorException = "O e-mail digitado não é valido.";
                    } catch (FirebaseAuthUserCollisionException ex) {
                        errorException = "O e-mail informada já está cadastrado no sistema.";
                    } catch (Exception ex) {
                        errorException = "Erro ao efetuar o cadastro.";
                        ex.printStackTrace();
                    }

                    Toast.makeText(LoginRegisterActivity.this, "ERRO: " + errorException, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
