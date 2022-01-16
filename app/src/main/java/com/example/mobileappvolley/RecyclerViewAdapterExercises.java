package com.example.mobileappvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.ViewHolder.Coach.ItemViewHolder;
import com.example.mobileappvolley.fragment.coach.ExerciseFragment;

import java.util.ArrayList;

public class RecyclerViewAdapterExercises extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    public ArrayList<Exercise> getExerciseArrayList() {
        return exerciseArrayList;
    }

    private ArrayList<Exercise> exerciseArrayList =  new ArrayList<>();

    public RecyclerViewAdapterExercises(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Exercise> arrayList){
        this.exerciseArrayList.clear();
        this.exerciseArrayList.addAll(arrayList);
    }

    public void addItem(Exercise exercise){
        this.exerciseArrayList.add(exercise);
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
        Exercise exercise = exerciseArrayList.get(position);
        itemViewHolder.name.setText(exercise.getName());
        StringBuilder temp = new StringBuilder();
        for(int i = 0 ;i<exercise.getType().size();i++)
        {
            temp.append("#");
           temp.append(exercise.getType().get(i));
           temp.append(" ");

        }
        itemViewHolder.description.setText(temp);
        itemViewHolder.image.setBackgroundResource(R.drawable.cancel);
        itemViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseArrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            ExerciseFragment myFragment= ExerciseFragment.newInstance(exercise.getName(),exercise.getNumberRepeat(),exercise.getDecription(),exercise.getType(),exercise.getId());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
        });
    }

    @Override
    public int getItemCount() {
        return exerciseArrayList.size();
    }
}
