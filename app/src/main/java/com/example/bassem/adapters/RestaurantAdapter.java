package com.example.bassem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bassem.R;
import com.example.bassem.data.FireBaseServices;
import com.example.bassem.data.Restaurant;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    Context context;
    ArrayList<Restaurant> restList;
    private FireBaseServices fbs;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restList) {
        this.context = context;
        this.restList = restList;
        this.fbs = FireBaseServices.getInstance();
    }

    @NonNull
    @Override
    public RestaurantAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rest_item, parent, false);
        return new RestaurantAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.MyViewHolder holder, int position) {
        Restaurant rest = restList.get(position);
        holder.tvName.setText(rest.getName());
        holder.tvPhone.setText(rest.getPhone());
    }

    @Override
    public int getItemCount() {
        return restList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameRestItem);
            tvPhone = itemView.findViewById(R.id.tvPhoneRestItem);

        }
    }
}