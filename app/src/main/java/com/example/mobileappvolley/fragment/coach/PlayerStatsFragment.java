package com.example.mobileappvolley.fragment.coach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentStatsPlayerBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PlayerStatsFragment extends Fragment {
    private FragmentStatsPlayerBinding fragmentStatsPlayerBinding;
    private FirebaseAuth firebaseAuth;
    private final Player player = new Player();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStatsPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats_player, container, false);
        fragmentStatsPlayerBinding.setCallback(this);
        View view = fragmentStatsPlayerBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }
}
