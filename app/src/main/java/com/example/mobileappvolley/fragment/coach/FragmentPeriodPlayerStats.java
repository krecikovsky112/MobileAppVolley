package com.example.mobileappvolley.fragment.coach;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.MatchDate;
import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.Model.Statistic;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentPeriodPlayerStatsBinding;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class FragmentPeriodPlayerStats extends Fragment {
    FragmentPeriodPlayerStatsBinding fragmentPeriodPlayerStatsBinding;
    ArrayList<String> players = new ArrayList<>();
    private ArrayList<String> xAxisValues = new ArrayList<>();
    private ArrayList<MatchDate> matchDateArrayList = new ArrayList<>();
    private ArrayList<Statistic> playerStatisticPeriod = new ArrayList<>();
    private ArrayList<String> playersId = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPeriodPlayerStatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_period_player_stats, container, false);
        fragmentPeriodPlayerStatsBinding.setCallback(this);
        View view = fragmentPeriodPlayerStatsBinding.getRoot();

        fragmentPeriodPlayerStatsBinding.accept.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                playerStatisticPeriod.clear();
                fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.invalidate();
                fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.clear();
                getStatsPlayer(fragmentPeriodPlayerStatsBinding.playersSpinner.getSelectedItemPosition());
            }
        });

        return view;
    }

    public static FragmentPeriodPlayerStats newInstance(ArrayList<MatchDate> matchDatesArrayList) {
        Bundle args = new Bundle();
        FragmentPeriodPlayerStats fragment = new FragmentPeriodPlayerStats();
        args.putSerializable("matchDateArrayList",(Serializable) matchDatesArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getStatsPlayer(int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Stats");
        for(int i = 0,j=3;i<4;i++,j--){
            Statistic tempStatistic = new Statistic();
            playerStatisticPeriod.add(tempStatistic);

            LocalDateTime dateTime = LocalDateTime.ofEpochSecond(matchDateArrayList.get(j).getMatchDate().getSeconds(), 0, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
            String formattedDate = dateTime.format(formatter);
            xAxisValues.add(formattedDate);

            Query query = ref.whereEqualTo("idPlayer",playersId.get(position)).whereEqualTo("matchDate",matchDateArrayList.get(i).getMatchDate().getSeconds());
            int finalI = i;
            query.addSnapshotListener((value, error) -> {
                if(error == null){
                    for (QueryDocumentSnapshot document : value) {
                        Statistic statistic = new Statistic();
                        statistic.setPoints(document.getLong("points").intValue());

                        String temp = "attack";
                        statistic.setAllAttack(getValueFromMap(document, temp, "all"));
//                        statistic.setBlockAttack(getValueFromMap(document, temp, "block"));
//                        statistic.setErrorAttack(getValueFromMap(document, temp, "error"));
                        statistic.setPerfAttack(getValueFromMap(document, temp, "perfect"));
//                        statistic.setPerfAttackPerc(getValueFromMap(document, temp, "perfect (%)"));

                        temp = "receive";
                        statistic.setAllReceive(getValueFromMap(document, temp, "all"));
//                        statistic.setErrorReceive(getValueFromMap(document, temp, "error"));
//                        statistic.setNegativeReceive(getValueFromMap(document, temp, "negative"));
                        statistic.setPerfReceive(getValueFromMap(document, temp, "perfect"));
//                        statistic.setPerfReceivePerc(getValueFromMap(document, temp, "perfect (%)"));
                        statistic.setPositiveReceive(getValueFromMap(document, temp, "positive"));
//                        statistic.setPositiveReceivePerc(getValueFromMap(document, temp, "positive (%)"));

                        temp = "serve";
                        statistic.setAllServe(getValueFromMap(document, temp, "all"));
                        statistic.setAceServe(getValueFromMap(document, temp, "ace"));
//                        statistic.setErrorServe(getValueFromMap(document, temp, "error"));

                        playerStatisticPeriod.get(finalI).setAllServe(playerStatisticPeriod.get(finalI).getAllServe() + statistic.getAllServe());
                        playerStatisticPeriod.get(finalI).setAceServe(playerStatisticPeriod.get(finalI).getAceServe() + statistic.getAceServe());


                        playerStatisticPeriod.get(finalI).setPerfAttack(playerStatisticPeriod.get(finalI).getPerfAttack() + statistic.getPerfAttack());
                        playerStatisticPeriod.get(finalI).setAllAttack(playerStatisticPeriod.get(finalI).getAllAttack() + statistic.getAllAttack());

                        playerStatisticPeriod.get(finalI).setPositiveReceive(playerStatisticPeriod.get(finalI).getPositiveReceive() + statistic.getPositiveReceive());
                        playerStatisticPeriod.get(finalI).setPerfReceive(playerStatisticPeriod.get(finalI).getPerfReceive() + statistic.getPerfReceive());
                        playerStatisticPeriod.get(finalI).setAllReceive(playerStatisticPeriod.get(finalI).getAllReceive() + statistic.getAllReceive());
                    }
                    setChartValues();
                }
            });
        }
    }

    private void setChartValues() {
        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.setDragEnabled(true);
        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.setScaleEnabled(false);
        ArrayList<Entry> effectivenesAttackValues = new ArrayList<>();
        ArrayList<Entry> positiveReceiveValues = new ArrayList<>();
        ArrayList<Entry> perfectReceiveValues = new ArrayList<>();
        ArrayList<Entry> effectivenessServeValues = new ArrayList<>();

        for(int i = 3,j = 0;i>=0;i--,j++){
            effectivenesAttackValues.add(new Entry(j, (float)playerStatisticPeriod.get(i).getPerfAttack() / playerStatisticPeriod.get(i).getAllAttack() * 100));
            positiveReceiveValues.add(new Entry(j, (float)playerStatisticPeriod.get(i).getPositiveReceive() / playerStatisticPeriod.get(i).getAllReceive() * 100));
            perfectReceiveValues.add(new Entry(j, (float)playerStatisticPeriod.get(i).getPerfReceive() / playerStatisticPeriod.get(i).getAllReceive() * 100));
            effectivenessServeValues.add(new Entry(j, (float)playerStatisticPeriod.get(i).getAceServe() / playerStatisticPeriod.get(i).getAllServe() * 100));
        }

        LineDataSet set1 = new LineDataSet(effectivenesAttackValues,"Attack effectiveness (%)");
        setLineDataSet(set1, Color.BLUE);

        LineDataSet set2 = new LineDataSet(positiveReceiveValues,"Positive receive (%)");
        setLineDataSet(set2, Color.GREEN);

        LineDataSet set3 = new LineDataSet(perfectReceiveValues,"Perfect receive (%)");
        setLineDataSet(set3, Color.RED);

        LineDataSet set4 = new LineDataSet(effectivenessServeValues,"Serve points effectiveness (%)");
        setLineDataSet(set4, Color.MAGENTA);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);

        LineData data = new LineData(dataSets);

        setLineChartAttributes();

        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.setData(data);
    }

    private void setLineChartAttributes() {
        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.getLegend().setTextSize(15f);
        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.setExtraTopOffset(10f);
        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.setExtraRightOffset(40f);
        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.setExtraLeftOffset(40f);
        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
        fragmentPeriodPlayerStatsBinding.lineChartEffectiveness.getXAxis().setTextSize(16f);
    }

    private void setLineDataSet(LineDataSet set1, int color) {
        set1.setFillAlpha(110);
        set1.setColor(color);
        set1.setLineWidth(2f);
        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setValueTextSize(17f);
        set1.setValueTextColor(color);
    }

    private long getValueFromMap(QueryDocumentSnapshot document, String key1, String key2) {
        long res = 0;
        Map<String, Object> statsMap = document.getData();
        for (Map.Entry<String, Object> entry : statsMap.entrySet()) {
            if (entry.getKey().equals(key1)) {
                Map<String, Object> map = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> e : map.entrySet()) {
                    if (e.getKey().equals(key2)) {
                        res = (Long) e.getValue();
                    }
                }
            }
        }
        return res;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Players");

        Query query = ref.orderBy("id", Query.Direction.ASCENDING);

        query.addSnapshotListener((value, error) -> {
            players.clear();
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    Player player = new Player();
                    player.setIdUser(document.getString("idUser"));
                    player.setName(document.getString("name"));
                    players.add(player.getName());
                    playersId.add(player.getIdUser());
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, players);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fragmentPeriodPlayerStatsBinding.playersSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            fragmentPeriodPlayerStatsBinding.playersSpinner.setAdapter(dataAdapter);
            fragmentPeriodPlayerStatsBinding.playersSpinner.setSelection(0);
        });

        if(getArguments() != null){
            matchDateArrayList = (ArrayList<MatchDate>) getArguments().getSerializable("matchDateArrayList");
        }
    }
}
