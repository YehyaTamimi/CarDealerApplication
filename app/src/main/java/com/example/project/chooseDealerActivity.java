package com.example.project;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class chooseDealerActivity extends AppCompatActivity {

    LinearLayout layout;
    String uid;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dealer);
        dbHelper = new DataBaseHelper(chooseDealerActivity.this, "project", null, 1);
        layout = (LinearLayout) findViewById(R.id.chooseDealer);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        addDealerToView();
    }

    public void addDealerToView(){
        Cursor cursor = dbHelper.getAllDealers();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Button temp = createButton(cursor);
                layout.addView(temp);
            } while (cursor.moveToNext());
        }


    }

    public Button createButton(Cursor cursor){

        String temp = cursor.getString(0);

        Button button = new Button(this);
        button.setBackgroundResource(R.drawable.rounded_button);
        button.setText(cursor.getString(1));
        button.setTypeface(null, android.graphics.Typeface.BOLD);
        button.setGravity(Gravity.CENTER);

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int marginInPixels = convertDpToPixels(this, 8); // Change the margin as needed
        params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

        button.setLayoutParams(params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int admin;
                admin = dbHelper.getUserDealerAdminStatus(uid, temp);
                if (admin == 1) {
                    Intent intent = new Intent(chooseDealerActivity.this, AdminActivity.class);
                    intent.putExtra("dealerid", temp);
                    intent.putExtra("uid", uid);
                    chooseDealerActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(chooseDealerActivity.this, UserActivity.class);
                    intent.putExtra("dealerid", temp);
                    intent.putExtra("uid", uid);
                    chooseDealerActivity.this.startActivity(intent);
                }
            }
        });

        return  button;
    }

    private int convertDpToPixels(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private static String getType(Object variable) {
        return variable.getClass().getSimpleName();
    }





}