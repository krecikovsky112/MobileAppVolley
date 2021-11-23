package com.example.mobileappvolley.ViewHolder.Coach;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.R;

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    public TextView playerTittle;
    public TextView playerNumber;
    public TextView playerPosition;

    public PlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        playerTittle = itemView.findViewById(R.id.playerTittle);
        playerNumber = itemView.findViewById(R.id.number);
        playerPosition = itemView.findViewById(R.id.playerPosition);
    }
}
