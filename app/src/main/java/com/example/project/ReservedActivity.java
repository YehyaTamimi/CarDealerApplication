package com.example.project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
public class ReservedActivity extends AppCompatActivity {

    String dealerId;

    DataBaseHelper db;
    List<UserReservations> reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved);

        Intent intent = getIntent();
        dealerId = intent.getStringExtra("dealerid");
        Log.d("DEALEERRR1", dealerId);

        TableLayout table = findViewById(R.id.tableReservations);

        db = new DataBaseHelper(this, "project", null, 1);
        reservations = db.getReservations(dealerId);

        for (UserReservations reservation : reservations) {
            int userId = reservation.getUserId();
            int carId = reservation.getCarId();
            String dateOfReservation = reservation.getReserveDate();

            String userName = reservation.getFirstName() +" "+ reservation.getLastName();
            String carModel = reservation.getCarName();

            TableRow row = new TableRow(this);

            TextView tvUserName = new TextView(this);
            tvUserName.setText(userName);
            tvUserName.setTextColor(Color.WHITE);
            row.addView(tvUserName);

            TextView tvUserId = new TextView(this);
            tvUserId.setText(String.valueOf(userId));
            tvUserId.setTextColor(Color.WHITE);
            row.addView(tvUserId);

            TextView tvCarModel = new TextView(this);
            tvCarModel.setText(carModel);
            tvCarModel.setTextColor(Color.WHITE);
            row.addView(tvCarModel);

            TextView tvCarId = new TextView(this);
            tvCarId.setText(String.valueOf(carId));
            tvCarId.setTextColor(Color.WHITE);
            row.addView(tvCarId);

            TextView tvDate = new TextView(this);
            tvDate.setText(dateOfReservation);
            tvDate.setTextColor(Color.WHITE);
            row.addView(tvDate);

            table.addView(row);
        }
    }
}
