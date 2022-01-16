package com.example.mobileappvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.Model.MatchDate;
import com.example.mobileappvolley.ViewHolder.Coach.ItemViewHolder;
import com.example.mobileappvolley.fragment.coach.StatsDisplayFragment;

import java.util.ArrayList;

public class RecyclerViewAdapterHistoryStats extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<MatchDate> statsArrayList =  new ArrayList<>();

    public RecyclerViewAdapterHistoryStats(Context context) {
        this.context = context;
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
        MatchDate matchDate = statsArrayList.get(position);
        matchDate.setNumber(position+1);
        itemViewHolder.name.setText("Match " + matchDate.getNumber());
        itemViewHolder.description.setText(matchDate.getMatchDate().toDate().toString());
        itemViewHolder.image.setBackgroundResource(R.drawable.set);

        holder.itemView.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                StatsDisplayFragment myFragment = StatsDisplayFragment.newInstance(matchDate.getMatchDate().getSeconds());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();

        });
    }

    @Override
    public int getItemCount() {
        return statsArrayList.size();
    }

    public void setItems(ArrayList<MatchDate> matchDateArrayList) {
        this.statsArrayList.addAll(matchDateArrayList);
    }
}
