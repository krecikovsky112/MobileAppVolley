package com.example.mobileappvolley.ViewHolder.Coach;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.R;

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    public TextView playerTittle;
    public TextView playerNumber;
    public TextView playerPosition;
    public LinearLayout backgroundPlayerViewHolder;
    public ImageView shirtImageView;

    public PlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        playerTittle = itemView.findViewById(R.id.playerTittle);
        playerNumber = itemView.findViewById(R.id.number);
        playerPosition = itemView.findViewById(R.id.playerPosition);
        backgroundPlayerViewHolder = itemView.findViewById(R.id.backgroundPlayerViewHolder);
        shirtImageView = itemView.findViewById(R.id.shirtImageView);
    }

}
