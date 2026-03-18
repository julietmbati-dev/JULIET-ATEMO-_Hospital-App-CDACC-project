package com.example.hospitalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Hospital.db";
    private static final int DATABASE_VERSION = 2; // Incremented version to apply changes

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_REQUESTS = "requests";
    public static final String TABLE_SERVICES = "services"; // New Table for Task 2.5.b/c

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Create Users Table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, EMAIL TEXT, PASSWORD TEXT, ROLE TEXT)");

        // 2. Create Requests Table
        db.execSQL("CREATE TABLE " + TABLE_REQUESTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE TEXT, WARD TEXT, BED TEXT, NOTES TEXT)");

        // 3. Create Services Table (To allow Admin to Add/Remove Services)
        db.execSQL("CREATE TABLE " + TABLE_SERVICES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE_NAME TEXT)");

        // Default Admin Account
        db.execSQL("INSERT INTO " + TABLE_USERS + " (USERNAME, EMAIL, PASSWORD, ROLE) VALUES ('Supervisor', 'admin@hosp.com', 'admin123', 'admin')");

        // Insert Default Services (Initial Data)
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (SERVICE_NAME) VALUES ('General Consultation')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (SERVICE_NAME) VALUES ('Wheelchair Assistance')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (SERVICE_NAME) VALUES ('Laboratory Test')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        onCreate(db);
    }

    // --- USER METHODS ---

    public boolean registerUser(String name, String email, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERNAME", name);
        values.put("EMAIL", email);
        values.put("PASSWORD", pass);
        values.put("ROLE", "patient");
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public String checkLogin(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ROLE FROM " + TABLE_USERS + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, pass});
        String role = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                role = cursor.getString(0);
            }
            cursor.close();
        }
        return role;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE ROLE='patient'", null);
    }

    public boolean deleteUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, "ID = ?", new String[]{id}) > 0;
    }

    // --- SERVICE MANAGEMENT METHODS (Task 2.5.b and 2.5.c) ---

    // Method to ADD a service (Task 2.5.b)
    public boolean addService(String serviceName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SERVICE_NAME", serviceName);
        return db.insert(TABLE_SERVICES, null, values) != -1;
    }

    // Method to REMOVE a service (Task 2.5.c)
    public boolean deleteService(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SERVICES, "ID = ?", new String[]{id}) > 0;
    }

    // Method to get all services for Spinner or Admin List
    public Cursor getAllServices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SERVICES, null);
    }

    // --- REQUEST METHODS ---

    public boolean submitRequest(String service, String ward, String bed, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SERVICE", service);
        values.put("WARD", ward);
        values.put("BED", bed);
        values.put("NOTES", notes);
        long result = db.insert(TABLE_REQUESTS, null, values);
        return result != -1;
    }

    public Cursor getAllRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_REQUESTS, null);
    }

    public boolean deleteRequest(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_REQUESTS, "ID = ?", new String[]{id}) > 0;
    }
}