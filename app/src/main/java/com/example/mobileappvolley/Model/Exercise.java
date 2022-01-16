package com.example.mobileappvolley.Model;

import java.util.ArrayList;

public class Exercise {
    private String id;
    private String name;
    private int numberRepeat;
    private String decription;
    private ArrayList<String> type;

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberRepeat() {
        return numberRepeat;
    }

    public void setNumberRepeat(int numberRepeat) {
        this.numberRepeat = numberRepeat;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }
}
