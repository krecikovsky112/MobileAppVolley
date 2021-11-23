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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivityPlayer extends AppCompatActivity {

    private ActivityPlayerBinding activityPlayerBinding;
    private FirebaseUser firebaseUser;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FirebaseAuth firebaseAuth;
    private String position;

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

        getPlayerInfo();

        activityPlayerBinding.navigationbar.setOnItemSelectedListener(i -> {
            System.out.println(i);

            if (i == R.id.home) {
                FragmentHomePlayer myFragment = new FragmentHomePlayer();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
            }
            else if(i == R.id.exercises){
                FragmentExercises myFragment = FragmentExercises.newInstance(position);
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
         firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, AuthActivity.class));
        }
    }

    private void getPlayerInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        CollectionReference ref = db.collection("Players");

        Query query = ref.whereEqualTo("idUser", firebaseUser.getUid());

        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    System.out.println("DocumentSnapshot data: " + document.getData());

                    position = document.getString("position");

                }
            }
            else{
                System.out.println("get failed with " + task.getException());
            }
        });


    }
}
