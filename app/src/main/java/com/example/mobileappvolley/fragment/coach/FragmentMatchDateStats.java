package com.example.mobileappvolley.fragment.coach;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.example.mobileappvolley.activity.StatsActivity;
import com.example.mobileappvolley.databinding.FragmentMatchesdateStatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentMatchDateStats extends Fragment {
    FragmentMatchesdateStatsBinding fragmentMatchesdateStatsBinding;
    private FirebaseAuth firebaseAuth;
    private RecyclerViewAdapterHistoryStats recyclerViewAdapterHistoryStats;
    private ArrayList<MatchDate> matchDateArrayList = new ArrayList<>();
    private ArrayList<MatchDate> filterMatchesArrayList = new ArrayList<>();

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

        boolean isTablet = isTablet(getContext());
        if (isTablet) {
            fragmentMatchesdateStatsBinding.createStatisticButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), StatsActivity.class));
                }
            });
        }

        fragmentMatchesdateStatsBinding.editTextDateFrom.setOnClickListener(v -> showDateTimeDialog(fragmentMatchesdateStatsBinding.editTextDateFrom));

        fragmentMatchesdateStatsBinding.editTextDateTo.setOnClickListener(v -> showDateTimeDialog(fragmentMatchesdateStatsBinding.editTextDateTo));

        fragmentMatchesdateStatsBinding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMatchesArrayList.clear();
                for(int i = 0 ;i< matchDateArrayList.size();i++){
                    String dateFrom = fragmentMatchesdateStatsBinding.editTextDateFrom.getText().toString();
                    String dateTo = fragmentMatchesdateStatsBinding.editTextDateTo.getText().toString();
                    try{
                        Date dateFROM = new SimpleDateFormat("dd/MM/yyyy").parse(dateFrom);
                        Date dateTO = new SimpleDateFormat("dd/MM/yyyy").parse(dateTo);
                        if(dateFROM.getTime()/1000 < matchDateArrayList.get(i).getMatchDate().getSeconds() && dateTO.getTime()/1000 >  matchDateArrayList.get(i).getMatchDate().getSeconds()){
                            filterMatchesArrayList.add(matchDateArrayList.get(i));
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                recyclerViewAdapterHistoryStats.setItems(filterMatchesArrayList);
                recyclerViewAdapterHistoryStats.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void showDateTimeDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            editText.setText(simpleDateFormat.format(calendar.getTime()));
        };

        new DatePickerDialog(getActivity(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("HistoryMatches");

        Query query = ref.orderBy("matchDate", Query.Direction.DESCENDING);

        query.addSnapshotListener((value, error) -> {
            matchDateArrayList.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    MatchDate matchDate = new MatchDate();
                    matchDate.setMatchDate(document.getTimestamp("matchDate"));
                    try{
                        boolean temp = document.getBoolean("win");
                        matchDate.setWin(temp);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }


                    matchDateArrayList.add(matchDate);
                }
                recyclerViewAdapterHistoryStats.setItems(matchDateArrayList);
                recyclerViewAdapterHistoryStats.notifyDataSetChanged();
            }
        });
    }
}
