package com.example.mobileappvolley.fragment.coach;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.MatchDate;
import com.example.mobileappvolley.Model.Statistic;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentPeriodTeamsStatsBinding;
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

public class FragmentPeriodTeamStats extends Fragment {
    FragmentPeriodTeamsStatsBinding fragmentPeriodTeamsStatsBinding;
    private ArrayList<MatchDate> matchDateArrayList = new ArrayList<>();
    private ArrayList<Statistic> teamStatisticPeriod = new ArrayList<>();
    private ArrayList<String> xAxisValues = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPeriodTeamsStatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_period_teams_stats, container, false);
        fragmentPeriodTeamsStatsBinding.setCallback(this);
        View view = fragmentPeriodTeamsStatsBinding.getRoot();

        getStats();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getStats() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Stats");
        for(int i = 0,j=3;i<4;i++,j--){
            Statistic tempStatistic = new Statistic();
            teamStatisticPeriod.add(tempStatistic);

            LocalDateTime dateTime = LocalDateTime.ofEpochSecond(matchDateArrayList.get(j).getMatchDate().getSeconds(), 0, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
            String formattedDate = dateTime.format(formatter);
            xAxisValues.add(formattedDate);

            Query query = ref.whereEqualTo("matchDate",matchDateArrayList.get(i).getMatchDate().getSeconds());
            int finalI = i;
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error == null){
                        for (QueryDocumentSnapshot document : value) {
                            Statistic statistic = new Statistic();
                            statistic.setPoints(document.getLong("points").intValue());

                            String temp = "attack";
                            statistic.setAllAttack(getValueFromMap(document, temp, "all"));
//                            statistic.setBlockAttack(getValueFromMap(document, temp, "block"));
//                            statistic.setErrorAttack(getValueFromMap(document, temp, "error"));
                            statistic.setPerfAttack(getValueFromMap(document, temp, "perfect"));
//                            statistic.setPerfAttackPerc(getValueFromMap(document, temp, "perfect (%)"));

                            temp = "receive";
                            statistic.setAllReceive(getValueFromMap(document, temp, "all"));
//                            statistic.setErrorReceive(getValueFromMap(document, temp, "error"));
//                            statistic.setNegativeReceive(getValueFromMap(document, temp, "negative"));
                            statistic.setPerfReceive(getValueFromMap(document, temp, "perfect"));
//                            statistic.setPerfReceivePerc(getValueFromMap(document, temp, "perfect (%)"));
                            statistic.setPositiveReceive(getValueFromMap(document, temp, "positive"));
//                            statistic.setPositiveReceivePerc(getValueFromMap(document, temp, "positive (%)"));

                            temp = "serve";
                            statistic.setAllServe(getValueFromMap(document, temp, "all"));
                            statistic.setAceServe(getValueFromMap(document, temp, "ace"));
//                            statistic.setErrorServe(getValueFromMap(document, temp, "error"));

                            teamStatisticPeriod.get(finalI).setAllServe(teamStatisticPeriod.get(finalI).getAllServe() + statistic.getAllServe());
                            teamStatisticPeriod.get(finalI).setAceServe(teamStatisticPeriod.get(finalI).getAceServe() + statistic.getAceServe());


                            teamStatisticPeriod.get(finalI).setPerfAttack(teamStatisticPeriod.get(finalI).getPerfAttack() + statistic.getPerfAttack());
                            teamStatisticPeriod.get(finalI).setAllAttack(teamStatisticPeriod.get(finalI).getAllAttack() + statistic.getAllAttack());

                            teamStatisticPeriod.get(finalI).setPositiveReceive(teamStatisticPeriod.get(finalI).getPositiveReceive() + statistic.getPositiveReceive());
                            teamStatisticPeriod.get(finalI).setPerfReceive(teamStatisticPeriod.get(finalI).getPerfReceive() + statistic.getPerfReceive());
                            teamStatisticPeriod.get(finalI).setAllReceive(teamStatisticPeriod.get(finalI).getAllReceive() + statistic.getAllReceive());
                        }
                        setChartValues();
                    }
                }
            });
        }
    }

    private void setChartValues() {
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.setDragEnabled(true);
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.setScaleEnabled(false);
        ArrayList<Entry> effectivenesAttackValues = new ArrayList<>();
        ArrayList<Entry> positiveReceiveValues = new ArrayList<>();
        ArrayList<Entry> perfectReceiveValues = new ArrayList<>();
        ArrayList<Entry> effectivenessServeValues = new ArrayList<>();

        for(int i = 3,j = 0;i>=0;i--,j++){
            effectivenesAttackValues.add(new Entry(j, (float)teamStatisticPeriod.get(i).getPerfAttack() / teamStatisticPeriod.get(i).getAllAttack() * 100));
            positiveReceiveValues.add(new Entry(j, (float)teamStatisticPeriod.get(i).getPositiveReceive() / teamStatisticPeriod.get(i).getAllReceive() * 100));
            perfectReceiveValues.add(new Entry(j, (float)teamStatisticPeriod.get(i).getPerfReceive() / teamStatisticPeriod.get(i).getAllReceive() * 100));
            effectivenessServeValues.add(new Entry(j, (float)teamStatisticPeriod.get(i).getAceServe() / teamStatisticPeriod.get(i).getAllServe() * 100));
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

        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.setData(data);
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.invalidate();
    }

    private void setLineChartAttributes() {
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.getLegend().setTextSize(15f);
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.setExtraTopOffset(10f);
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.setExtraRightOffset(40f);
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.setExtraLeftOffset(40f);
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
        fragmentPeriodTeamsStatsBinding.lineChartAttackEffectivnes.getXAxis().setTextSize(16f);
    }

    private void setLineDataSet(LineDataSet set1, int color) {
        set1.setFillAlpha(110);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setDrawFilled(true);
        set1.setFillColor(color);
        set1.setColor(color);
        set1.setLineWidth(2f);
//        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
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

    public static FragmentPeriodTeamStats newInstance(ArrayList<MatchDate> matchDatesArrayList) {
        Bundle args = new Bundle();
        FragmentPeriodTeamStats fragment = new FragmentPeriodTeamStats();
        args.putSerializable("matchDateArrayList",(Serializable) matchDatesArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            matchDateArrayList = (ArrayList<MatchDate>) getArguments().getSerializable("matchDateArrayList");
        }
    }

}
