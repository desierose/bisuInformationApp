package com.example.bisuinformationapp;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class calendar extends AppCompatActivity {
 CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

           calendarView =findViewById(R.id.calendarView);
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Display the selected date in a toast message
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(calendar.this, date, Toast.LENGTH_SHORT).show();
            }
        });
        // Fetch event dates from Firebase and mark them on the calendar
        fetchEventDatesFromFirebase();
    }

    private void fetchEventDatesFromFirebase() {
        FirebaseDatabase.getInstance().getReference("events")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot dateSnapshot = eventSnapshot.child("date");
                            String dateString = dateSnapshot.getValue(String.class);
                            if (dateString != null) {
                                markDateOnCalendar(dateString);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(calendar.this, "Failed to fetch event dates", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void markDateOnCalendar(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(dateString);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                long timeInMillis = calendar.getTimeInMillis();
                calendarView.setDate(timeInMillis, true, true);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}