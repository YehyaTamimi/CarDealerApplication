package com.example.project;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class FavoritesFragment extends Fragment implements FavoriteAdapter.OnReserveButtonClickListener,  FavoriteAdapter.OnLayoutButtonClickListener {

    String uid;
    String dealerId;
    private List<Cars> dealerCars;
    private List<Cars> favoriteCars;
    private RecyclerView favoriteRecyclerView;
    private FavoriteAdapter favoriteAdapter;
    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
            dealerId = bundle.getString("dealerid");
        }

        favoriteRecyclerView = rootView.findViewById(R.id.FavoriteRecyclerView);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DataBaseHelper db = new DataBaseHelper(getActivity(), "project", null, 1);
        favoriteCars = db.getFavorite(uid, dealerId);

        favoriteAdapter = new FavoriteAdapter(favoriteCars, this, this);
        favoriteRecyclerView.setAdapter(favoriteAdapter);

        return rootView;
    }

    @Override
    public void onReserveButtonClick(int position, List<Cars> favorite) {

        Cars car = favorite.get(position);
        showReservePopup(car);
    }

    @Override
    public void onLayoutButtonClick(int position) {

    }

    private void showReservePopup(Cars car) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Reserve Car?\n");
        builder.setMessage(" Car Name: " + car.getType() + "\n\n Car Information: " + car.getInformation() + "\n\n Car Price: " + car.getPrice() + "\n\n Car Door Count: " + car.getNum_doors() + "\n\n Car Year: " + car.getYear() + "\n\n Car Status: " + car.getState() );

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DataBaseHelper db = new DataBaseHelper(getActivity(), "project", null, 1);
                db.reserveCar(uid, dealerId, String.valueOf(car.getId()));

                Toast.makeText(requireContext(), "Reservation confirmed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}