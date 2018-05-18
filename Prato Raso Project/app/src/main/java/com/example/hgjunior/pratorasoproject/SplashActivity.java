package com.example.hgjunior.pratorasoproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar my_progressBar;
    private int interval = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        my_progressBar = (ProgressBar) findViewById(R.id.progress);

        new Thread() {
            @Override
            public void run() {

                try {
                    while (interval < 100) {
                        sleep(10);
                        interval += 1;
                        my_progressBar.setProgress(interval);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }
}
