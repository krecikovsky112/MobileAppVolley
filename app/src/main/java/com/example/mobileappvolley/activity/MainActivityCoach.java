package com.example.mobileappvolley.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.ActivityCoachBinding;
import com.example.mobileappvolley.fragment.coach.FragmentExercises;
import com.example.mobileappvolley.fragment.coach.FragmentHomeCoach;
import com.example.mobileappvolley.fragment.coach.FragmentNotifications;
import com.example.mobileappvolley.fragment.coach.TrainingCreatorFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityCoach extends AppCompatActivity {
    private ActivityCoachBinding activityCoachBinding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCoachBinding = DataBindingUtil.setContentView(this, R.layout.activity_coach);
        activityCoachBinding.setCallback(this);
        firebaseAuth = FirebaseAuth.getInstance();
        addFragmentHomeCoach();

        activityCoachBinding.logout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            checkUser();
        });

        activityCoachBinding.navigationbar.setOnItemSelectedListener(i -> {
            System.out.println(i);

            if (i == R.id.home) {
                FragmentHomeCoach myFragment = new FragmentHomeCoach();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();

            } else if (i == R.id.creator) {
                TrainingCreatorFragment myFragment = new TrainingCreatorFragment();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
            } else if (i == R.id.exercises) {
                FragmentExercises myFragment = new FragmentExercises();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
            }
            else if(i == R.id.notifications){
                FragmentNotifications myFragment = new FragmentNotifications();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
            }
        });
    }

    public void addFragmentHomeCoach() {
        activityCoachBinding.navigationbar.setItemSelected(R.id.home,true);
        FragmentHomeCoach fragment = new FragmentHomeCoach();
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
    public void changeToHomeNavigation(){
        activityCoachBinding.navigationbar.setItemSelected(R.id.home,true);
    }
}
