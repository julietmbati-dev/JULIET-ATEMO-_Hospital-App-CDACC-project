package com.example.hospitalapp;

import android.content.Intent;import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPass;
    Button btnLogin;
    TextView tvRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. HIDE THE ACTION BAR (Removes bold class name at top)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);

        // Initialize
        db = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword); // Ensure this matches your XML ID exactly
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Login Click Logic
        btnLogin.setOnClickListener(v -> {
            // 2. Added .trim() to prevent space errors
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();

            // 3. Added Basic Validation (Task 2.2)
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                String role = db.checkLogin(email, pass);

                if (role != null) {
                    if (role.equals("admin")) {
                        startActivity(new Intent(this, AdminDashboardActivity.class));
                    } else {
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    // 4. Added finish() to prevent going back to login screen
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Redirect to Register
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}