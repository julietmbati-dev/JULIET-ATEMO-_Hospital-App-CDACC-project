package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Declaration matches XML IDs
    EditText etWard, etBed, etNotes;
    Spinner spinnerServices;
    Button btnSubmit, btnLogout; // Added Logout button
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. HIDE THE ACTION BAR (Removes the bold text at the top)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        // Initialize Database
        db = new DatabaseHelper(this);

        // Matching Java variables to XML IDs
        etWard = findViewById(R.id.etWard);
        etBed = findViewById(R.id.etBed);
        etNotes = findViewById(R.id.etNotes);
        spinnerServices = findViewById(R.id.spinnerServices);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Optional: Find Logout button if you add it to your XML
        // btnLogout = findViewById(R.id.btnLogout);

        // Button Logic
        btnSubmit.setOnClickListener(v -> {
            String ward = etWard.getText().toString().trim();
            String bed = etBed.getText().toString().trim();
            String service = spinnerServices.getSelectedItem().toString();
            String notes = etNotes.getText().toString().trim();

            // Task 2.2: Basic Validation
            if (ward.isEmpty() || bed.isEmpty() || service.contains("Select")) {
                Toast.makeText(this, "Please select a service and enter Ward/Bed", Toast.LENGTH_SHORT).show();
            } else {
                // Task 2.5: Save to SQLite Database
                boolean success = db.submitRequest(service, ward, bed, notes);

                if (success) {
                    Toast.makeText(this, "Request Submitted Successfully!", Toast.LENGTH_LONG).show();

                    // Clear fields after success
                    etWard.setText("");
                    etBed.setText("");
                    etNotes.setText("");
                    spinnerServices.setSelection(0);
                } else {
                    Toast.makeText(this, "Error saving request", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Optional: Logout Logic
        /*
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
        */
    }
}