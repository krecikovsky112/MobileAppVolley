package com.example.mobileappvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.Model.Notification;
import com.example.mobileappvolley.ViewHolder.Player.ExerciseViewHolder;
import com.example.mobileappvolley.ViewHolder.Player.NotificationViewHolder;
import com.example.mobileappvolley.fragment.coach.FragmentExercises;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecyclerViewAdapterNotifications extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context context;
    private ArrayList<Notification> notificationArrayList = new ArrayList<>();
    public static int counter;

    public RecyclerViewAdapterNotifications(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Notification> arrayList) {
        this.notificationArrayList.clear();
        this.notificationArrayList.addAll(arrayList);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_player, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
        Notification notification = notificationArrayList.get(position);
        notificationViewHolder.notificationName.setText(notification.getTitle());
        notificationViewHolder.description.setText(notification.getDescription());

        holder.itemView.setOnClickListener(view -> {
            //TODO: Zaimplementować usuwanie na klikniecie notyfikacji
        });
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }
}
