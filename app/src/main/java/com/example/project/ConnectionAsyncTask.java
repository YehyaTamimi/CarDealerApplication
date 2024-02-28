package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

public class ConnectionAsyncTask extends AsyncTask<String, String,
        String> {
    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
        ((MainActivity) activity).setButtonText("Connecting");
        super.onPreExecute();
        ((MainActivity) activity).setProgress(true);
    }
    @Override
    protected String doInBackground(String... params) {
        return HttpManager.getData(params[0]);
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        ((MainActivity) activity).setProgress(false);
        if (s != null && !s.isEmpty()) {
            // Connection successful
            List<Cars> element = CarsJsonParser.getObjectFromJson(s);
            ((MainActivity) activity).setButtonText("Connected");
            ((MainActivity)activity).moveToNextPage();
        } else {
            // Connection failed
            Toast.makeText((MainActivity)activity, "Connection failed. Please try again.", Toast.LENGTH_SHORT).show();
            ((MainActivity) activity).setButtonText("Connect");
        }

    }
}