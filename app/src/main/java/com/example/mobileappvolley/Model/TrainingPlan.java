package com.example.mobileappvolley.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class TrainingPlan {
    private String id;
    private Timestamp dateTime;
    private ArrayList<String> idExercises;
    private String name;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
