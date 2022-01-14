package com.example.mobileappvolley.fragment.coach;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterExercises;
import com.example.mobileappvolley.databinding.FragmentTrainingplanCreatorBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrainingPlanCreatorFragment extends Fragment {
    private FragmentTrainingplanCreatorBinding fragmentTrainingplanCreatorBinding;
    private FirebaseAuth firebaseAuth;
    ArrayList<Exercise> exercises = new ArrayList<>();
    ArrayList<String> exercisesSpinner = new ArrayList<>();
    private RecyclerViewAdapterExercises recyclerViewAdapterExercises;
    ArrayList<String>exercisesToSave = new ArrayList<>();
        Calendar calendarToSave;

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

        leadData();

        fragmentTrainingplanCreatorBinding.createExercise.setOnClickListener(v -> {
            TrainingCreatorFragment myFragment = new TrainingCreatorFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();

        });

        fragmentTrainingplanCreatorBinding.addExercise.setOnClickListener(v -> {
            exercisesToSave.add(exercises.get(fragmentTrainingplanCreatorBinding.numberSpinner.getSelectedItemPosition()).getId());
            recyclerViewAdapterExercises.addItems(exercises.get(fragmentTrainingplanCreatorBinding.numberSpinner.getSelectedItemPosition()));
            recyclerViewAdapterExercises.notifyDataSetChanged();
        });

        fragmentTrainingplanCreatorBinding.editTextDatePlanTraining.setOnClickListener(v -> showDateTimeDialog());

        fragmentTrainingplanCreatorBinding.save.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> trainingPlan = new HashMap<>();
            trainingPlan.put("name", fragmentTrainingplanCreatorBinding.editTextNameTrainingPlan.getText().toString());

            calendarToSave.set(Calendar.SECOND,0);
            calendarToSave.set(Calendar.MILLISECOND,0);
            Timestamp timestamp = new Timestamp(calendarToSave.getTime());
            trainingPlan.put("dateTime",timestamp);

            trainingPlan.put("idExercises",exercisesToSave);

            db.collection("TrainingPlan").add(trainingPlan);
        });

        return view;
    }

    private void showDateTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

                calendarToSave = calendar;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                fragmentTrainingplanCreatorBinding.editTextDatePlanTraining.setText(simpleDateFormat.format(calendar.getTime()));
            };

            new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
        };

        new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
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
            }

        });

    }
}
