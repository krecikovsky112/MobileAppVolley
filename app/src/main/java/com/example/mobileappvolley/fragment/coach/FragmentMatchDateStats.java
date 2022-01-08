package com.example.mobileappvolley.fragment.coach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappvolley.Model.Exercise;
import com.example.mobileappvolley.Model.MatchDate;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.RecyclerViewAdapterExercises;
import com.example.mobileappvolley.RecyclerViewAdapterHistoryStats;
import com.example.mobileappvolley.databinding.FragmentMatchesdateStatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentMatchDateStats extends Fragment {
    FragmentMatchesdateStatsBinding fragmentMatchesdateStatsBinding;
    private FirebaseAuth firebaseAuth;
    private RecyclerViewAdapterHistoryStats recyclerViewAdapterHistoryStats;
    private ArrayList<MatchDate> matchDateArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMatchesdateStatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_matchesdate_stats, container, false);
        fragmentMatchesdateStatsBinding.setCallback(this);
        View view = fragmentMatchesdateStatsBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        fragmentMatchesdateStatsBinding.historyStatsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        fragmentMatchesdateStatsBinding.historyStatsRecyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapterHistoryStats = new RecyclerViewAdapterHistoryStats(this.getContext());
        fragmentMatchesdateStatsBinding.historyStatsRecyclerView.setAdapter(recyclerViewAdapterHistoryStats);

        leadData();


        return view;
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("HistoryMatches");

        Query query = ref.orderBy("matchDate", Query.Direction.ASCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    for (QueryDocumentSnapshot document : value) {
                        MatchDate matchDate = new MatchDate();
                        matchDate.setMatchDate(document.getTimestamp("matchDate"));
                        matchDateArrayList.add(matchDate);
                    }
                    recyclerViewAdapterHistoryStats.setItems(matchDateArrayList);
                    recyclerViewAdapterHistoryStats.notifyDataSetChanged();
                }
            }

        });
    }
}
