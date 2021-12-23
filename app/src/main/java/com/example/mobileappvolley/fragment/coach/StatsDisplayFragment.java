package com.example.mobileappvolley.fragment.coach;

import android.content.Context;
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
import com.example.mobileappvolley.databinding.FragmentStatsCoachBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class StatsDisplayFragment extends Fragment {
    private FragmentStatsCoachBinding fragmentStatsCoachBinding;
    private FirebaseAuth firebaseAuth;

    private ArrayList<Statistic> statistics = new ArrayList<>();
    private ArrayList<String> idPlayer = new ArrayList<>();
    private int counter = 0;

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
        } else {
            leadDataStatsPhone();
        }

//        addData();

        fragmentStatsCoachBinding.createStatisticButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

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
        ref.addSnapshotListener((value, error) -> {
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
        ref.addSnapshotListener((value, error) -> {
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

        if (statistics.get(finalI).getAllAttack() != null) {

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

//    private void addData() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("idPlayer", "eoygo9FAOaNiqojT94E9NjKHnKz1");
//        stats.put("set1", 1);
//        stats.put("set2", 6);
//        stats.put("set3", 1);
//        stats.put("set4", 6);
//        stats.put("set5", 0);
//        stats.put("points", 14);
//
//        Map<String, Object> serve = new HashMap<>();
//        serve.put("all", 13);
//        serve.put("ace", 1);
//        serve.put("error", 5);
//        stats.put("serve", serve);
//
//        Map<String, Object> receive = new HashMap<>();
//        int all = 27;
//        int positive = 15;
//        int perfect = 7;
//        receive.put("all", all);
//        receive.put("error", 1);
//        receive.put("negative", 6);
//        receive.put("positive", positive);
//        receive.put("perfect", perfect);
//        if (all != 0) {
//            receive.put("positive (%)", positive / all * 100);
//            receive.put("perfect (%)", perfect / all * 100);
//        } else {
//            receive.put("positive (%)", 0);
//            receive.put("perfect (%)", 0);
//        }
//
//        stats.put("receive", receive);
//
//        Map<String, Object> attack = new HashMap<>();
//        all = 20;
//        perfect = 0;
//        attack.put("all", all);
//        attack.put("error", 1);
//        attack.put("block", 1);
//        ;
//        attack.put("perfect", perfect);
//        if (all != 0) {
//            attack.put("perfect (%)", perfect / all * 100);
//        } else {
//            attack.put("perfect (%)", 0);
//        }
//
//        stats.put("attack", attack);
//
//        Map<String, Object> block = new HashMap<>();
//        block.put("all", 0);
//        stats.put("block", block);
//
//        db.collection("Stats").add(stats);
//
//    }
//}
