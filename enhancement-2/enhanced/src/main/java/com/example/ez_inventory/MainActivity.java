package com.example.ez_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab UI elements
        TextView welcomeText = findViewById(R.id.welcomeText);
        TextView subText = findViewById(R.id.subText);
        Button startButton = findViewById(R.id.startButton);


        // Load animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Apply animation to views
        welcomeText.startAnimation(fadeIn);
        subText.startAnimation(fadeIn);
        startButton.startAnimation(fadeIn);

        String userRole = getIntent().getStringExtra("userRole");

        // Navigate to login
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("userRole", userRole);
            startActivity(intent);
        });
    }
}
