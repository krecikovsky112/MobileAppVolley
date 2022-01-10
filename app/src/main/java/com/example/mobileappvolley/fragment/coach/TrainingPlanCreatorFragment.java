package com.example.mobileappvolley.fragment.coach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentTrainingplanCreatorBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class TrainingPlanCreatorFragment extends Fragment {
    private FragmentTrainingplanCreatorBinding fragmentTrainingplanCreatorBinding;
    private FirebaseAuth firebaseAuth;
    ArrayList<Exercise> exercises = new ArrayList<>();
    ArrayList<String> exercisesSpinner = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentTrainingplanCreatorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trainingplan_creator, container, false);
        fragmentTrainingplanCreatorBinding.setCallback(this);
        View view = fragmentTrainingplanCreatorBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        leadData();

        fragmentTrainingplanCreatorBinding.createExercise.setOnClickListener(v -> {
            FragmentExercises myFragment = new FragmentExercises();
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();

        });

        return view;
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Exercises");

        Query query = ref.orderBy("order", Query.Direction.ASCENDING);

        query.addSnapshotListener((value, error) -> {
            exercisesSpinner.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    Exercise exercise = new Exercise();
                    exercise.setId(document.getId());
                    exercise.setName(document.getString("name"));
                    exercise.setNumberRepeat(document.getLong("numberRepeat").intValue());
                    exercise.setDecription(document.getString("description"));
                    exercise.setType((ArrayList<String>) document.get("type"));
                    exercise.setUrgent(document.getBoolean("urgent"));
                    exercise.setOrder(document.getLong("order").intValue());
                    exercises.add(exercise);
                    exercisesSpinner.add(exercise.getName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, exercisesSpinner);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fragmentTrainingplanCreatorBinding.numberSpinner.setAdapter(dataAdapter);
//                fragmentTrainingplanCreatorBinding.numberSpinner.setSelection(dataAdapter.getPosition(exercise.getOrder()));
            }

        });

    }
}
