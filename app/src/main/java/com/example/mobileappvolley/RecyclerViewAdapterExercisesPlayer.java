package com.example.mobileappvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.Model.TrainingPlan;
import com.example.mobileappvolley.ViewHolder.Player.ExerciseViewHolder;

import java.util.ArrayList;

public class RecyclerViewAdapterExercisesPlayer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<TrainingPlan> trainingPlanArrayList = new ArrayList<>();

    public RecyclerViewAdapterExercisesPlayer(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<TrainingPlan> arrayList) {
        this.trainingPlanArrayList.clear();
        this.trainingPlanArrayList.addAll(arrayList);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise_player, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExerciseViewHolder exerciseViewHolder = (ExerciseViewHolder) holder;
        TrainingPlan trainingPlan = trainingPlanArrayList.get(position);
        exerciseViewHolder.exerciseName.setText(trainingPlan.getName());
        exerciseViewHolder.description.setText(trainingPlan.getDateTime().toDate().toString());

        holder.itemView.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return trainingPlanArrayList.size();
    }
}
