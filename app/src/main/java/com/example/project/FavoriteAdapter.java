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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewAdapter> {

    static List<Cars> dealerCars;

    //added
    public interface OnReserveButtonClickListener {
        void onReserveButtonClick(int position, List<Cars> filtered);
    }

    public interface OnLayoutButtonClickListener {
        void onLayoutButtonClick(int position);
    }

    private static OnReserveButtonClickListener reserveButtonClickListener;
    private static OnLayoutButtonClickListener LayoutButtonClickListener;


    public FavoriteAdapter(List<Cars> dealerCars, OnReserveButtonClickListener Reservelistener, OnLayoutButtonClickListener layoutListner) {
        this.dealerCars = dealerCars;
        this.reserveButtonClickListener = Reservelistener;
        this.LayoutButtonClickListener = layoutListner;
    }



    @NonNull
    @Override
    public FavoriteViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_favorite_item, parent, false);
        return new FavoriteViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewAdapter holder, int position) {
        Cars car = dealerCars.get(position);
        holder.carName.setText(car.getType());
        holder.carYear.setText(String.valueOf(car.getYear()));


    }

    @Override
    public int getItemCount() {
        return dealerCars.size();
    }

    public static class FavoriteViewAdapter extends RecyclerView.ViewHolder{
        TextView carName;
        TextView carYear;
        Button reserve;
        LinearLayout item;


        public FavoriteViewAdapter(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.textCarName);
            carYear = itemView.findViewById(R.id.textCarYear);
            reserve = itemView.findViewById(R.id.btnReserve);
            item = itemView.findViewById(R.id.itemLayout);

            reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reserveButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            reserveButtonClickListener.onReserveButtonClick(position, dealerCars);
                        }
                    }
                }
            });



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