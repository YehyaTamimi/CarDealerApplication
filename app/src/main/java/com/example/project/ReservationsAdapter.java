package com.example.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationsViewAdapter> {

    static List<Cars> dealerCars;

    //added
    public interface OnLayoutButtonClickListener {
        void onLayoutButtonClick(int position);
    }

    private static OnLayoutButtonClickListener LayoutButtonClickListener;


    public ReservationsAdapter(List<Cars> dealerCars, OnLayoutButtonClickListener layoutListner) {
        this.dealerCars = dealerCars;
        this.LayoutButtonClickListener = layoutListner;
    }



    @NonNull
    @Override
    public ReservationsViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_reserved_item, parent, false);
        return new ReservationsViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationsViewAdapter holder, int position) {
        Cars car = dealerCars.get(position);
        holder.carName.setText(car.getType());
        holder.carYear.setText(String.valueOf(car.getYear()));
        holder.reserveDate.setText(car.getReserveDate());


    }

    @Override
    public int getItemCount() {
        return dealerCars.size();
    }

    public static class ReservationsViewAdapter extends RecyclerView.ViewHolder{
        TextView carName;
        TextView carYear;

        TextView reserveDate;
        LinearLayout item;


        public ReservationsViewAdapter(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.textCarName);
            carYear = itemView.findViewById(R.id.textCarYear);
            reserveDate = itemView.findViewById(R.id.textReserveDate);
            item = itemView.findViewById(R.id.itemLayout);


            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LayoutButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            LayoutButtonClickListener.onLayoutButtonClick(position);
                        }
                    }
                }
            });
        }
    }
}