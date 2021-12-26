package com.example.mobileappvolley.Model;

import java.io.Serializable;

public class Player implements Serializable {
    private int id;
    private String name;
    private int age;
    private int attackRange;
    private int blockRange;
    private int height;
    private String position;
    private int weight;
    private String idUser;
    private boolean isChecked = false;

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getBlockRange() {
        return blockRange;
    }

    public void setBlockRange(int blockRange) {
        this.blockRange = blockRange;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
