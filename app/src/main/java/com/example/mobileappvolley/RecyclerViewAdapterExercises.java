package com.example.mobileappvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.ViewHolder.Coach.ExerciseViewHolder;
import com.example.mobileappvolley.fragment.coach.ExerciseFragment;

import java.util.ArrayList;

public class RecyclerViewAdapterExercises extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Exercise> exerciseArrayList =  new ArrayList<>();

    public RecyclerViewAdapterExercises(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Exercise> arrayList){
        this.exerciseArrayList.clear();
        this.exerciseArrayList.addAll(arrayList);
    }

    public void addItems(Exercise exercise){
        this.exerciseArrayList.add(exercise);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise , parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExerciseViewHolder exerciseViewHolder = (ExerciseViewHolder) holder;
        Exercise exercise = exerciseArrayList.get(position);
        exerciseViewHolder.exerciseName.setText(exercise.getName());
        StringBuilder temp = new StringBuilder();
        for(int i = 0 ;i<exercise.getType().size();i++)
        {
           temp.append(exercise.getType().get(i));
           temp.append(",");
        }
        exerciseViewHolder.typeTextview.setText(temp);
        if(exercise.isUrgent())
            exerciseViewHolder.alarmImage.setBackgroundResource(R.drawable.ic_baseline_warning_24);
        else
            exerciseViewHolder.alarmImage.setBackgroundResource(R.drawable.ic_baseline_warning_white);


        holder.itemView.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            ExerciseFragment myFragment= ExerciseFragment.newInstance(exercise.getName(),exercise.getNumberRepeat(),exercise.getDecription(),exercise.getType(),exercise.getId(),exercise.isUrgent(),exercise.getOrder());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
        });
    }

    @Override
    public int getItemCount() {
        return exerciseArrayList.size();
    }
}
