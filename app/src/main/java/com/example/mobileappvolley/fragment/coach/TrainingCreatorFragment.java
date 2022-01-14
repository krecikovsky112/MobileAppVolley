package com.example.mobileappvolley.fragment.coach;

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

import com.example.mobileappvolley.R;
import com.example.mobileappvolley.activity.MainActivityCoach;
import com.example.mobileappvolley.databinding.FragmentTrainingCreatorBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrainingCreatorFragment extends Fragment {
    private FragmentTrainingCreatorBinding fragmentTrainingCreatorBinding;
    private boolean urgentFlag = false;
    ArrayList<Integer> numbers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentTrainingCreatorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_training_creator, container, false);
        fragmentTrainingCreatorBinding.setCallback(this);
        View view = fragmentTrainingCreatorBinding.getRoot();

        for (int i = 1; i < 6; i++) {
            numbers.add(i);
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, numbers);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentTrainingCreatorBinding.numberSpinner.setAdapter(dataAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public void onClickAddExercise() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ArrayList<String> type = new ArrayList<>();
        Map<String, Object> exercises = new HashMap<>();
        exercises.put("name", fragmentTrainingCreatorBinding.editTextNameExercise.getText().toString());
        exercises.put("numberRepeat", Integer.parseInt(fragmentTrainingCreatorBinding.editTextNumberRepeat.getText().toString()));
        exercises.put("description", fragmentTrainingCreatorBinding.editTextDescription.getText().toString());
        exercises.put("urgent", urgentFlag);
        exercises.put("order",Integer.parseInt(fragmentTrainingCreatorBinding.numberSpinner.getSelectedItem().toString()));

        if (fragmentTrainingCreatorBinding.checkboxReceiver.isChecked())
            type.add("Receiver");
        if (fragmentTrainingCreatorBinding.checkboxLibero.isChecked())
            type.add("Libero");
        if (fragmentTrainingCreatorBinding.checkboxSetter.isChecked())
            type.add("Setter");
        if (fragmentTrainingCreatorBinding.checkboxMiddleBlocker.isChecked())
            type.add("Middle blocker");
        if (fragmentTrainingCreatorBinding.checkboxSpiker.isChecked())
            type.add("Spiker");
        exercises.put("type", type);

        db.collection("Exercises").add(exercises);

        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Added your exercises. What you want to do?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Add another one",
                (dialog, id) -> {
                    fragmentTrainingCreatorBinding.editTextNameExercise.setText("");
                    fragmentTrainingCreatorBinding.editTextNumberRepeat.setText("");
                    fragmentTrainingCreatorBinding.editTextDescription.setText("");
                    fragmentTrainingCreatorBinding.checkboxSpiker.setChecked(false);
                    fragmentTrainingCreatorBinding.checkboxSetter.setChecked(false);
                    fragmentTrainingCreatorBinding.checkboxReceiver.setChecked(false);
                    fragmentTrainingCreatorBinding.checkboxLibero.setChecked(false);
                    fragmentTrainingCreatorBinding.checkboxMiddleBlocker.setChecked(false);
                    fragmentTrainingCreatorBinding.urgent.setBackgroundResource(R.drawable.alarm);
                    urgentFlag = false;
                    dialog.cancel();
                });

        builder1.setNegativeButton(
                "Continue",
                (dialog, id) -> {
                    ((MainActivityCoach) getActivity()).changeToHomeNavigation();
                    AppCompatActivity activity = (AppCompatActivity) this.getContext();
                    TrainingPlanCreatorFragment myFragment = new TrainingPlanCreatorFragment();
                    assert activity != null;
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
                    dialog.cancel();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void onUrgentClick() {
        if (!urgentFlag) {
            urgentFlag = true;
            fragmentTrainingCreatorBinding.urgent.setBackgroundResource(R.drawable.alarm_red);
        } else {
            urgentFlag = false;
            fragmentTrainingCreatorBinding.urgent.setBackgroundResource(R.drawable.alarm);
        }

    }
}
