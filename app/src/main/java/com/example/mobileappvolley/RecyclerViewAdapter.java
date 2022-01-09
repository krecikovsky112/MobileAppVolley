package com.example.mobileappvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.ViewHolder.Coach.PlayerViewHolder;
import com.example.mobileappvolley.fragment.coach.PlayerFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context context;
    private ArrayList<Player> playerArrayList =  new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Player> arrayList){
        this.playerArrayList.clear();
        this.playerArrayList.addAll(arrayList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_player , parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlayerViewHolder playerViewHolder = (PlayerViewHolder) holder;
        Player player = playerArrayList.get(position);
        playerViewHolder.playerTittle.setText(player.getName());
//        playerViewHolder.playerNumber.setText(String.valueOf(player.getId()));
        playerViewHolder.playerPosition.setText(player.getPosition());
            StorageReference storageReference = storageRef.child("Snapchat-411991285.jpg");

            //TODO: do poprawienia implementacja bo nie działa
        Glide.with(context)
                .load(storageReference)
                .into(playerViewHolder.photo);

        holder.itemView.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            PlayerFragment myFragment= PlayerFragment.newInstance(player.getName(), String.valueOf(player.getId()),player.getPosition(),String.valueOf(player.getBlockRange()),String.valueOf(player.getAttackRange()),String.valueOf(player.getHeight()),String.valueOf(player.getWeight()),String.valueOf(player.getAge()),player.getIdUser());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
        });
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }


}
