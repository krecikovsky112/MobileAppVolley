package com.example.mobileappvolley.ViewHolder.Player;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.R;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    public TextView exerciseName;
    public TextView number;
    public TextView description;

    public ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        exerciseName = itemView.findViewById(R.id.nameExercise);
        number = itemView.findViewById(R.id.number);
        description = itemView.findViewById(R.id.descriptionTextView);
    }
}
