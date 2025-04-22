package com.example.ez_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.example.ez_inventory.dao.UserDao;
import com.example.ez_inventory.data.User;

public class MainActivity extends BaseActivity {
    private EditText usernameEditText; // EditText for username input
    private EditText passwordEditText; // EditText for password input
    private UserDao userDao; // DAO for user operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        // Button for login action
        Button loginButton = findViewById(R.id.loginButton);
        // Button for registration action
        Button registerButton = findViewById(R.id.registerButton);

        // Initialize the database and DAO
        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();

        // Set click listeners for buttons
        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> register());
    }

    // Handle user login
    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input before hitting the database
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Log.d("LOGIN", "Attempting login for user: " + username);

            User user = userDao.login(username, password);
            Log.d("LOGIN", "Login result: " + (user != null ? "Success" : "Failure"));

            runOnUiThread(() -> {
                if (user != null) {
                    // Store login state
                    getSharedPreferences("AppPrefs", MODE_PRIVATE)
                            .edit()
                            .putBoolean("isLoggedIn", true)
                            .apply();

                    // Go to Inventory screen
                    Log.d("LOGIN", "Starting InventoryActivity...");
                    Intent intent = new Intent(this, InventoryActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    finish(); // Optional: disables back button from returning to login
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    passwordEditText.setText("");
                    passwordEditText.requestFocus();
                }
            });
        }).start();
    }

    // Handle user registration
    private void register() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(this, "Password must be between 6 and 20 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User existingUser = userDao.getUserByUsername(username); // 👈 Check if taken

            if (existingUser != null) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show()
                );
            } else {
                // Create a new user and insert into the database
                userDao.insert(new User(username, password));
                runOnUiThread(() ->
                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}