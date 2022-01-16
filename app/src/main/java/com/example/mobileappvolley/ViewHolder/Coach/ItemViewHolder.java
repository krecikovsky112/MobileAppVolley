package com.example.mobileappvolley.ViewHolder.Coach;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView description;
//    public ImageView image;
    public AppCompatImageButton image;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nameItem);
        description = itemView.findViewById(R.id.descriptionItem);
//        image = itemView.findViewById(R.id.imageItem);
        image = itemView.findViewById(R.id.deleteImage);
    }
}
