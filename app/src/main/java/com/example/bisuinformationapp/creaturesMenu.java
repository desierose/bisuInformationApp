package com.example.bisuinformationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class creaturesMenu extends AppCompatActivity {

    private static final int FILE_PICKER_REQUEST_CODE = 1;
    FirebaseDatabase admin = FirebaseDatabase.getInstance();

    EditText datePicker;
    EditText eventNameTxt,dateTxt,locationTxt,descriptionTxt;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatures_menu);

        eventNameTxt = findViewById(R.id.eventName);
        dateTxt = findViewById(R.id.date);
        locationTxt = findViewById(R.id.location);
        descriptionTxt = findViewById(R.id.description);


        datePicker = findViewById(R.id.date);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(creaturesMenu.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Do something with the selected date
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(creaturesMenu.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Do something with the selected time
                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String dateTime = sdf.format(calendar.getTime());
                                datePicker.setText(dateTime);
                            }
                        }, hour, minute, false);

                        timePickerDialog.show();
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        //attach file function
        Button uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");  // Set the MIME type or restrict to specific file types (e.g., "image/*" for images)
                startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);

            }
        });
// Submitting events into apps
        submit = findViewById(R.id.submitBTN);
        submit.setOnClickListener(new View.OnClickListener() {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("events");

            @Override
            public void onClick(View v) {
                String eventName = eventNameTxt.getText().toString();
                String date = dateTxt.getText().toString();
                String location = locationTxt.getText().toString();
                String description = descriptionTxt.getText().toString();

                // Write the data to the database
                admin event = new admin("",eventName, date, location, description);
                myRef.push().setValue(event)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data successfully written to the database
                                eventNameTxt.getText().clear();
                                dateTxt.getText().clear();
                                locationTxt.getText().clear();
                                descriptionTxt.getText().clear();
                                Toast.makeText(creaturesMenu.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to write data to the database
                                Toast.makeText(creaturesMenu.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the URI of the selected file
            Uri fileUri = data.getData();

            // Perform further operations with the file
            // For example, you can upload it to a server using networking libraries or save it locally.
        }
    }

}