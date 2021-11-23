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
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterExercises;
import com.example.mobileappvolley.RecyclerViewAdapterExercisesPlayer;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentExercisesPlayerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FragmentExercises extends Fragment {
    private static final String ARG_PARAM1 = "position";
    private FragmentExercisesPlayerBinding fragmentExercisesPlayerBinding;
    private FirebaseAuth firebaseAuth;
    private RecyclerViewAdapterExercisesPlayer recyclerViewAdapterExercisesPlayer;
    ArrayList<Exercise> exercises = new ArrayList<>();
    private String positionPlayer;

    public static FragmentExercises newInstance(String param1){
        FragmentExercises fragment = new FragmentExercises();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            positionPlayer = getArguments().getString(ARG_PARAM1);
        }
    }

    //TODO: Do zrobienia po stronie playera
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

        Query query = db.collection("Exercises");


        query.addSnapshotListener((value, error) -> {
            exercises.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    ArrayList<String> positions = (ArrayList<String>)document.get("type");
                    for(int i = 0;i<positions.size();i++){
                        if(positions.get(i).equals(positionPlayer)){
                            Exercise exercise = new Exercise();
                            exercise.setId(document.getId());
                            exercise.setName(document.getString("name"));
                            exercise.setNumberRepeat(document.getLong("numberRepeat").intValue());
                            exercise.setDecription(document.getString("description"));
                            exercise.setType(positions);
                            exercise.setUrgent(document.getBoolean("urgent"));
                            exercises.add(exercise);
                        }
                    }
                }
                RecyclerViewAdapterExercisesPlayer.counter = 1;
                recyclerViewAdapterExercisesPlayer.setItems(exercises);
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
