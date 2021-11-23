package com.example.mobileappvolley.fragment.coach;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentExerciseBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ExerciseFragment extends Fragment {

    private FragmentExerciseBinding fragmentExerciseBinding;
    private FirebaseAuth firebaseAuth;
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "numberRepeat";
    private static final String ARG_PARAM3 = "description";
    private static final String ARG_PARAM4 = "type";
    private static final String ARG_PARAM5 = "id";
    private final Exercise exercise = new Exercise();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentExerciseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise, container, false);
        fragmentExerciseBinding.setCallback(this);
        View view = fragmentExerciseBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();

        LinearLayout linearLayout = new LinearLayout(getActivity());
        setAttibutesLinearLayout(linearLayout);
        Typeface face = ResourcesCompat.getFont(getActivity(), R.font.dongle_regular);
        TextView textView = new TextView(getActivity());
        textView.setTextSize(50);
        textView.setPadding(0, 10, 100, 0);
        textView.setTextColor(getResources().getColor(R.color.main_color));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(face);
        textView.setText(exercise.getName());
        linearLayout.addView(textView);

        Button buttonDelete = new Button(getContext());
        buttonDelete.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        buttonDelete.setBackgroundResource(R.drawable.bin);
        buttonDelete.setPadding(50,10,10,0);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Exercises").document(exercise.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("DocumentSnapshot successfully deleted!");
                        FragmentExercises myFragment = new FragmentExercises();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error deleting document" + e);
                    }
                });
            }
        });
        linearLayout.addView(buttonDelete);
        fragmentExerciseBinding.infoExerciseContainer.addView(linearLayout);

        textView = new TextView(getActivity());
        setTextViewAttributes(face, textView);
        textView.setText("Number of repeat: " + String.valueOf(exercise.getNumberRepeat()));
        fragmentExerciseBinding.infoExerciseContainer.addView(textView);

        textView = new TextView(getActivity());
        setTextViewAttributes(face, textView);
        textView.setText("Description: " + exercise.getDecription());
        fragmentExerciseBinding.infoExerciseContainer.addView(textView);

        textView = new TextView(getActivity());
        setTextViewAttributes(face, textView);
        StringBuilder stringBuilder = getStringBuilder();
        textView.setText("Type:\n" + stringBuilder.toString());
        fragmentExerciseBinding.infoExerciseContainer.addView(textView);
        return view;
    }

    @NonNull
    private StringBuilder getStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> type = exercise.getType();
        for(int i = 0;i<type.size();i++){
            stringBuilder.append("-");
            stringBuilder.append(type.get(i)).append("\n");
        }
        return stringBuilder;
    }

    private void setAttibutesLinearLayout(LinearLayout linearLayout) {
        linearLayout.setPadding(300, 10, 50, 10);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setTextViewAttributes(Typeface face, TextView textView) {
        textView.setTextSize(30);
        textView.setPadding(0, 10, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.main_color));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setTypeface(face);
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

}
