package com.example.mobileappvolley.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.ActivityMainBinding;
import com.example.mobileappvolley.fragment.coach.FragmentHomeCoach;
import com.example.mobileappvolley.fragment.player.FragmentHomePlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ActivityMainBinding activityMainBinding;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setCallback(this);
        firebaseAuth = FirebaseAuth.getInstance();

        getRoleUser();
    }

    private void getRoleUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        DocumentReference documentReference = db.collection("Users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    System.out.println("DocumentSnapshot data: " + document.getData());
                    String role1 = document.getString("role");
                    assert role1 != null;
                    if(role1.equals("player")){
                        Intent intent = new Intent(this,MainActivityPlayer.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(this,MainActivityCoach.class);
                        startActivity(intent);
                    }
                }
                else{
                    System.out.println("No such document");
                }
            }
            else{
                System.out.println("get failed with " + task.getException());
            }
        });

    }


    public void addFragmentHomeCoach() {
        FragmentHomeCoach fragment = new FragmentHomeCoach();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    public void addFragmentHomePlayer() {
        FragmentHomePlayer fragment = new FragmentHomePlayer();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }


//    public void onSubmit() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        Map<String, Object> players = new HashMap<>();
//        players.put("name",activityMainBinding.name.getText().toString());
//        players.put("age",Integer.parseInt(activityMainBinding.age.getText().toString()));
//        players.put("attackRange",Integer.parseInt(activityMainBinding.attackRange.getText().toString()));
//        players.put("blockRange",Integer.parseInt(activityMainBinding.blockRange.getText().toString()));
//        players.put("height",Integer.parseInt(activityMainBinding.Height.getText().toString()));
//        players.put("positionId",Integer.parseInt(activityMainBinding.positionId.getText().toString()));
//        players.put("weight",Integer.parseInt(activityMainBinding.weight.getText().toString()));
//
//    db.collection("Players").document(String.valueOf(counter)).set(players).addOnCompleteListener(task -> {
//     if(task.isSuccessful()){
//         Toast.makeText(MainActivity.this,"Values added!",Toast.LENGTH_SHORT).show();
//         counter++;
//         activityMainBinding.name.setText("");
//         activityMainBinding.age.setText("");
//         activityMainBinding.attackRange.setText("");
//         activityMainBinding.blockRange.setText("");
//         activityMainBinding.Height.setText("");
//         activityMainBinding.positionId.setText("");
//         activityMainBinding.weight.setText("");
//     }
//     else{
//         Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();
//     }
//    });
//
//    }

}