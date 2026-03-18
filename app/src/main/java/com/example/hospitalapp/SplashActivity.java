package com.example.hospitalapp; // Ensure this matches your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide the Action Bar for a full-screen look
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Delay for 3 seconds (3000 milliseconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Task 4.2: Navigate to Login Screen after splash
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

                // Finish SplashActivity so the user can't go back to it
                finish();
            }
        }, 3000);
    }
}