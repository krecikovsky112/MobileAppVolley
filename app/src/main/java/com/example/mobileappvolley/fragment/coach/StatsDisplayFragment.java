package com.example.mobileappvolley.fragment.coach;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentStatsCoachBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;


public class StatsDisplayFragment extends Fragment {
    private FragmentStatsCoachBinding fragmentStatsCoachBinding;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStatsCoachBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats_coach, container, false);
        fragmentStatsCoachBinding.setCallback(this);
        View view = fragmentStatsCoachBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

//        addData();
        leadData();
        return view;
    }

    private void leadData() {
        final long[] attackPercent = {0};
        final long[] block = {0};
        final int[] points = {0};
        final long[] receivePercent = {0};
        final long[] serve = {0};

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Stats");
        Query query = ref.orderBy("points", Query.Direction.DESCENDING);
        query.addSnapshotListener((value, error) -> {
            if (error == null) {

                for (QueryDocumentSnapshot document : value) {
                    TableRow tr = new TableRow(getActivity());
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    points[0] = document.getLong("points").intValue();

                    getValueFromMap(attackPercent, document, "attack", "perfect (%)");

                    getValueFromMap(block, document, "block", "all");

                    getValueFromMap(receivePercent, document, "receive", "positive (%)");

                    getValueFromMap(serve, document, "serve", "ace");

                    TextView textView = new TextView(getActivity());
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText("Kowalski");
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextColor(Color.parseColor("#17358b"));
                    textView.setTextSize(20);
                    tr.addView(textView);

                    textView = new TextView(getActivity());
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText(String.valueOf(points[0]));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextColor(Color.parseColor("#17358b"));
                    textView.setTextSize(18);
                    tr.addView(textView);

                    textView = new TextView(getActivity());
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText(String.valueOf((int)attackPercent[0]));
                    textView.setTextColor(Color.parseColor("#17358b"));
                    textView.setTextSize(18);
                    tr.addView(textView);

                    textView = new TextView(getActivity());
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText(String.valueOf((int)block[0]));
                    textView.setTextColor(Color.parseColor("#17358b"));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextSize(18);
                    tr.addView(textView);

                    textView = new TextView(getActivity());
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText(String.valueOf((int)receivePercent[0]));
                    textView.setTextColor(Color.parseColor("#17358b"));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextSize(18);
                    tr.addView(textView);

                    textView = new TextView(getActivity());
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textView.setText(String.valueOf((int)serve[0]));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextColor(Color.parseColor("#17358b"));
                    textView.setTextSize(18);
                    tr.addView(textView);
                    fragmentStatsCoachBinding.statsTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                }
            }

        });



         }

    private void getValueFromMap(long[] value,QueryDocumentSnapshot document, String key1, String key2) {
        Map<String, Object> statsMap = document.getData();
        for (Map.Entry<String,Object> entry : statsMap.entrySet()){
            if(entry.getKey().equals(key1)){
                Map<String, Object> map = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> e : map.entrySet()){
                    if (e.getKey().equals(key2)) {
                            value[0] =  (Long)e.getValue();
                    }
                }
            }
        }
    }

    private void addData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> stats = new HashMap<>();
        stats.put("idPlayer","eoygo9FAOaNiqojT94E9NjKHnKz1");
        stats.put("set1",1);
        stats.put("set2",6);
        stats.put("set3",1);
        stats.put("set4",6);
        stats.put("set5",0);
        stats.put("points",14);

        Map<String, Object> serve = new HashMap<>();
        serve.put("all",13);
        serve.put("ace",1);
        serve.put("error",5);
        stats.put("serve",serve);

        Map<String, Object> receive = new HashMap<>();
        int all = 27;
        int positive = 15;
        int perfect = 7;
        receive.put("all",all);
        receive.put("error",1);
        receive.put("negative",6);
        receive.put("positive",positive);
        receive.put("perfect",perfect);
        if(all != 0){
            receive.put("positive (%)",positive/all * 100);
            receive.put("perfect (%)",perfect/all * 100);
        }
        else{
            receive.put("positive (%)",0);
            receive.put("perfect (%)",0);
        }

        stats.put("receive",receive);

        Map<String, Object> attack = new HashMap<>();
        all = 20;
        perfect = 0;
        attack.put("all",all);
        attack.put("error",1);
        attack.put("block",1);;
        attack.put("perfect",perfect);
        if(all != 0){
            attack.put("perfect (%)",perfect/all * 100);
        }
        else{
            attack.put("perfect (%)",0);
        }

        stats.put("attack",attack);

        Map<String, Object> block = new HashMap<>();
        block.put("all",0);
        stats.put("block",block);

        db.collection("Stats").add(stats);

    }
}
