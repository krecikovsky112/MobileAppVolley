package com.example.mobileappvolley.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class TrainingPlan {
    private Timestamp dateTime;
    private ArrayList<String> idExercises;
    private String name;

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public ArrayList<String> getIdExercises() {
        return idExercises;
    }

    public void setIdExercises(ArrayList<String> idExercises) {
        this.idExercises = idExercises;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
