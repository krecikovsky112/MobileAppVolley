package com.example.mobileappvolley.fragment.coach;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentExerciseBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExerciseFragment extends Fragment {

    private FragmentExerciseBinding fragmentExerciseBinding;
    private FirebaseAuth firebaseAuth;
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "numberRepeat";
    private static final String ARG_PARAM3 = "description";
    private static final String ARG_PARAM4 = "type";
    private static final String ARG_PARAM5 = "id";
    private final Exercise exercise = new Exercise();
    ArrayList<Integer> numbers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentExerciseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise, container, false);
        fragmentExerciseBinding.setCallback(this);
        View view = fragmentExerciseBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();

        fragmentExerciseBinding.tittleTextView.setText(exercise.getName());

        fragmentExerciseBinding.numberOfRepeatTextView.setText("Number of repeat: " + String.valueOf(exercise.getNumberRepeat()));
        fragmentExerciseBinding.descriptionTextView.setText("Description: " + exercise.getDecription());

        setCheckboxes();

        return view;
    }

    private void setCheckboxes() {
        for(int i = 0 ;i<exercise.getType().size();i++){
            if(exercise.getType().get(i).equals("Receiver")){
                fragmentExerciseBinding.checkboxReceiver.setChecked(true);
            }
            if(exercise.getType().get(i).equals("Libero")){
                fragmentExerciseBinding.checkboxLibero.setChecked(true);
            }
            if(exercise.getType().get(i).equals("Setter")){
                fragmentExerciseBinding.checkboxSetter.setChecked(true);
            }
            if(exercise.getType().get(i).equals("Middle blocker")){
                fragmentExerciseBinding.checkboxMiddleBlocker.setChecked(true);
            }
            if(exercise.getType().get(i).equals("Spiker")){
                fragmentExerciseBinding.checkboxSpiker.setChecked(true);
            }
        }
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getActivity(), AuthActivity.class));
        }
    }

    public static ExerciseFragment newInstance(String param1, int param2, String param3, ArrayList<String> param4,String param5){
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putStringArrayList(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exercise.setName(getArguments().getString(ARG_PARAM1));
            exercise.setNumberRepeat(getArguments().getInt(ARG_PARAM2));
            exercise.setDecription(getArguments().getString(ARG_PARAM3));
            exercise.setType(getArguments().getStringArrayList(ARG_PARAM4));
            exercise.setId(getArguments().getString(ARG_PARAM5));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public void onDeleteClick(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Exercises").document(exercise.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("DocumentSnapshot successfully deleted!");
                FragmentTrainingPlans myFragment = new FragmentTrainingPlans();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
            }
        }).addOnFailureListener(e -> System.out.println("Error deleting document" + e));
    }

    public void onSaveClick(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> exerciseE = new HashMap<>();
        ArrayList<String> type = new ArrayList<>();
        if (fragmentExerciseBinding.checkboxReceiver.isChecked())
            type.add("Receiver");
        if (fragmentExerciseBinding.checkboxLibero.isChecked())
            type.add("Libero");
        if (fragmentExerciseBinding.checkboxSetter.isChecked())
            type.add("Setter");
        if (fragmentExerciseBinding.checkboxMiddleBlocker.isChecked())
            type.add("Middle blocker");
        if (fragmentExerciseBinding.checkboxSpiker.isChecked())
            type.add("Spiker");
        exerciseE.put("type",type);

        db.collection("Exercises").document(String.valueOf(exercise.getId())).update(exerciseE).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getActivity(),"Values added!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(),"Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
