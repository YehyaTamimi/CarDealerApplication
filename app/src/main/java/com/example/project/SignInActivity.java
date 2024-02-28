package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private TextView textViewSignUp;
    private DataBaseHelper dbHelper;

    private CheckBox checkBoxRememberMe;
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        dbHelper = new DataBaseHelper(SignInActivity.this, "project", null, 1 ); // Initialize the database helper

        editTextEmail = findViewById(R.id.editTextLastName);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        checkBoxRememberMe = findViewById(R.id.checkboxRememberMe);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        checkSavedCredentials();

        Button buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCredentials()) {
                    attemptSignIn();
                } else {
                    editTextEmail.setError("Invalid email or password format");
                }
            }
        });


        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateCredentials() {
        String email = editTextEmail.getText().toString().toLowerCase();
        String password = editTextPassword.getText().toString();

        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && isValidPassword(password);
    }

    private void checkSavedCredentials() {
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            editTextEmail.setText(savedEmail);
            editTextPassword.setText(savedPassword);
            checkBoxRememberMe.setChecked(true);
        }
    }

    private void attemptSignIn() {
        String email = editTextEmail.getText().toString().toLowerCase();
        String password = editTextPassword.getText().toString();

        Cursor cursor = dbHelper.loginUser(email, password);
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            editTextEmail.setText("");
            editTextPassword.setText("");

            if (checkBoxRememberMe.isChecked()) {
                saveCredentials(email, password);
            } else {
                clearCredentials();
            }

                Intent intent = new Intent(SignInActivity.this, chooseDealerActivity.class);
                intent.putExtra("uid", String.valueOf(userId));
                startActivity(intent);
        } else {
            editTextEmail.setError("Wrong email or password");
            editTextPassword.setError("Wrong email or password");
        }

    }

    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void clearCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();
    }
    private boolean isValidPassword(String password) {
            return !password.isEmpty();
    }
}
