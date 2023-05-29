package com.example.bisuinformationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class dashboard extends AppCompatActivity {
    CardView onClickEvent, viewEvent,calendar;
    Button logOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        onClickEvent = findViewById(R.id.createEvent);
        viewEvent = findViewById(R.id.viewEvent);
        logOutBtn = findViewById(R.id.logOutBtn);
        calendar=findViewById(R.id.calendarIn);

        onClickEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, creaturesMenu.class);
                startActivity(intent);
            }
        });

        viewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, infoDashboard.class);
                startActivity(intent);
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, calendar.class);
                startActivity(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, logInscreen.class);
                startActivity(intent);
                finish();
                Toast.makeText(dashboard.this,"Logout Successful!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}