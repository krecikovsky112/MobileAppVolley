package com.example.mobileappvolley.fragment.coach;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterExercises;
import com.example.mobileappvolley.activity.MainActivityCoach;
import com.example.mobileappvolley.databinding.FragmentTrainingplanCreatorBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TrainingPlanCreatorFragment extends Fragment {
    private FragmentTrainingplanCreatorBinding fragmentTrainingplanCreatorBinding;
    private FirebaseAuth firebaseAuth;
    ArrayList<Exercise> exercises = new ArrayList<>();
    ArrayList<String> exercisesSpinner = new ArrayList<>();
    private RecyclerViewAdapterExercises recyclerViewAdapterExercises;
    ArrayList<String> exercisesToSave = new ArrayList<>();
    Calendar calendarToSave;
    ArrayList<String> categories = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentTrainingplanCreatorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trainingplan_creator, container, false);
        fragmentTrainingplanCreatorBinding.setCallback(this);
        View view = fragmentTrainingplanCreatorBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        fragmentTrainingplanCreatorBinding.exercisesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        fragmentTrainingplanCreatorBinding.exercisesRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapterExercises = new RecyclerViewAdapterExercises(this.getContext());
        fragmentTrainingplanCreatorBinding.exercisesRecyclerView.setAdapter(recyclerViewAdapterExercises);


        initCategorySpinner();

        leadData();

        fragmentTrainingplanCreatorBinding.createExercise.setOnClickListener(v -> {
            TrainingCreatorFragment myFragment = new TrainingCreatorFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();

        });

        fragmentTrainingplanCreatorBinding.addExercise.setOnClickListener(v -> {
            recyclerViewAdapterExercises.addItem(exercises.get(fragmentTrainingplanCreatorBinding.exercisesSpinner.getSelectedItemPosition()));
            recyclerViewAdapterExercises.notifyDataSetChanged();
        });

        fragmentTrainingplanCreatorBinding.editTextDatePlanTraining.setOnClickListener(v -> showDateTimeDialog());

        fragmentTrainingplanCreatorBinding.save.setOnClickListener(v -> {
            for(int i = 0;i<recyclerViewAdapterExercises.getItemCount();i++){
                exercisesToSave.add(recyclerViewAdapterExercises.getExerciseArrayList().get(i).getId());
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> trainingPlan = new HashMap<>();
            trainingPlan.put("name", fragmentTrainingplanCreatorBinding.editTextNameTrainingPlan.getText().toString());

            calendarToSave.set(Calendar.SECOND, 0);
            calendarToSave.set(Calendar.MILLISECOND, 0);
            Timestamp timestamp = new Timestamp(calendarToSave.getTime());
            trainingPlan.put("dateTime", timestamp);

            trainingPlan.put("idExercises", exercisesToSave);
            trainingPlan.put("category",fragmentTrainingplanCreatorBinding.categorySpinner.getSelectedItem().toString());

            db.collection("TrainingPlan").add(trainingPlan);

            showDialog();
        });

        return view;
    }

    private void initCategorySpinner() {
        categories.add("default");
        categories.add("normal");
        categories.add("pre-match");
        categories.add("post-match");
        categories.add("preparatory");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentTrainingplanCreatorBinding.categorySpinner.setAdapter(dataAdapter);
    }

    private void showDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Added your training plan. What you want to do?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Add another one",
                (dialog, id) -> {
                    fragmentTrainingplanCreatorBinding.editTextNameTrainingPlan.setText("");
                    fragmentTrainingplanCreatorBinding.editTextDatePlanTraining.setText("");
                    dialog.cancel();
                });

        builder1.setNegativeButton(
                "Continue",
                (dialog, id) -> {
                    ((MainActivityCoach) getActivity()).changeToTrainingPlansNavigation();
                    AppCompatActivity activity = (AppCompatActivity) this.getContext();
                    FragmentTrainingPlans myFragment = new FragmentTrainingPlans();
                    assert activity != null;
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
                    dialog.cancel();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void showDateTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                calendarToSave = calendar;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                fragmentTrainingplanCreatorBinding.editTextDatePlanTraining.setText(simpleDateFormat.format(calendar.getTime()));
            };

            new TimePickerDialog(getActivity(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        };

        new DatePickerDialog(getActivity(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Exercises");

        ref.addSnapshotListener((value, error) -> {
            exercisesSpinner.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    Exercise exercise = new Exercise();
                    exercise.setId(document.getId());
                    exercise.setName(document.getString("name"));
                    exercise.setNumberRepeat(document.getLong("numberRepeat").intValue());
                    exercise.setDecription(document.getString("description"));
                    exercise.setType((ArrayList<String>) document.get("type"));
                    exercises.add(exercise);
                    exercisesSpinner.add(exercise.getName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, exercisesSpinner);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fragmentTrainingplanCreatorBinding.exercisesSpinner.setAdapter(dataAdapter);
            }

        });

    }
}
