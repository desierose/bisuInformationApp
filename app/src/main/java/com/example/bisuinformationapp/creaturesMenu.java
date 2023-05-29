package com.example.bisuinformationapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class creaturesMenu extends AppCompatActivity {

    private static final int FILE_PICKER_REQUEST_CODE = 1;
    TextView fileTextView;
    EditText datePicker;
    EditText eventNameTxt,dateTxt,locationTxt,descriptionTxt;
    Button submit;
    DatabaseReference myRef;
    Uri selectedFileUri;
    StorageReference storageRef;
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
        fileTextView = findViewById(R.id.fileTextView);
        fileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
            }
        });

         // Submitting events into apps
        submit = findViewById(R.id.submitBTN);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String eventName = eventNameTxt.getText().toString();
                String date = dateTxt.getText().toString();
                String location = locationTxt.getText().toString();
                String description = descriptionTxt.getText().toString();

                myRef = FirebaseDatabase.getInstance().getReference("events");
                storageRef = FirebaseStorage.getInstance().getReference();
                // Write the data to the database
                admin event = new admin("",eventName, date, location, description,"");
                DatabaseReference newEventRef = myRef.push();
                String eventId = newEventRef.getKey(); // Get the key of the newly created event
                event.setEventID(eventId);
                newEventRef.setValue(event)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data successfully written to the database
                                eventNameTxt.getText().clear();
                                dateTxt.getText().clear();
                                locationTxt.getText().clear();
                                descriptionTxt.getText().clear();
                                Toast.makeText(creaturesMenu.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                                if (selectedFileUri != null) {
                                    String filePath = "document/files/";
                                    String fileId = UUID.randomUUID().toString(); // Generate a unique ID for the file
                                    String fileStoragePath = filePath + fileId; // Append the unique ID to the storage path

                                    UploadTask uploadTask = storageRef.child(fileStoragePath).putFile(selectedFileUri);
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            storageRef.child(fileStoragePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri downloadUrl) {
                                                    String downloadUrlString = downloadUrl.toString();
                                                    Toast.makeText(creaturesMenu.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                                                    updateFileDownloadUrl(eventId, downloadUrlString);
                                                }
                                            })
                                            ;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(creaturesMenu.this, "File upload failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
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
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                String fileName = getFileName(selectedFileUri);
                fileTextView.setText(fileName);
            }
        }
    }

    // Utility method to get the file name from the URI
    private String getFileName(Uri uri) {
        String fileName = "";
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName;
    }
    // Update the download URL of the file in the Realtime Database
    private void updateFileDownloadUrl(String eventId, String downloadUrl) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventId).child("document");
        eventRef.setValue(downloadUrl);
}
}