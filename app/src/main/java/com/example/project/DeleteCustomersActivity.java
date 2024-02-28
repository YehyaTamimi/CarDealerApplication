package com.example.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class DeleteCustomersActivity extends AppCompatActivity {

    private ListView usersListView;
    private List<String> usersList;
    private ArrayAdapter<String> adapter;
    private DataBaseHelper db;
    private String dealerId;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "User Deletion Notifications";
            String description = "Notifications for when a user is deleted";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("delete_notification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_customers);
        createNotificationChannel();

        Intent intent = getIntent();
        dealerId = intent.getStringExtra("dealerid");

        usersListView = findViewById(R.id.usersListView);
        usersList = new ArrayList<>();
        db = new DataBaseHelper(this, "project", null, 1);

        List<User> users = db.getAllUsers(dealerId);
        for (User user : users) {
            String userInfo = user.getId() + ", " + user.getFirstName() + " " + user.getLastName();
            usersList.add(userInfo);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        usersListView.setAdapter(adapter);

        usersListView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = usersList.get(position);
            int userId = Integer.parseInt(selected.split(",")[0].trim());

            confirmAndDeleteUser(userId, position);
        });
    }

    private void confirmAndDeleteUser(int userId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteCustomersActivity.this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this user?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            String userName = usersList.get(position).split(",")[1].trim();
            db.deleteUser(String.valueOf(userId), dealerId);

            usersList.remove(position);
            adapter.notifyDataSetChanged();
            sendDeleteNotification(userName);

            Toast.makeText(DeleteCustomersActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendDeleteNotification(String userName) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Notification notification = new NotificationCompat.Builder(this, "delete_notification")
                .setSmallIcon(android.R.drawable.ic_dialog_alert) // Using a default system icon
                .setContentTitle("Customer Deleted")
                .setContentText(userName + " deleted")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, notification);
    }
}
