package com.example.bisuinformationapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class logInscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_inscreen);
    }

    public void forgetPasswordClicked(View view) {

        Toast.makeText(this, "Forget password clicked", Toast.LENGTH_SHORT).show();
    }
}
