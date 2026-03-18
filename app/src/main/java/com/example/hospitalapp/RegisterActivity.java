package com.example.hospitalapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText regName, regEmail, regPass, regConfirm;
    Button btnRegister;
    DatabaseHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        regConfirm = findViewById(R.id.regConfirmPass);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = regName.getText().toString();
            String email = regEmail.getText().toString();
            String pass = regPass.getText().toString();
            String conf = regConfirm.getText().toString();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(conf)) {
                Toast.makeText(this, "Passwords mismatch", Toast.LENGTH_SHORT).show();
            } else {
                if (db.registerUser(name, email, pass)) {
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}