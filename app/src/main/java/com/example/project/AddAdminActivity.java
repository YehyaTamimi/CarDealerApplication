package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddAdminActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Image pick request code

    private EditText editTextEmail, editTextFirstName, editTextLastName, editTextGender, editTextPassword, editTextCountry, editTextCity, editTextPhoneNumber;
    private Button buttonSignUp;
    private DataBaseHelper dbHelper;
    private Uri selectedImageUri; // Uri of the selected image
    private String dealerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        Intent intent = getIntent();
        dealerId = intent.getStringExtra("dealerid");

        dbHelper = new DataBaseHelper(this, "project", null, 1);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextGender = findViewById(R.id.editTextGender);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextCity = findViewById(R.id.editTextCity);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllEditTextsToNormal();
                signUpUser();
            }
        });

    }

    private void signUpUser() {
        String email = editTextEmail.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String gender = editTextGender.getText().toString();
        String password = editTextPassword.getText().toString();
        String country = editTextCountry.getText().toString();
        String city = editTextCity.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String profilePictureUri = selectedImageUri != null ? selectedImageUri.toString() : "@drawable/default_profile";

        if (validateInput(email, firstName, lastName, gender, password, country, city, phoneNumber)) {
            dbHelper.addAdmin(email, firstName, lastName, gender, password, country, city,phoneNumber, dealerId);
            Toast.makeText(AddAdminActivity.this, "User Signed Up Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetAllEditTextsToNormal() {
        resetEditTextToNormal(editTextEmail, "Email");
        resetEditTextToNormal(editTextFirstName, "First Name");
        resetEditTextToNormal(editTextLastName, "Last Name");
        resetEditTextToNormal(editTextGender, "Gender");
        resetEditTextToNormal(editTextPassword, "Password");
        resetEditTextToNormal(editTextCountry, "Country");
        resetEditTextToNormal(editTextCity, "City");
        resetEditTextToNormal(editTextPhoneNumber, "Phone Number");
    }

    private boolean validateInput(String email, String firstName, String lastName, String gender, String password, String country, String city, String phoneNumber) {
        isValidEmail(email);isValidName(firstName);isValidName(lastName);isValidGender(gender);isValidPassword(password);
        return isValidEmail(email)
                && isValidName(firstName)
                && isValidName(lastName)
                && isValidGender(gender)
                && isValidPassword(password);
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty() || !email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            setEditTextError(editTextEmail, "Enter a valid email address");
            return false;
        }
        return true;
    }

    private boolean isValidName(String name) {
        if (name == null || name.isEmpty() || name.matches(".*\\d.*")) {
            setEditTextError(editTextFirstName, "Invalid name"); // Adjust for lastName if needed
            return false;
        }
        return true;
    }

    private boolean isValidGender(String gender) {
        if (!("Male".equalsIgnoreCase(gender) || "Female".equalsIgnoreCase(gender))) {
            setEditTextError(editTextGender, "Enter 'Male' or 'Female'");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            setEditTextError(editTextPassword, "Password must be 6+ chars");
            return false;
        }
        return true;
    }

    private void setEditTextError(EditText editText, String errorHint) {
        editText.setHint(errorHint);
        editText.setHintTextColor(getResources().getColor(R.color.errorColor));
    }

    private void resetEditTextToNormal(EditText editText, String normalHint) {
        editText.setHint(normalHint);
        editText.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // You can update an ImageView here to show the selected image
        }
    }
}
