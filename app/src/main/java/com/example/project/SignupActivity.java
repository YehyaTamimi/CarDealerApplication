package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText editTextFirstName, editTextLastName, editTextEmail,editTextPassword, editTextConfirmPassword, editTextCountryCode, editTextPhoneNumber;
    private Spinner spinnerGender, spinnerCountry, spinnerCity;
    private Map<String, String> countryPhoneCodeMap = new HashMap<>();
    private Map<String, String[]> countryCitiesMap = new HashMap<>();
    private ImageView imageViewProfile;
    private static final int IMAGE_PICK_REQUEST = 1;
    private String profilePictureUriString = null; // URI as String

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
        setupData();
        setupSpinners();

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    signUpUser();
                }
            }
        });

    }

    private void initializeViews() {
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerCity = findViewById(R.id.spinnerCity);
    }

    private void setupData() {
        // Populate Country and Phone Code Map
        countryPhoneCodeMap.put("Palestine", "00970");
        countryPhoneCodeMap.put("Jordan", "00962");
        countryPhoneCodeMap.put("Syria", "00963");
        countryPhoneCodeMap.put("Lebanon", "00961");

        // Populate Country and Cities Map
        countryCitiesMap.put("Palestine", new String[]{"Ramallah", "Jericho", "Bethlehem", "Nablus"});
        countryCitiesMap.put("Jordan", new String[]{"Amman", "Alzarqa", "Irbid", "Salt"});
        countryCitiesMap.put("Syria", new String[]{"Damascus", "Aleppo", "Hama", "Homs"});
        countryCitiesMap.put("Lebanon", new String[]{"Beirut", "Tripoli", "Byblos", "Baalbek"});
    }
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageViewProfile.setImageURI(imageUri);

            profilePictureUriString = imageUri.toString();
        }
    }

    private void setupSpinners() {
        // Setup Gender Spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Setup Country Spinner
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countryPhoneCodeMap.keySet().toArray(new String[0]));
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        // Country Spinner item selected listener
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountry = spinnerCountry.getSelectedItem().toString();
                updateCitySpinner(selectedCountry);
                updatePhoneNumberPrefix(selectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateCitySpinner(String country) {
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countryCitiesMap.get(country));
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);
    }

    private void updatePhoneNumberPrefix(String country) {
        String phoneCode = countryPhoneCodeMap.get(country);
        editTextCountryCode.setText(phoneCode);
    }

    private boolean validateInput() {
        String email = editTextEmail.getText().toString();

        if (editTextFirstName.getText().toString().length() < 3) {
            editTextFirstName.setError("First name must be at least 3 characters");
            return false;
        }

        if (editTextLastName.getText().toString().length() < 3) {
            editTextLastName.setError("Last name must be at least 3 characters");
            return false;
        }

        String password = editTextPassword.getText().toString();
        if (password.length() < 5 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*].*")) {
            editTextPassword.setError("Password must be at least 5 characters and include a letter, a number, and a special character");
            return false;
        }

        if (!password.equals(editTextConfirmPassword.getText().toString())) {
            editTextConfirmPassword.setError("Passwords do not match");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email address");
            return false;
        }

        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String selectedCountry = spinnerCountry.getSelectedItem().toString();
        String countryCode = countryPhoneCodeMap.get(selectedCountry);

        String fullPhoneNumber = editTextCountryCode.getText().toString() + editTextPhoneNumber.getText().toString();

        if (fullPhoneNumber.length() != 14) {
            editTextPhoneNumber.setError("Phone number must be 14 digits in total");
            return false;
        }


        return true;
    }

    private void signUpUser() {
        if (!validateInput()) {
            Toast.makeText(this, "Validation failed", Toast.LENGTH_SHORT).show();
            return; //dont continue if fails
        }

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = editTextEmail.getText().toString().toLowerCase();
        String password = editTextPassword.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String country = spinnerCountry.getSelectedItem().toString();
        String city = spinnerCity.getSelectedItem().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String profilePictureUri = profilePictureUriString; // Use the URI string
        DataBaseHelper dbHelper = new DataBaseHelper(SignupActivity.this, "project",null , 1);
        if (dbHelper.getUserByEmail(email) != null) {
            Toast.makeText(this, "Account already exists!", Toast.LENGTH_SHORT).show();
            return; //dont continue if fails
        }

        long row = dbHelper.addUserOfficial(email, firstName, lastName, gender, password, country, city, phoneNumber);
        Toast.makeText(this, "Signed up successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignupActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}
