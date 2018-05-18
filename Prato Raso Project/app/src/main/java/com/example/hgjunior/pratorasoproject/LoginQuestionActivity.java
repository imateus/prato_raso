package com.example.hgjunior.pratorasoproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginQuestionActivity extends AppCompatActivity {

    private Button btn_loginEmail, btn_newRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_question);

        btn_loginEmail = (Button) findViewById(R.id.btn_loginEmail);
        btn_newRegister = (Button) findViewById(R.id.btn_newRegister);

        clickEvents();
    }

    private void clickEvents() {

        btn_newRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_email = new Intent(LoginQuestionActivity.this, LoginRegisterActivity.class);
                startActivity(intent_email);
                finish();
            }
        });

        btn_loginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_email = new Intent(LoginQuestionActivity.this, LoginActivity.class);
                startActivity(intent_email);
                finish();
            }
        });

    }
}
