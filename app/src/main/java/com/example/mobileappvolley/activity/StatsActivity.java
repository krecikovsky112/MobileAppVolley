package com.example.mobileappvolley.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapter;
import com.example.mobileappvolley.RecyclerViewAdapterStats;
import com.example.mobileappvolley.databinding.ActivityStatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    private ActivityStatsBinding activityStatsBinding;
    private FirebaseAuth firebaseAuth;
    ArrayList<Player> players = new ArrayList<>();
    private RecyclerViewAdapterStats recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStatsBinding = DataBindingUtil.setContentView(this, R.layout.activity_stats);
        activityStatsBinding.setCallback(this);
        firebaseAuth = FirebaseAuth.getInstance();

        activityStatsBinding.homeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityStatsBinding.homeRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapterStats(this);
        activityStatsBinding.homeRecyclerView.setAdapter(recyclerViewAdapter);
        leadData();
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Players");

        Query query = ref.orderBy("id", Query.Direction.ASCENDING);

        query.addSnapshotListener((value, error) -> {
            players.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    int id = document.getLong("id").intValue();
                    Player player = new Player();
                    player.setId(id);
                    player.setIdUser(document.getString("idUser"));
                    player.setName(document.getString("name"));
                    player.setAge(document.getLong("age").intValue());
                    player.setAttackRange(document.getLong("attackRange").intValue());
                    player.setBlockRange(document.getLong("blockRange").intValue());
                    player.setHeight(document.getLong("height").intValue());
                    player.setWeight(document.getLong("weight").intValue());
                    player.setPosition(document.getString("position"));
                    players.add(player);
                }
                recyclerViewAdapter.setItems(players);
                recyclerViewAdapter.notifyDataSetChanged();
            }

        });
    }
}
