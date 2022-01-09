package com.example.mobileappvolley.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.Model.Statistic;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterStats;
import com.example.mobileappvolley.databinding.ActivityStatsBinding;
import com.example.mobileappvolley.fragment.coach.PlayerStatsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatsActivity extends AppCompatActivity implements PlayerStatsFragment.SendMessages{
    private ActivityStatsBinding activityStatsBinding;
    private FirebaseAuth firebaseAuth;
    ArrayList<Player> players = new ArrayList<>();
    private RecyclerViewAdapterStats recyclerViewAdapter;
    private PlayerStatsFragment playerStatsFragment;
    private ArrayList<String> idDocumentsToDelete = new ArrayList<>();

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

        activityStatsBinding.sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference ref = db.collection("Stats");
                deleteAllDocumentsFromCollection(ref);

                for(int i = 0;i< players.size();i++){
                    Map<String, Object> statistic = new HashMap<>();
                    PlayerStatsFragment fragment = (PlayerStatsFragment) getSupportFragmentManager().findFragmentByTag(players.get(i).getIdUser());
                    if (fragment != null && fragment.getTag().equals(players.get(i).getIdUser())) {
                        Statistic tempStatistic = fragment.statisticPlayer;

                        statistic.put("idPlayer",tempStatistic.getIdPlayer());
                        statistic.put("points",tempStatistic.getPoints());

                        Map<String, Object> attack = new HashMap<>();
                        attack.put("all", tempStatistic.getAllAttack());
                        attack.put("error", tempStatistic.getErrorAttack());
                        attack.put("block", tempStatistic.getBlockAttack());
                        attack.put("perfect", tempStatistic.getPerfAttack());
                        attack.put("perfect (%)",tempStatistic.getPerfAttackPerc());
                        statistic.put("attack",attack);

                        Map<String, Object> serve = new HashMap<>();
                        serve.put("all", tempStatistic.getAllServe());
                        serve.put("ace", tempStatistic.getAceServe());
                        serve.put("error", tempStatistic.getErrorServe());
                        statistic.put("serve", serve);

                        Map<String, Object> receive = new HashMap<>();
                        receive.put("all", tempStatistic.getAllReceive());
                        receive.put("error", tempStatistic.getErrorReceive());
                        receive.put("negative", tempStatistic.getNegativeReceive());
                        receive.put("positive", tempStatistic.getPositiveReceive());
                        receive.put("perfect", tempStatistic.getPerfReceive());
                        receive.put("positive (%)", tempStatistic.getPositiveReceivePerc());
                        receive.put("perfect (%)", tempStatistic.getPerfReceivePerc());
                        statistic.put("receive", receive);

                        Map<String, Object> block = new HashMap<>();
                        block.put("all", tempStatistic.getAllBlock());
                        statistic.put("block", block);

                        statistic.put("matchDate",Timestamp.now().getSeconds());

                        db.collection("Stats").add(statistic);

                        System.out.println("Add values!");

                        startActivity(new Intent(getApplicationContext(), MainActivityCoach.class));
                    }
                }
            }
        });
    }

    void deleteAllDocumentsFromCollection(CollectionReference collection) {
        try {
            collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            idDocumentsToDelete.add(documentSnapshot.getId());

                            collection.document(documentSnapshot.getId()).delete();
                        }
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Players");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

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

                for (int i = players.size() - 1; i >= 0; i--) {
                    playerStatsFragment = PlayerStatsFragment.newInstance(players.get(i).getIdUser());
                    ft.add(R.id.fragmentContainer, playerStatsFragment, players.get(i).getIdUser());
                }

                ft.commit();
                recyclerViewAdapter.setItems(players);
                recyclerViewAdapter.notifyDataSetChanged();

//                PlayerStatsFragment myFragment = PlayerStatsFragment.newInstance(players.get(0).getIdUser());
//                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).commit();

            }

        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void send(Statistic statistic) {
        String temp;
    }
}
