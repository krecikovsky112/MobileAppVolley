package com.example.mobileappvolley.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.ActivityPlayerBinding;
import com.example.mobileappvolley.fragment.player.FragmentExercises;
import com.example.mobileappvolley.fragment.player.FragmentHomePlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityPlayer extends AppCompatActivity {

    private ActivityPlayerBinding activityPlayerBinding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        activityPlayerBinding.setCallback(this);
        firebaseAuth = FirebaseAuth.getInstance();
        addFragmentHomePlayer();

        activityPlayerBinding.logout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            checkUser();
        });

        activityPlayerBinding.navigationbar.setOnItemSelectedListener(i -> {
            System.out.println(i);

            if (i == R.id.home) {
                FragmentHomePlayer myFragment = new FragmentHomePlayer();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
            }
            else if(i == R.id.exercises){
                FragmentExercises myFragment = new FragmentExercises();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();

            }
        });
    }

    public void addFragmentHomePlayer() {
        activityPlayerBinding.navigationbar.setItemSelected(R.id.home,true);
        FragmentHomePlayer fragment = new FragmentHomePlayer();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, AuthActivity.class));
        }
    }
}
