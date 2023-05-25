package com.example.bisuinformationapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splashScreen extends AppCompatActivity {

    // I-set ang duration ng splash screen (millisecond)
    private static final long SPLASH_SCREEN_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Handler para sa delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Mag-navigate sa main activity pagkatapos ng duration
                Intent intent = new Intent(splashScreen.this, logInscreen.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_DURATION);
    }
}

