package com.example.mobileappvolley.Model;

import com.google.firebase.Timestamp;

public class MatchDate {
    private int number;
    private Timestamp matchDate;
    private boolean win;

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

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
