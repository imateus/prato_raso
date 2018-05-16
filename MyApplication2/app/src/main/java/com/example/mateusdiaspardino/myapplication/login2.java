package com.example.mateusdiaspardino.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class login2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }

    public void abrirFeed2(View view){
        Intent feed = new Intent(this, LoginActivity.class);
        startActivity(feed);
    }
}
