package com.example.mobileappvolley.fragment.player;

import android.content.Intent;
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
import com.example.mobileappvolley.Model.TrainingPlan;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterExercisesPlayer;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentExercisesPlayerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FragmentTrainingPlans extends Fragment {
    private FragmentExercisesPlayerBinding fragmentExercisesPlayerBinding;
    private FirebaseAuth firebaseAuth;
    private RecyclerViewAdapterExercisesPlayer recyclerViewAdapterExercisesPlayer;
    ArrayList<TrainingPlan> trainingPlans = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentExercisesPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercises_player, container, false);
        fragmentExercisesPlayerBinding.setCallback(this);
        View view = fragmentExercisesPlayerBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();

        fragmentExercisesPlayerBinding.exercisesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        fragmentExercisesPlayerBinding.exercisesRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapterExercisesPlayer = new RecyclerViewAdapterExercisesPlayer(this.getContext());
        fragmentExercisesPlayerBinding.exercisesRecyclerView.setAdapter(recyclerViewAdapterExercisesPlayer);
        leadData();
        return view;
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference ref = db.collection("TrainingPlan");

        Query query = ref.orderBy("dateTime", Query.Direction.ASCENDING);

        query.addSnapshotListener((value, error) -> {
            trainingPlans.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    TrainingPlan trainingPlan = new TrainingPlan();
                    trainingPlan.setId(document.getId());
                    trainingPlan.setName(document.getString("name"));
                    trainingPlan.setDateTime(document.getTimestamp("dateTime"));
                    trainingPlan.setIdExercises((ArrayList<String>)document.get("idExercises"));
                    trainingPlans.add(trainingPlan);
                }
                recyclerViewAdapterExercisesPlayer.setItems(trainingPlans);
                recyclerViewAdapterExercisesPlayer.notifyDataSetChanged();
            }

        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getActivity(), AuthActivity.class));
        }
    }
}
