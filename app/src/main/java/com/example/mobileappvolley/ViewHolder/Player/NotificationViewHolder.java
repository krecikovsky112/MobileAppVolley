package com.example.mobileappvolley.ViewHolder.Player;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public TextView notificationName;
    public TextView description;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        notificationName = itemView.findViewById(R.id.notificationTitle);
        description = itemView.findViewById(R.id.notificationDescription);
    }
}
