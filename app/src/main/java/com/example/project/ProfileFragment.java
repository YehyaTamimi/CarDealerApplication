package com.example.project;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileFragment extends Fragment {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText phoneNumberEditText;
    private Button updateButton;

    private TextView firstName;
    private TextView lastName;
    private TextView phoneNumber;
    private TextView password;

    User user;


    private DataBaseHelper db;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI elements
        firstNameEditText = view.findViewById(R.id.editTextFirstName);
        lastNameEditText = view.findViewById(R.id.editTextLastName);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        confirmPasswordEditText = view.findViewById(R.id.editTextConfirmPassword);
        phoneNumberEditText = view.findViewById(R.id.editTextPhoneNumber);

        updateButton = view.findViewById(R.id.buttonUpdate);
        ScrollView scrollView = view.findViewById(R.id.scrollView);

        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        password = view.findViewById(R.id.password);

        Bundle bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
        }


        // Initialize DatabaseHelper
        db = new DataBaseHelper(getActivity(), "project", null, 1);

        // Load user information
        loadUserInfo();
        // Replace with your actual layout ID
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });

        // Set onClickListener for the update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if(validateInput()){
                    updateUser();
                    clearEditTextFields();
                    loadUserInfo();
                }
//                updateUser();
            }
        });

        return view;
    }

    private void loadUserInfo() {
        user = db.getUserById(uid);

        if (user != null) {
            String first = "Current First Name: "+user.getFirstName();
            String last = "Current Last Name: "+user.getLastName();
            String pass = "Current Password Name: "+user.getPassword();
            String num = "Current Phone number Name: "+user.getPhoneNumber();
            firstName.setText(first);
            lastName.setText(last);
            password.setText(pass);
            phoneNumber.setText(num);
        }
    }

    private void updateUser() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();


        if(firstName.isEmpty()){
            firstName = user.getFirstName();
        }else {
            firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        }

        if(lastName.isEmpty()){
            lastName = user.getLastName();
        }else{
            lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        }

        if(phoneNumber.isEmpty()){
            phoneNumber = user.getPhoneNumber();
        }

        if(password.isEmpty()){
            password = user.getPassword();
        }

        // Add your validation logic here based on the specified conditions

        // Update user information
        db.updateUser(uid, firstName, lastName, phoneNumber, password);

        // Optionally, you can show a success message or navigate to another screen
        Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }


    private boolean validateInput() {
        boolean isValid = true;

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (firstName.length() < 3 && !firstName.isEmpty()) {
            firstNameEditText.setError("First name must be at least 3 characters");
            isValid = false;
        }

        if (lastName.length() < 3 && !lastName.isEmpty()) {
            lastNameEditText.setError("Last name must be at least 3 characters");
            isValid = false;
        }

        if ((password.length() < 5 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*].*")) && !password.isEmpty()) {
            passwordEditText.setError("Password must be at least 5 characters and include a letter, a number, and a special character");
            isValid = false;
        }

        if ((!password.equals(confirmPassword)) && !password.isEmpty() && !confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Passwords do not match");
            isValid = false;
        }

        if ((phoneNumber.length() != 10) && !phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Phone number must be 10 digits in total");
            isValid = false;
        }

        return isValid;
    }

    private void hideKeyboard(View view) {
        // Get the InputMethodManager
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        // Hide the keyboard
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void clearEditTextFields() {
        clearEditText(firstNameEditText);
        clearEditText(lastNameEditText);
        clearEditText(passwordEditText);
        clearEditText(confirmPasswordEditText);
        clearEditText(phoneNumberEditText);
    }

    // Helper function to clear an EditText if it contains a string
    private void clearEditText(EditText editText) {
        if (editText != null && editText.getText() != null && editText.getText().length() > 0) {
            editText.setText("");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close the database when the fragment is destroyed
        if (db != null) {
            db.close();
        }
    }
}
