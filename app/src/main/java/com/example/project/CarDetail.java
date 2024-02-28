package com.example.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CarDetail extends Fragment {

    TextView carName;
    TextView carInfo;
    TextView carPrice;
    TextView carDoors;
    TextView carState;
    TextView carYear;
    Button btnReserve;
    Button btnBack;

    String name;
    String info;
    String price;
    String doors;
    String state;
    String year;
    String userid;
    String dealerId;
    String id;

    public CarDetail() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_car_detail, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userid = bundle.getString("uid");
            dealerId = bundle.getString("dealerid");
            name = "Car Name: " + bundle.getString("carName") ;
            info =  "Car Information: " +bundle.getString("carInfo");
            price =  "Car Price: " +bundle.getString("carPrice");
            doors =  "Car Door Count: " +bundle.getString("carDoors");
            state =  "Car State: " +bundle.getString("carState");
            year =  "Car Year: " +bundle.getString("carYear");
            id = bundle.getString("id");
        }

        carName = view.findViewById(R.id.carName);
        carInfo = view.findViewById(R.id.carInfo);
        carDoors = view.findViewById(R.id.carDoors);
        carPrice = view.findViewById(R.id.carPrice);
        carYear = view.findViewById(R.id.carYear);
        carState = view.findViewById(R.id.carState);
        btnReserve = view.findViewById(R.id.btnReserve);
        btnBack = view.findViewById(R.id.btnBack);

        carName.setText(name);
        carInfo.setText(info);
        carDoors.setText(doors);
        carPrice.setText(price);
        carState.setText(state);
        carYear.setText(year);

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper db = new DataBaseHelper(getActivity(), "project", null, 1);
                db.reserveCar(userid, dealerId, id);
                Toast.makeText(requireContext(), "Reserved Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    fragmentManager.popBackStack();
                }
            }
        });




        return view;
    }
}