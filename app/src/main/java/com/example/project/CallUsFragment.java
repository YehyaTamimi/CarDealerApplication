package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CallUsFragment extends Fragment {

    String dealerId;

    Button call;

    Button maps;

    Button mail;

    DataBaseHelper db;

    CarDealer dealer;
    public CallUsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_call_us, container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            dealerId = bundle.getString("dealerid");
        }
        db = new DataBaseHelper(getActivity(), "project", null, 1);
        dealer = db.getDealerInfo(dealerId);

        call = view.findViewById(R.id.btnCall);
        maps = view.findViewById(R.id.btnOpenMaps);
        mail = view.findViewById(R.id.btnOpenGmail);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateCall(v);

            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openGoogleMaps(v);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openGmail(v);
            }
        });

        return view;
    }

        public void initiateCall(View view) {
            String phone = "tel:"+ dealer.getPhoneNumber();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone));
            startActivity(callIntent);
        }

        // Method to open Google Maps
        public void openGoogleMaps(View view) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=car+dealer");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }

        // Method to open Gmail
        public void openGmail(View view) {
            String mail = "mailto:"+dealer.getEmail();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(mail));
            startActivity(emailIntent);
        }

}