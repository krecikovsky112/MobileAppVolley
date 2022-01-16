package com.example.mobileappvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.Model.TrainingPlan;
import com.example.mobileappvolley.ViewHolder.Coach.ItemViewHolder;
import com.example.mobileappvolley.fragment.coach.ExerciseFragment;
import com.example.mobileappvolley.fragment.coach.FragmentTrainingPlan;

import java.util.ArrayList;

public class RecyclerViewAdapterTrainingPlans extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<TrainingPlan> trainingPlanArrayList =  new ArrayList<>();

    public RecyclerViewAdapterTrainingPlans(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<TrainingPlan> arrayList){
        this.trainingPlanArrayList.clear();
        this.trainingPlanArrayList.addAll(arrayList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise , parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        TrainingPlan trainingPlan = trainingPlanArrayList.get(position);
        itemViewHolder.name.setText(trainingPlan.getName());
        itemViewHolder.description.setText(trainingPlan.getDateTime().toDate().toString());
        itemViewHolder.image.setBackgroundResource(R.drawable.clipboard);

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            FragmentTrainingPlan myFragment= FragmentTrainingPlan.newInstance(trainingPlan.getId(),trainingPlan.getDateTime().toDate().toString(),trainingPlan.getName(),trainingPlan.getIdExercises());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
        });
    }

    @Override
    public int getItemCount() {
        return trainingPlanArrayList.size();
    }
}
