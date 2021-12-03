package com.example.mobileappvolley.fragment.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.Model.Notification;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterExercises;
import com.example.mobileappvolley.RecyclerViewAdapterNotifications;
import com.example.mobileappvolley.databinding.FragmentNotificationsPlayerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.NotActiveException;
import java.util.ArrayList;

public class FragmentNotificationsPlayer extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FragmentNotificationsPlayerBinding fragmentNotificationsPlayerBinding;
    private RecyclerViewAdapterNotifications recyclerViewAdapterNotifications;
    ArrayList<Notification> notificationArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNotificationsPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications_player, container, false);
        fragmentNotificationsPlayerBinding.setCallback(this);
        View view = fragmentNotificationsPlayerBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        fragmentNotificationsPlayerBinding.exercisesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        fragmentNotificationsPlayerBinding.exercisesRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapterNotifications = new RecyclerViewAdapterNotifications(this.getContext());
        fragmentNotificationsPlayerBinding.exercisesRecyclerView.setAdapter(recyclerViewAdapterNotifications);
        leadData();

        return view;
    }

    private void leadData() {
        String id = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Notifications");

        Query query = ref.orderBy("time", Query.Direction.ASCENDING);

        query.addSnapshotListener((value, error) -> {
            notificationArrayList.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    Notification notification = new Notification();
                    notification.setId(document.getString("id"));
                    notification.setTitle(document.getString("title"));
                    notification.setDescription(document.getString("description"));
                    notification.setTime(document.getTimestamp("time"));
                    if(notification.getId().equals("all") || notification.getId().equals(id)){
                        notificationArrayList.add(notification);
                    }
                }
                recyclerViewAdapterNotifications.setItems(notificationArrayList);
                recyclerViewAdapterNotifications.notifyDataSetChanged();
            }

        });
    }
}
