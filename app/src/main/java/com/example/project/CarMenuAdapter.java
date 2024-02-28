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

public class CarMenuAdapter extends RecyclerView.Adapter<CarMenuAdapter.CarViewAdapter> {

    List<Cars> dealerCars;
    private static List<Cars> filteredCars;
    //added
    public interface OnReserveButtonClickListener {
        void onReserveButtonClick(int position, List<Cars> filtered);
    }

    public interface OnFavoriteButtonClickListener {
        void onFavoriteButtonClick(int position, List<Cars> filtered);
    }

    public interface OnLayoutButtonClickListener {
        void onLayoutButtonClick(int position, List<Cars> filtered);
    }
    private static OnReserveButtonClickListener reserveButtonClickListener;
    private static OnFavoriteButtonClickListener FavoriteButtonClickListener;
    private static OnLayoutButtonClickListener LayoutButtonClickListener;


    public CarMenuAdapter(List<Cars> dealerCars, OnReserveButtonClickListener Reservelistener, OnFavoriteButtonClickListener favoriteListner, OnLayoutButtonClickListener layoutListner) {
        this.dealerCars = dealerCars;
        this.reserveButtonClickListener = Reservelistener;
        this.FavoriteButtonClickListener = favoriteListner;
        this.LayoutButtonClickListener = layoutListner;
        this.filteredCars = new ArrayList<>(dealerCars);
    }

    public void filter(String searchText) {
        filteredCars.clear();
        filteredCars.addAll(dealerCars);
        if (searchText.isEmpty()) {
            filteredCars.addAll(dealerCars);
        } else {
            filteredCars.clear();
            searchText = searchText.toLowerCase();
            for (Cars car : dealerCars) {
                if (car.getType().toLowerCase().contains(searchText) ||
                        String.valueOf(car.getYear()).contains(searchText) ||
                        String.valueOf(car.getPrice()).contains(searchText) ||
                        car.getInformation().toLowerCase().contains(searchText) ||
                        car.getState().toLowerCase().contains(searchText)) {
                    filteredCars.add(car);
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter that the data set has changed
    }

    public void removeReservedCar(Cars reservedCar) {
        dealerCars.remove(reservedCar);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_item, parent, false);
        return new CarViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewAdapter holder, int position) {
        Cars car = filteredCars.get(position);
        holder.carName.setText(car.getType());
        holder.carYear.setText(String.valueOf(car.getYear()));
    }

    @Override
    public int getItemCount() {
        return filteredCars.size();
    }

    public static class CarViewAdapter extends RecyclerView.ViewHolder{
        TextView carName;
        TextView carYear;
        ImageView favorite;
        Button reserve;
        LinearLayout item;



        public CarViewAdapter(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.textCarName);
            carYear = itemView.findViewById(R.id.textCarYear);
            favorite = itemView.findViewById(R.id.btnAddToFavorites);
            reserve = itemView.findViewById(R.id.btnReserve);
            item = itemView.findViewById(R.id.itemLayout);

            reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reserveButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            reserveButtonClickListener.onReserveButtonClick(position, filteredCars);
                        }
                    }
                }
            });

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("FavoriteButton", "Clicked");
                    if (FavoriteButtonClickListener != null) {
                        Log.d("FavoriteButton1", "Clicked");
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Log.d("FavoriteButton2", "Clicked");
                            FavoriteButtonClickListener.onFavoriteButtonClick(position, filteredCars);
                        }
                    }
                    Log.d("FavoriteButton3", "Clicked");
                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LayoutButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            LayoutButtonClickListener.onLayoutButtonClick(position, filteredCars);
                        }
                    }
                }
            });
        }
    }
}
