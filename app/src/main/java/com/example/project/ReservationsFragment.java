package com.example.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class ReservationsFragment extends Fragment implements ReservationsAdapter.OnLayoutButtonClickListener {
    String uid;
    String dealerId;
    private List<Cars> dealerCars;
    private List<Cars> reservedCars;
    private RecyclerView reservationRecyclerView;
    private ReservationsAdapter reservationsAdapter;
    public ReservationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reservations, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
            dealerId = bundle.getString("dealerid");
        }

        reservationRecyclerView = rootView.findViewById(R.id.ReservedRecyclerView);
        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DataBaseHelper db = new DataBaseHelper(getActivity(), "project", null, 1);
        reservedCars = db.getReserved(uid, dealerId);

        reservationsAdapter = new ReservationsAdapter(reservedCars, this);
        reservationRecyclerView.setAdapter(reservationsAdapter);

        return rootView;
    }

    @Override
    public void onLayoutButtonClick(int position) {

    }
}