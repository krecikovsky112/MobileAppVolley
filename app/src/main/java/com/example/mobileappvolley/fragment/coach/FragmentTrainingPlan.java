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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.Model.TrainingPlan;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterExercises;
import com.example.mobileappvolley.databinding.FragmentTrainingPlanBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FragmentTrainingPlan extends Fragment {
    private FragmentTrainingPlanBinding fragmentTrainingPlanBinding;
    private FirebaseAuth firebaseAuth;
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "dateTime";
    private static final String ARG_PARAM3 = "name";
    private static final String ARG_PARAM4 = "idExercises";
    private final TrainingPlan trainingPlan = new TrainingPlan();
    private String dateTime;
    private RecyclerViewAdapterExercises recyclerViewAdapterExercises;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentTrainingPlanBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_training_plan, container, false);
        fragmentTrainingPlanBinding.setCallback(this);
        View view = fragmentTrainingPlanBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        fragmentTrainingPlanBinding.tittleTextView.setText(trainingPlan.getName());
        fragmentTrainingPlanBinding.dateTimeTextView.setText(dateTime);

        fragmentTrainingPlanBinding.exercisesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        fragmentTrainingPlanBinding.exercisesRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapterExercises = new RecyclerViewAdapterExercises(this.getContext());
        fragmentTrainingPlanBinding.exercisesRecyclerView.setAdapter(recyclerViewAdapterExercises);

        leadData();

        fragmentTrainingPlanBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("TrainingPlan").document(trainingPlan.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("DocumentSnapshot successfully deleted!");
                        FragmentTrainingPlans myFragment = new FragmentTrainingPlans();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
                    }
                }).addOnFailureListener(e -> System.out.println("Error deleting document" + e));
            }
        });

        return view;
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Exercises");

        for(int i = 0;i<trainingPlan.getIdExercises().size();i++){
            ref.document(trainingPlan.getIdExercises().get(i)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error == null){
                        Exercise exercise = new Exercise();
                        exercise.setId(value.getId());
                        exercise.setName(value.getString("name"));
                        exercise.setNumberRepeat(value.getLong("numberRepeat").intValue());
                        exercise.setDecription(value.getString("description"));
                        exercise.setType((ArrayList<String>) value.get("type"));
                        recyclerViewAdapterExercises.addItem(exercise);
                        recyclerViewAdapterExercises.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    public static FragmentTrainingPlan newInstance(String param1, String param2, String param3, ArrayList<String> param4) {
        FragmentTrainingPlan fragment = new FragmentTrainingPlan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putStringArrayList(ARG_PARAM4, param4);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trainingPlan.setId(getArguments().getString(ARG_PARAM1));
            dateTime = getArguments().getString(ARG_PARAM2);
            trainingPlan.setName(getArguments().getString(ARG_PARAM3));
            trainingPlan.setIdExercises(getArguments().getStringArrayList(ARG_PARAM4));
        }
    }
}
