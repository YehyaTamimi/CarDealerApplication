package com.example.project;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CarMenuFragement extends Fragment implements CarMenuAdapter.OnReserveButtonClickListener, CarMenuAdapter.OnFavoriteButtonClickListener, CarMenuAdapter.OnLayoutButtonClickListener   {

    private List<Cars> dealerCars;
    private List<Cars> remaining;
    private RecyclerView carMenuRecyclerView;
    private CarMenuAdapter carMenuAdapter;

    private String uid;
    private String dealerId;

    private EditText editTextFilter;
    private Button buttonFilter;

    public CarMenuFragement(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_car_menu_fragement, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
            dealerId = bundle.getString("dealerid");
        }

        dealerCars = CarsJsonParser.carsOG;
        DataBaseHelper db = new DataBaseHelper(getActivity(), "project", null, 1);
        remaining= db.getNotReserved(dealerId);

        if(remaining.isEmpty()){
            remaining = new ArrayList<>(dealerCars);
        }

        carMenuRecyclerView = rootView.findViewById(R.id.CarMenuRecyclerView);
        carMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        carMenuAdapter = new CarMenuAdapter(remaining, this, this, this);
        carMenuRecyclerView.setAdapter(carMenuAdapter);

        editTextFilter = rootView.findViewById(R.id.editTextFilter);
        buttonFilter = rootView.findViewById(R.id.buttonFilter);
        final String originalHint = editTextFilter.getHint().toString();

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the filter text and apply filtering
                String filterText = editTextFilter.getText().toString();
                carMenuAdapter.filter(filterText);
                hideKeyboard(v);
                editTextFilter.setText("");
                editTextFilter.setHint(originalHint);

            }
        });

//        checkFavorite(cid, dealerId)
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    @Override
    public void onReserveButtonClick(int position, List<Cars> filtered) {

        Cars clickedCar = filtered.get(position);
        showReservePopup(clickedCar);

    }

    @Override
    public void onFavoriteButtonClick(int position, List<Cars> filtered) {

        Cars clickedCar = filtered.get(position);
        animateFavoriteButton(carMenuRecyclerView, position);
        addtoFavorites(clickedCar);
    }

    @Override
    public void onLayoutButtonClick(int position, List<Cars> filtered) {

             Cars clickedCar = filtered.get(position);
            CarDetail carDetail = new CarDetail();

            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putString("dealerid", dealerId);
            bundle.putString("id", String.valueOf(clickedCar.getId()));
            bundle.putString("carName", clickedCar.getType());
            bundle.putString("carInfo", clickedCar.getInformation());
            bundle.putString("carState", clickedCar.getState());
            bundle.putString("carDoors", String.valueOf(clickedCar.getNum_doors()));
            bundle.putString("carYear", String.valueOf(clickedCar.getYear()));
            bundle.putString("carPrice", String.valueOf(clickedCar.getPrice()));

            carDetail.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, carDetail);
            transaction.addToBackStack(null);
            transaction.commit();
    }





    private void addtoFavorites(Cars car){
        DataBaseHelper db = new DataBaseHelper(getActivity(), "project", null, 1);
        db.insertFavorite(uid, dealerId, String.valueOf(car.getId()));
        Toast.makeText(requireContext(), "Add To Favorites", Toast.LENGTH_SHORT).show();
    }

    private void animateFavoriteButton(RecyclerView recyclerView, int position) {

        View itemView = recyclerView.getChildAt(position);
        ImageView favoriteButton = itemView.findViewById(R.id.btnAddToFavorites);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(favoriteButton, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(favoriteButton, "scaleY", 1f, 1.5f, 1f);

        scaleAnimatorX.setDuration(500);
        scaleAnimatorY.setDuration(500);

        animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.start();

        int targetColor =  Color.RED;
        favoriteButton.setColorFilter(new PorterDuffColorFilter(targetColor, PorterDuff.Mode.SRC_IN));
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

    private void hideKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}