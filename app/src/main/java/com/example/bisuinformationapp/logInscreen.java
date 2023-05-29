package com.example.bisuinformationapp;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class logInscreen extends AppCompatActivity {
  EditText emailAdd,password;
  Button loginButton,adminBtn;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_inscreen);

        mAuth = FirebaseAuth.getInstance();

        emailAdd = findViewById(R.id.emailTF);
        password = findViewById(R.id.passTF);
        loginButton = findViewById(R.id.loginBTN);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAdd.getText().toString();
                String pass = password.getText().toString();

                if (isValidEmail(email)) {
                    signInWithEmailAndPassword(email, pass);
                } else {
                    Toast.makeText(logInscreen.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adminBtn = findViewById(R.id.adminBtn);
       adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Admin Account
                String desiredEmail = "administrator@bisu.edu.ph";
                String desiredPassword = "admin1";

                String enteredEmail = emailAdd.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredEmail.equals(desiredEmail) && enteredPassword.equals(desiredPassword)) {
                    // Successful login
                    Intent intent = new Intent(logInscreen.this, dashboard.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(logInscreen.this,"Login successful!",Toast.LENGTH_SHORT).show();
                } else {
                    // Invalid credentials
                    Toast.makeText(logInscreen.this,"Invalid email or password!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Checking for valid email address using regex
        private boolean isValidEmail (String email){
            // Perform basic email format validation
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0 -9.-]+$";
            return email.matches(emailRegex);
        }
        //Check for new user email and password and also create is email does exist
    private void signInWithEmailAndPassword(String email, String password) {
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(logInscreen.this, new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result.getSignInMethods().isEmpty()) {
                                // Email does not exist, create a new user
                                createNewUser(email, password);
                            } else {
                                // Email exists, proceed with sign-in
                                signInExistingUser(email, password);
                            }
                        } else {
                            // Error occurred while checking email existence
                            Toast.makeText(logInscreen.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
        //Insert new email and password in firebase authentication
    private void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(logInscreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // New user created successfully, navigate to the dashboard
                            Toast.makeText(logInscreen.this, "User created successfully.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(logInscreen.this, infoDashboard.class);
                            startActivity(intent);
                        } else {
                            // Failed to create new user
                            Toast.makeText(logInscreen.this, "Failed to create user.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
        //Sign in with the existing email and password function
    private void signInExistingUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(logInscreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Authentication successful, navigate to the dashboard
                            Toast.makeText(logInscreen.this, "Authentication Success.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(logInscreen.this, infoDashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Authentication failed, display an error message
                            Toast.makeText(logInscreen.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
