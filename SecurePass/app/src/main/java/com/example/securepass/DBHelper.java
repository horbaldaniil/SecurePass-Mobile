package com.example.securepass;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "securepass.db";

    public static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PASSWORDS = "passwords";
    public static final String TABLE_FOLDERS = "folders";

    // Common column names
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_ID = "user_id";

    // USERS table - column names
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    // PASSWORDS table - column names
    public static final String KEY_TITLE = "title";
    public static final String KEY_EMAIL_USERNAME = "email_username";
    public static final String KEY_FOLDER_ID = "folder_id";
    public static final String KEY_LAST_UPDATED = "last_updated";
    public static final String KEY_DELETED = "deleted";

    // FOLDERS table - column names
    public static final String KEY_FOLDER_TITLE = "title";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating tables
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_EMAIL + " TEXT UNIQUE NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_FOLDERS_TABLE = "CREATE TABLE " + TABLE_FOLDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FOLDER_TITLE + " TEXT UNIQUE NOT NULL,"
                + KEY_USER_ID + " INTEGER NOT NULL,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_FOLDERS_TABLE);

        String CREATE_PASSWORDS_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT UNIQUE NOT NULL,"
                + KEY_EMAIL_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_FOLDER_ID + " INTEGER,"
                + KEY_USER_ID + " INTEGER NOT NULL,"
                + KEY_LAST_UPDATED + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + KEY_DELETED + " BOOLEAN NOT NULL DEFAULT 0,"
                + "FOREIGN KEY(" + KEY_FOLDER_ID + ") REFERENCES " + TABLE_FOLDERS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_PASSWORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed and create them again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void generateUserTestData() {
        String currentTime = String.valueOf(System.currentTimeMillis());

        String sql = "INSERT INTO " + TABLE_PASSWORDS + " ("+ KEY_TITLE + ", " + KEY_PASSWORD + ",  " + KEY_EMAIL_USERNAME + ", " + KEY_USER_ID + ", " + KEY_LAST_UPDATED + ", " + KEY_DELETED + ") " +
                "VALUES " +
                "('Title1', 'password1', 'your1@email.com', 1, '" + currentTime + "', 0), " +
                "('Title2', 'password2', 'your2@email.com', 1, '" + currentTime + "', 0), " +
                "('Title3', 'password3', 'your3@email.com', 1, '" + currentTime + "', 0)";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    @SuppressLint("Range")
    public int checkUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL + " = ? AND " + KEY_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        int userId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            cursor.close();
        }
        db.close();

        return userId;
    }

    public String getUserEmailById(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_EMAIL};
        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        String userEmail = null;
        if (cursor != null && cursor.moveToFirst()) {
            userEmail = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
            cursor.close();
        }
        db.close();

        return userEmail;
    }


    public long addUser(String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1; // Default user ID if insertion fails
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EMAIL, email);
            values.put(KEY_PASSWORD, password);
            userId = db.insertOrThrow(TABLE_USERS, null, values);
        } catch (SQLException e) {
            // Handle insertion failure (e.g., duplicate email)
            Log.e("DBHelper", "Error adding user: " + e.getMessage());
        } finally {
            db.close();
        }
        return userId;
    }

    public Cursor getPasswordsForUser(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PASSWORDS + " WHERE " + KEY_USER_ID + " = ? AND " + KEY_DELETED + " = 0";
        String[] selectionArgs = {String.valueOf(userId)};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getPasswordById(int passwordId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PASSWORDS + " WHERE " + KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(passwordId)};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getTrashedPasswords(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PASSWORDS + " WHERE " + KEY_DELETED + " = 1 AND " + KEY_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        return db.rawQuery(query, selectionArgs);
    }

    // Method to get all the passwords with given user_id and folder_id
    public Cursor getPasswordsForUserAndFolder(int userId, int folderId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PASSWORDS + " WHERE " + KEY_USER_ID + " = ? AND " + KEY_FOLDER_ID + " = ? AND " + KEY_DELETED + " = 0";
        String[] selectionArgs = {String.valueOf(userId), String.valueOf(folderId)};
        return db.rawQuery(query, selectionArgs);
    }

    // Method to delete a password by id (flag it as deleted)
    public void softDeletePasswordById(int passwordId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DELETED, 1);
        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {String.valueOf(passwordId)};
        db.update(TABLE_PASSWORDS, values, whereClause, whereArgs);
        db.close();
    }

    // Method to delete a password by id (delete its row from the table)
    public void deletePasswordById(int passwordId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {String.valueOf(passwordId)};
        db.delete(TABLE_PASSWORDS, whereClause, whereArgs);
        db.close();
    }

    // Method to add a password to the table with given attributes
    public void addPassword(String title, String emailUsername, String password, int folderId, int userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_EMAIL_USERNAME, emailUsername);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_FOLDER_ID, folderId);
        values.put(KEY_USER_ID, userId);
        db.insert(TABLE_PASSWORDS, null, values);
        db.close();
    }

    // Method to update a password by id
    public void updatePasswordById(int passwordId, String newTitle, String newEmailUsername, String newPassword, int newFolderId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, newTitle);
        values.put(KEY_EMAIL_USERNAME, newEmailUsername);
        values.put(KEY_PASSWORD, newPassword);
        values.put(KEY_FOLDER_ID, newFolderId);
        values.put(KEY_LAST_UPDATED, Calendar.getInstance().getTimeInMillis()); // Update last_updated to current time in local time zone
        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {String.valueOf(passwordId)};

        db.update(TABLE_PASSWORDS, values, whereClause, whereArgs);
        db.close();
    }

    public Cursor getFoldersForUser(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FOLDERS + " WHERE " + KEY_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getOldPasswordsForUser(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        long sixMonthsAgoMillis = System.currentTimeMillis() - (6 * 30 * 24 * 60 * 60 * 1000L); // 6 months in milliseconds
        String query = "SELECT * FROM " + TABLE_PASSWORDS + " WHERE " + KEY_USER_ID + " = ? AND " + KEY_LAST_UPDATED + " < ?";
        String[] selectionArgs = {String.valueOf(userId), String.valueOf(sixMonthsAgoMillis)};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getReusedPasswordsForUser(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = getPasswordsForUser(userId);

        Map<String, Integer> passwordCounts = new HashMap<>();

        // Iterate through the cursor to count occurrences of each password
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));

            // Increment the count of occurrences of this password
            passwordCounts.put(password, passwordCounts.getOrDefault(password, 0) + 1);
        }

        cursor.close();

        // Create a list of reused passwords
        List<String> reusedPasswords = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : passwordCounts.entrySet()) {
            if (entry.getValue() > 1) {
                reusedPasswords.add(entry.getKey());
            }
        }

        // Retrieve passwords with reused passwords from the database
        StringJoiner passwordJoiner = new StringJoiner("', '", "'", "'");
        for (String password : reusedPasswords) {
            passwordJoiner.add(password);
        }

        String query = "SELECT * FROM " + TABLE_PASSWORDS + " WHERE " + KEY_USER_ID + " = ? AND " + KEY_PASSWORD + " IN (" + passwordJoiner.toString() + ")";
        String[] selectionArgs = {String.valueOf(userId)};
        return db.rawQuery(query, selectionArgs);
    }

    public void restorePasswordById(int passwordId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DELETED, 0); // Mark the password as not deleted
        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {String.valueOf(passwordId)};
        db.update(TABLE_PASSWORDS, values, whereClause, whereArgs);
        db.close();
    }

    public void deleteAllTrashedPasswordsForUser(int userId) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = KEY_USER_ID + " = ? AND " + KEY_DELETED + " = ?";
        String[] whereArgs = {String.valueOf(userId), "1"};

        db.delete(TABLE_PASSWORDS, whereClause, whereArgs);

        db.close();
    }

    public void addFolder(String folderTitle, int userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FOLDER_TITLE, folderTitle);
        values.put(KEY_USER_ID, userId);
        db.insert(TABLE_FOLDERS, null, values);
        db.close();
    }

    public void deleteFolder(int folderId) {
        SQLiteDatabase db = getWritableDatabase();

        // Remove the folder association from passwords in this folder
        ContentValues nullFolderValues = new ContentValues();
        nullFolderValues.putNull(KEY_FOLDER_ID);
        String whereClause = KEY_FOLDER_ID + " = ?";
        String[] whereArgs = {String.valueOf(folderId)};
        db.update(TABLE_PASSWORDS, nullFolderValues, whereClause, whereArgs);

        // Delete the folder from the folders table
        whereClause = KEY_ID + " = ?";
        whereArgs = new String[]{String.valueOf(folderId)};
        db.delete(TABLE_FOLDERS, whereClause, whereArgs);

        db.close();
    }

}