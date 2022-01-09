package com.example.mobileappvolley.fragment.coach;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Statistic;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.activity.MainActivity;
import com.example.mobileappvolley.activity.StatsActivity;
import com.example.mobileappvolley.databinding.FragmentStatsCoachBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class StatsDisplayFragment extends Fragment {
    private static final String ARG_PARAM1 = "matchDate";
    private FragmentStatsCoachBinding fragmentStatsCoachBinding;
    private FirebaseAuth firebaseAuth;
    private long matchDate;

    private ArrayList<Statistic> statistics = new ArrayList<>();
    private ArrayList<String> idPlayer = new ArrayList<>();
    private int counter = 0;

    public static StatsDisplayFragment newInstance(Long param1){
        StatsDisplayFragment fragment = new StatsDisplayFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStatsCoachBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats_coach, container, false);
        fragmentStatsCoachBinding.setCallback(this);
        View view = fragmentStatsCoachBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        boolean isTablet = isTablet(getContext());
        if (isTablet) {
            leadDataStatsTablet();
            fragmentStatsCoachBinding.createStatisticButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), StatsActivity.class));
                }
            });
        } else {
            leadDataStatsPhone();
        }

        return view;
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private void leadDataStatsTablet() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Stats");
        Query query = ref.whereEqualTo("matchDate",matchDate);
        query.addSnapshotListener((value, error) -> {
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {
                    Statistic statistic = new Statistic();
                    statistic.setPoints(document.getLong("points").intValue());

                    idPlayer.add(counter, document.getString("idPlayer"));

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
                }
                leadDataNamePlayer();
            }

        });
    }


    private void leadDataStatsPhone() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Stats");
        Query query = ref.whereEqualTo("matchDate",matchDate);
        query.addSnapshotListener((value, error) -> {
            if (error == null) {
                for (QueryDocumentSnapshot document : value) {

                    Statistic statistic = new Statistic();
                    idPlayer.add(counter, document.getString("idPlayer"));

                    statistic.setPoints(document.getLong("points").intValue());

                    statistic.setPerfAttackPerc(getValueFromMap(document, "attack", "perfect (%)"));

                    statistic.setAllBlock(getValueFromMap(document, "block", "all"));

                    statistic.setPositiveReceivePerc(getValueFromMap(document, "receive", "receive (%)"));

                    statistic.setAllServe(getValueFromMap(document, "serve", "all"));

                    statistics.add(counter, statistic);
                    counter++;
                }
                leadDataNamePlayer();
            }

        });

    }

    private void leadDataNamePlayer() {
        counter = 0;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Players");
        for (int i = 0; i < 13; i++) {
            Query query = ref.whereEqualTo("idUser", idPlayer.get(i));
            int finalI = i;
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        for (QueryDocumentSnapshot document : value) {
                            String temp = document.getString("name");
                            String[] parts = temp.split(" ");

                            setTableRow(parts[0], finalI);
                            counter++;
                        }
                    }
                }
            });
        }

    }

    private void setTableRow(String part, int finalI) {
        TableRow tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView textView = new TextView(getActivity());
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(part);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.parseColor("#fdbc00"));
        if(isTablet(getContext())){
            textView.setTextSize(25);
        }
        else{
            textView.setTextSize(20);
        }
        textView.setBackgroundResource(R.drawable.textview_border_tittles);

        tr.addView(textView);

        textView = new TextView(getActivity());
        textView.setText(String.valueOf(statistics.get(finalI).getPoints()));
        setTextViewAttributes(textView);
        tr.addView(textView);

        if (isTablet(getContext())) {

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getAllAttack()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getErrorAttack()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getBlockAttack()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getPerfAttack()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getPerfAttackPerc()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getAllBlock()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getAllReceive()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getErrorReceive()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getNegativeReceive()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getPositiveReceive()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getPositiveReceivePerc()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getPerfReceive()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getPerfReceivePerc()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getAllServe()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getAceServe()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getErrorServe()));
            setTextViewAttributes(textView);
            tr.addView(textView);
            fragmentStatsCoachBinding.statsTable2.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        } else {
            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getPerfAttackPerc()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getAllBlock()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getPositiveReceivePerc()));
            setTextViewAttributes(textView);
            tr.addView(textView);

            textView = new TextView(getActivity());
            textView.setText(String.valueOf(statistics.get(finalI).getAllServe()));
            setTextViewAttributes(textView);
            tr.addView(textView);
            fragmentStatsCoachBinding.statsTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

    }

    private void setTextViewAttributes(TextView textView) {
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(Color.parseColor("#fdbc00"));
        textView.setBackgroundResource(R.drawable.textview_border_tittles);
        if(isTablet(getContext())){
            textView.setTextSize(25);
        }
        else{
            textView.setTextSize(20);
        }

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
