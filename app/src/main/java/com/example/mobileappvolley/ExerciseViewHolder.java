package com.example.mobileappvolley;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    public TextView exerciseName;
    public TextView typeTextview;
    public ImageView alarmImage;

    public ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        exerciseName = itemView.findViewById(R.id.nameExercise);
        typeTextview = itemView.findViewById(R.id.typeTextView);
        alarmImage = itemView.findViewById(R.id.alarmImage);
    }
}
