package com.example.mobileappvolley;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.Model.Statistic;
import com.example.mobileappvolley.ViewHolder.Coach.PlayerViewHolder;
import com.example.mobileappvolley.fragment.coach.PlayerFragment;
import com.example.mobileappvolley.fragment.coach.PlayerStatsFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import javax.xml.parsers.FactoryConfigurationError;

public class RecyclerViewAdapterStats extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Player> playerArrayList = new ArrayList<>();
    private ArrayList<Statistic> statisticArrayList = new ArrayList<>();
    private int selectedPosition = 0;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public RecyclerViewAdapterStats(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Player> arrayList) {
        this.playerArrayList.clear();
        this.playerArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlayerViewHolder playerViewHolder = (PlayerViewHolder) holder;
        Player player = playerArrayList.get(position);
        playerViewHolder.playerTittle.setText(player.getName());
//        playerViewHolder.playerNumber.setText(String.valueOf(player.getId()));
        playerViewHolder.playerPosition.setText(player.getPosition());

        if (selectedPosition == -1) {
            setItemView(playerViewHolder, false);
        } else {
            if (selectedPosition == holder.getAdapterPosition()) {
                setItemView(playerViewHolder, true);
            } else {
                setItemView(playerViewHolder, false);
            }
        }

        String pathURL = "gs://mobileappvolley-16965.appspot.com/" + (position + 1) + ".jpg";
        StorageReference gsReference = storage.getReferenceFromUrl(pathURL);

        GlideApp.with(context)
                .load(gsReference)
                .into(playerViewHolder.photo);

        holder.itemView.setOnClickListener(view -> {
            setItemView(playerViewHolder, true);
            if (selectedPosition != holder.getAdapterPosition()) {
                notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
            }

            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(player.getIdUser());
            if (fragment != null && fragment.getTag().equals(player.getIdUser())) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
            }
        });
    }

    private void setItemView(PlayerViewHolder playerViewHolder, boolean isSelected) {
        if (!isSelected) {
            playerViewHolder.backgroundPlayerViewHolder.setBackgroundColor(Color.parseColor("#17358b"));
//            playerViewHolder.playerNumber.setTextColor(Color.parseColor("#ffffff"));
            playerViewHolder.playerTittle.setTextColor(Color.parseColor("#ffffff"));
            playerViewHolder.playerPosition.setTextColor(Color.parseColor("#ffffff"));
//            playerViewHolder.shirtImageView.setColorFilter(Color.parseColor("#ffffff"));
        } else {
            playerViewHolder.backgroundPlayerViewHolder.setBackgroundColor(Color.parseColor("#fdbc00"));
//            playerViewHolder.playerNumber.setTextColor(Color.parseColor("#17358b"));
            playerViewHolder.playerTittle.setTextColor(Color.parseColor("#17358b"));
            playerViewHolder.playerPosition.setTextColor(Color.parseColor("#17358b"));
//            playerViewHolder.shirtImageView.setColorFilter(Color.parseColor("#17358b"));
        }

    }


    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }
}
