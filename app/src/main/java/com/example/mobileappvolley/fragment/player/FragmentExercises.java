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

import com.example.mobileappvolley.Exercise;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterExercises;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentExercisesBinding;
import com.example.mobileappvolley.databinding.FragmentExercisesPlayerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FragmentExercises extends Fragment {
    private FragmentExercisesPlayerBinding fragmentExercisesBinding;
    private FirebaseAuth firebaseAuth;
    private RecyclerViewAdapterExercises recyclerViewAdapterExercises;
    ArrayList<Exercise> exercises = new ArrayList<>();

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
        recyclerViewAdapterExercises = new RecyclerViewAdapterExercises(this.getContext());
        fragmentExercisesBinding.exercisesRecyclerView.setAdapter(recyclerViewAdapterExercises);
        leadData();
        return view;
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Exercises");

        Query query = ref;

        query.addSnapshotListener((value, error) -> {
            exercises.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    Exercise exercise = new Exercise();
                    exercise.setId(document.getId());
                    exercise.setName(document.getString("name"));
                    exercise.setNumberRepeat(document.getLong("numberRepeat").intValue());
                    exercise.setDecription(document.getString("description"));
                    exercise.setType((ArrayList<String>) document.get("type"));
                    exercise.setUrgent(document.getBoolean("urgent"));
                    exercises.add(exercise);
                }
                recyclerViewAdapterExercises.setItems(exercises);
                recyclerViewAdapterExercises.notifyDataSetChanged();
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
