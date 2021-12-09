package com.example.mobileappvolley.fragment.coach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentStatsCoachBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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

        addData();
        return view;
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
