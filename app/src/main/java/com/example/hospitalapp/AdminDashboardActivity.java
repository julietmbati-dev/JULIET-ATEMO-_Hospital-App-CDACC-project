package com.example.hospitalapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {
    ListView lvRequests;
    Button btnViewRequests, btnViewUsers, btnManageServices, btnAddUser, btnLogout;
    DatabaseHelper db;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    // View state: 0 = Requests, 1 = Users, 2 = Services
    int currentView = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_admin_dashboard);

        db = new DatabaseHelper(this);
        lvRequests = findViewById(R.id.lvRequests);
        btnViewRequests = findViewById(R.id.btnViewRequests);
        btnViewUsers = findViewById(R.id.btnViewUsers);
        btnManageServices = findViewById(R.id.btnManageServices); // NEW BUTTON
        btnAddUser = findViewById(R.id.btnAddUser);
        btnLogout = findViewById(R.id.btnLogout);

        loadRequestsData();

        // Switch to Requests
        btnViewRequests.setOnClickListener(v -> {
            currentView = 0;
            btnAddUser.setVisibility(View.GONE);
            loadRequestsData();
        });

        // Switch to Users (Task 2.5.d)
        btnViewUsers.setOnClickListener(v -> {
            currentView = 1;
            btnAddUser.setText("+ Add New Patient");
            btnAddUser.setVisibility(View.VISIBLE);
            loadUsersData();
        });

        // Switch to Services (Task 2.5.b and 2.5.c)
        btnManageServices.setOnClickListener(v -> {
            currentView = 2;
            btnAddUser.setText("+ Add New Service");
            btnAddUser.setVisibility(View.VISIBLE);
            loadServicesData();
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Combined ADD Logic
        btnAddUser.setOnClickListener(v -> {
            if (currentView == 1) showAddUserDialog();
            else if (currentView == 2) showAddServiceDialog();
        });

        // Combined DELETE/COMPLETE Logic
        lvRequests.setOnItemClickListener((parent, view, position, id) -> {
            String item = list.get(position);
            String dbId = item.split(" ")[1];

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (currentView == 1) { // Delete User
                builder.setTitle("User Management")
                        .setMessage("Delete Patient ID #" + dbId + "?")
                        .setPositiveButton("Delete", (d, w) -> {
                            db.deleteUser(dbId);
                            loadUsersData();
                        });
            } else if (currentView == 2) { // Delete Service (Task 2.5.c)
                builder.setTitle("Service Management")
                        .setMessage("Remove this service type?")
                        .setPositiveButton("Remove", (d, w) -> {
                            db.deleteService(dbId);
                            loadServicesData();
                        });
            } else { // Complete Request (Task 2.5.a)
                builder.setTitle("Request Management")
                        .setMessage("Mark Request #" + dbId + " as Finished?")
                        .setPositiveButton("Complete", (d, w) -> {
                            db.deleteRequest(dbId);
                            loadRequestsData();
                        });
            }
            builder.setNegativeButton("Cancel", null).show();
        });
    }

    private void showAddServiceDialog() { // Task 2.5.b
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Service Name");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Add", (d, w) -> {
            String name = input.getText().toString().trim();
            if(!name.isEmpty()) {
                db.addService(name);
                loadServicesData();
            }
        });
        builder.setNegativeButton("Cancel", null).show();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Patient");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);
        final EditText nameInput = new EditText(this); nameInput.setHint("Name");
        final EditText emailInput = new EditText(this); emailInput.setHint("Email");
        final EditText passInput = new EditText(this); passInput.setHint("Password");
        layout.addView(nameInput); layout.addView(emailInput); layout.addView(passInput);
        builder.setView(layout);
        builder.setPositiveButton("Add", (dialog, which) -> {
            if(!nameInput.getText().toString().isEmpty()) {
                db.registerUser(nameInput.getText().toString(), emailInput.getText().toString(), passInput.getText().toString());
                loadUsersData();
            }
        });
        builder.setNegativeButton("Cancel", null).show();
    }

    void loadRequestsData() {
        list = new ArrayList<>();
        Cursor c = db.getAllRequests();
        while (c.moveToNext()) {
            list.add("ID: " + c.getString(0) + " | " + c.getString(1) + "\nWard: " + c.getString(2) + " Bed: " + c.getString(3));
        }
        updateListView();
    }

    void loadUsersData() {
        list = new ArrayList<>();
        Cursor c = db.getAllUsers();
        while (c.moveToNext()) {
            list.add("ID: " + c.getString(0) + " | Name: " + c.getString(1) + "\nEmail: " + c.getString(2));
        }
        updateListView();
    }

    void loadServicesData() { // Task 2.5.b
        list = new ArrayList<>();
        Cursor c = db.getAllServices();
        while (c.moveToNext()) {
            list.add("ID: " + c.getString(0) + " | " + c.getString(1));
        }
        updateListView();
    }

    void updateListView() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lvRequests.setAdapter(adapter);
    }
}