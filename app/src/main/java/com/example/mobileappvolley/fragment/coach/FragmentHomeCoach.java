package com.example.mobileappvolley.fragment.coach;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.RecyclerViewAdapter;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentHomeCoachBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FragmentHomeCoach extends Fragment {
    private FragmentHomeCoachBinding fragmentHomeCoachBinding;
    private FirebaseAuth firebaseAuth;
    private boolean flagLayoutNull = true;
    ArrayList<Player> players = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeCoachBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_coach, container, false);
        fragmentHomeCoachBinding.setCallback(this);
        View view = fragmentHomeCoachBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();

        fragmentHomeCoachBinding.homeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        fragmentHomeCoachBinding.homeRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(this.getContext());
        fragmentHomeCoachBinding.homeRecyclerView.setAdapter(recyclerViewAdapter);
        leadData();

        return view;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getActivity(), AuthActivity.class));
        }
    }

}
