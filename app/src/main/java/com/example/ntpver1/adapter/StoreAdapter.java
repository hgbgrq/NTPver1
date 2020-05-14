package com.example.ntpver1.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntpver1.R;
import com.example.ntpver1.item.Store;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    static final String TAG = "StoreAdapter";
    ArrayList<Store> items = new ArrayList<Store>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.store_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Store item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView starTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            starTextView = itemView.findViewById(R.id.starTextView);
        }

        public void setItem(Store item) {
            nameTextView.setText(item.getName());
            phoneTextView.setText(item.getPhone());
            starTextView.setText(Integer.toString(item.getStar()));
        }
    }

    public void setClean() {
        Log.d(TAG, "setClean() called");
        items.clear();
    }

    public void addItem(Store item) {
        items.add(item);
    }

    public void setItems(ArrayList<Store> items) {
        this.items = items;
    }

    public Store getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Store item) {
        items.set(position, item);
    }
}
