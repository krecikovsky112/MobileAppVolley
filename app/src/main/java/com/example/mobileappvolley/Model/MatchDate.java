package com.example.mobileappvolley.Model;

import com.google.firebase.Timestamp;

public class MatchDate {
    private int number;
    private Timestamp matchDate;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Timestamp getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Timestamp matchDate) {
        this.matchDate = matchDate;
    }

}
