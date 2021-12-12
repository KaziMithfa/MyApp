package com.example.myapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;




public class CarViewHolder extends RecyclerView.ViewHolder {

     ImageView fetchImage;
     TextView fetchTextView;
    public CarViewHolder( View itemView) {
        super(itemView);

        fetchImage = itemView.findViewById(R.id.fetchimageViewId);
        fetchTextView = itemView.findViewById(R.id.fetchtextViewId);
    }
}
