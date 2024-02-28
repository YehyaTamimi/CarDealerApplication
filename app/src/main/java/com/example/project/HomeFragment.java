package com.example.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    String dealerId;
    TextView name;
    TextView info;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            dealerId = bundle.getString("dealerid");

        }

        name = view.findViewById(R.id.name);
        info = view.findViewById(R.id.info);

        DataBaseHelper db = new DataBaseHelper(getActivity(), "project", null, 1);
        CarDealer dealer = db.getDealerById(dealerId);

        name.setText(dealer.getDealerName());
        info.setText(dealer.getDealerInformation());

        return view;
    }
}