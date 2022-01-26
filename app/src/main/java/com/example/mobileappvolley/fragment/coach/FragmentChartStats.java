package com.example.mobileappvolley.fragment.coach;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Statistic;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentChartStatsBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;


public class FragmentChartStats extends Fragment {
    FragmentChartStatsBinding fragmentChartStatsBinding;
    private long matchDate;
    private static final String ARG_PARAM1 = "matchDate";
    private ArrayList<Statistic> statistics = new ArrayList<>();
    private int counter = 0;
    private Statistic teamStatistic = new Statistic();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentChartStatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart_stats, container, false);
        fragmentChartStatsBinding.setCallback(this);
        View view = fragmentChartStatsBinding.getRoot();

        leadData();

        fragmentChartStatsBinding.statsDisplay.setOnClickListener(v -> {
            StatsDisplayFragment myFragment = StatsDisplayFragment.newInstance(matchDate);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();

        });

        return view;
    }

    public static FragmentChartStats newInstance(Long param1){
        FragmentChartStats fragment = new FragmentChartStats();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchDate = getArguments().getLong(ARG_PARAM1);
        }
    }

    private void leadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Stats");
        Query query = ref.whereEqualTo("matchDate",matchDate);
        query.addSnapshotListener((value, error) -> {
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    Statistic statistic = new Statistic();
                    statistic.setPoints(document.getLong("points").intValue());

                    String temp = "attack";
                    statistic.setAllAttack(getValueFromMap(document, temp, "all"));
                    statistic.setBlockAttack(getValueFromMap(document, temp, "block"));
                    statistic.setErrorAttack(getValueFromMap(document, temp, "error"));
                    statistic.setPerfAttack(getValueFromMap(document, temp, "perfect"));
                    statistic.setPerfAttackPerc(getValueFromMap(document, temp, "perfect (%)"));

                    temp = "block";
                    statistic.setAllBlock(getValueFromMap(document, temp, "all"));

                    temp = "receive";
                    statistic.setAllReceive(getValueFromMap(document, temp, "all"));
                    statistic.setErrorReceive(getValueFromMap(document, temp, "error"));
                    statistic.setNegativeReceive(getValueFromMap(document, temp, "negative"));
                    statistic.setPerfReceive(getValueFromMap(document, temp, "perfect"));
                    statistic.setPerfReceivePerc(getValueFromMap(document, temp, "perfect (%)"));
                    statistic.setPositiveReceive(getValueFromMap(document, temp, "positive"));
                    statistic.setPositiveReceivePerc(getValueFromMap(document, temp, "positive (%)"));

                    temp = "serve";
                    statistic.setAllServe(getValueFromMap(document, temp, "all"));
                    statistic.setAceServe(getValueFromMap(document, temp, "ace"));
                    statistic.setErrorServe(getValueFromMap(document, temp, "error"));

                    statistics.add(counter, statistic);
                    counter++;

                    getTeamStatistics(statistic);
                }

                setChartValues();
            }

        });
    }

    private void setChartValues() {
        ArrayList<PieEntry> paramsPoints = new ArrayList<>();
        paramsPoints.add(new PieEntry(teamStatistic.getPerfAttack().intValue(),"Attack"));
        paramsPoints.add(new PieEntry(teamStatistic.getAllBlock().intValue(),"Block"));
        paramsPoints.add(new PieEntry(teamStatistic.getAceServe().intValue(),"Serve"));

        PieDataSet pieDataSet = new PieDataSet(paramsPoints,"Points");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        fragmentChartStatsBinding.pieChartPoints.setData(pieData);
        fragmentChartStatsBinding.pieChartPoints.getDescription().setEnabled(false);
        fragmentChartStatsBinding.pieChartPoints.setCenterText("Points");
        fragmentChartStatsBinding.pieChartPoints.setCenterTextColor(Color.BLACK);
        fragmentChartStatsBinding.pieChartPoints.setCenterTextSize(22f);
        fragmentChartStatsBinding.pieChartPoints.animate();


        ArrayList<PieEntry> paramsAttack = new ArrayList<>();
        paramsAttack.add(new PieEntry((float)teamStatistic.getErrorAttack() / teamStatistic.getAllAttack() * 100,"Error"));
        paramsAttack.add(new PieEntry((float)teamStatistic.getBlockAttack() / teamStatistic.getAllAttack() * 100,"In block"));
        paramsAttack.add(new PieEntry((float)teamStatistic.getPerfAttack() / teamStatistic.getAllAttack() * 100,"Perfect"));

        PieDataSet pieDataSet2 = new PieDataSet(paramsAttack,"Attack");
        pieDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet2.setValueTextColor(Color.WHITE);
        pieDataSet2.setValueTextSize(16f);

        PieData pieData2 = new PieData(pieDataSet2);

        fragmentChartStatsBinding.pieChartAttack.setData(pieData2);
        fragmentChartStatsBinding.pieChartAttack.getDescription().setEnabled(false);
        fragmentChartStatsBinding.pieChartAttack.setCenterText("Attack (%)");
        fragmentChartStatsBinding.pieChartAttack.setCenterTextColor(Color.BLACK);
        fragmentChartStatsBinding.pieChartAttack.setCenterTextSize(22f);
        fragmentChartStatsBinding.pieChartAttack.animate();


        ArrayList<PieEntry> paramsReceive = new ArrayList<>();
        paramsReceive.add(new PieEntry(teamStatistic.getErrorReceive(),"Error"));
        paramsReceive.add(new PieEntry(teamStatistic.getNegativeReceive(),"Negative"));
        paramsReceive.add(new PieEntry(teamStatistic.getPositiveReceive(),"Positive"));
        paramsReceive.add(new PieEntry(teamStatistic.getPerfReceive(),"Perfect"));

        PieDataSet pieDataSet3 = new PieDataSet(paramsReceive,"Receive");
        pieDataSet3.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet3.setValueTextColor(Color.WHITE);
        pieDataSet3.setValueTextSize(16f);

        PieData pieData3 = new PieData(pieDataSet3);

        fragmentChartStatsBinding.pieChartReceive.setData(pieData3);
        fragmentChartStatsBinding.pieChartReceive.getDescription().setEnabled(false);
        fragmentChartStatsBinding.pieChartReceive.setCenterText("Receive");
        fragmentChartStatsBinding.pieChartReceive.setCenterTextColor(Color.BLACK);
        fragmentChartStatsBinding.pieChartReceive.setCenterTextSize(22f);
        fragmentChartStatsBinding.pieChartReceive.animate();


        ArrayList<PieEntry> paramsServe = new ArrayList<>();
        float temp = (float)teamStatistic.getErrorServe() / teamStatistic.getAllServe() * 100 + (float) teamStatistic.getAceServe() / teamStatistic.getAllServe() * 100;
        paramsServe.add(new PieEntry((float)teamStatistic.getErrorServe() / teamStatistic.getAllServe() * 100,"Error"));
        paramsServe.add(new PieEntry((float) teamStatistic.getAceServe() / teamStatistic.getAllServe() * 100,"Ace"));
        paramsServe.add(new PieEntry((float) 100 - temp,"Normal"));

        PieDataSet pieDataSet4 = new PieDataSet(paramsServe,"Serve");
        pieDataSet4.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet4.setValueTextColor(Color.WHITE);
        pieDataSet4.setValueTextSize(16f);

        PieData pieData4 = new PieData(pieDataSet4);

        fragmentChartStatsBinding.pieChartServe.setData(pieData4);
        fragmentChartStatsBinding.pieChartServe.getDescription().setEnabled(false);
        fragmentChartStatsBinding.pieChartServe.setCenterText("Serve (%)");
        fragmentChartStatsBinding.pieChartServe.setCenterTextColor(Color.BLACK);
        fragmentChartStatsBinding.pieChartServe.setCenterTextSize(22f);
        fragmentChartStatsBinding.pieChartServe.animate();
    }

    private void getTeamStatistics(Statistic statistic) {
        teamStatistic.setPoints(teamStatistic.getPoints() + statistic.getPoints());
        teamStatistic.setAllBlock(teamStatistic.getAllBlock() + statistic.getAllBlock());

        teamStatistic.setAllAttack(teamStatistic.getAllAttack() + statistic.getAllAttack());
        teamStatistic.setErrorAttack(teamStatistic.getErrorAttack() + statistic.getErrorAttack());
        teamStatistic.setBlockAttack(teamStatistic.getBlockAttack() + statistic.getBlockAttack());
        teamStatistic.setPerfAttack(teamStatistic.getPerfAttack() + statistic.getPerfAttack());

        teamStatistic.setAllReceive(teamStatistic.getAllReceive() + statistic.getAllReceive());
        teamStatistic.setErrorReceive(teamStatistic.getErrorReceive() + statistic.getErrorReceive());
        teamStatistic.setNegativeReceive(teamStatistic.getNegativeReceive() + statistic.getNegativeReceive());
        teamStatistic.setPositiveReceive(teamStatistic.getPositiveReceive() + statistic.getPositiveReceive());
        teamStatistic.setPerfReceive(teamStatistic.getPerfReceive() + statistic.getPerfReceive());

        teamStatistic.setAllServe(teamStatistic.getAllServe() + statistic.getAllServe());
        teamStatistic.setAceServe(teamStatistic.getAceServe() + statistic.getAceServe());
        teamStatistic.setErrorServe(teamStatistic.getErrorServe() + statistic.getErrorServe());
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
}
