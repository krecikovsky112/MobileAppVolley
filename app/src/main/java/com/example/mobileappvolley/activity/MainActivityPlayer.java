package com.example.mobileappvolley.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.ActivityPlayerBinding;
import com.example.mobileappvolley.fragment.player.FragmentExercises;
import com.example.mobileappvolley.fragment.player.FragmentHomePlayer;
import com.example.mobileappvolley.fragment.player.FragmentNotificationsPlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        setToken();
        FirebaseMessaging.getInstance().subscribeToTopic("all");

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
            else if(i == R.id.notifications){
                FragmentNotificationsPlayer myFragment = new FragmentNotificationsPlayer();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
            }
        });


    }

    private void setToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String  token = Objects.requireNonNull(task.getResult()).getToken();
                            System.out.println(token);
                            firebaseUser = firebaseAuth.getCurrentUser();
                            Map<String, Object> tokenT = new HashMap<>();
                            tokenT.put("token",token);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Tokens").document(firebaseUser.getUid()).update(tokenT).addOnCompleteListener(task2 -> {
                                if(task2.isSuccessful()){
                                    Toast.makeText(MainActivityPlayer.this,"Values added!",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MainActivityPlayer.this,"Error!",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
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
