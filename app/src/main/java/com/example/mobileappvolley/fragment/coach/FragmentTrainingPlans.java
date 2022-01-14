package com.example.mobileappvolley.fragment.coach;

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
import com.example.mobileappvolley.RecyclerViewAdapterExercises;
import com.example.mobileappvolley.RecyclerViewAdapterTrainingPlans;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentExercisesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentTrainingPlans extends Fragment {
    private FragmentExercisesBinding fragmentExercisesBinding;
    private FirebaseAuth firebaseAuth;
    private RecyclerViewAdapterTrainingPlans recyclerViewAdapterTrainingPlans;
    ArrayList<TrainingPlan> trainingPlans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentExercisesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercises, container, false);
        fragmentExercisesBinding.setCallback(this);
        View view = fragmentExercisesBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();

        fragmentExercisesBinding.exercisesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        fragmentExercisesBinding.exercisesRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapterTrainingPlans = new RecyclerViewAdapterTrainingPlans(this.getContext());
        fragmentExercisesBinding.exercisesRecyclerView.setAdapter(recyclerViewAdapterTrainingPlans);

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
                    trainingPlan.setName(document.getString("name"));
                    trainingPlan.setDateTime(document.getTimestamp("dateTime"));
                    trainingPlan.setIdExercises((ArrayList<String>)document.get("idExercises"));
                    trainingPlans.add(trainingPlan);
                }
                recyclerViewAdapterTrainingPlans.setItems(trainingPlans);
                recyclerViewAdapterTrainingPlans.notifyDataSetChanged();
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
