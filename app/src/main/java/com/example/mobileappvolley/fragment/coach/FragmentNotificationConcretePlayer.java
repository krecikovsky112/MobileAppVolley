package com.example.mobileappvolley.fragment.coach;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Token;
import com.example.mobileappvolley.Notifications.FcmNotificationsSender;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentNotificationToConcretePlayerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FragmentNotificationConcretePlayer extends Fragment {
    private static final String ARG_PARAM1 = "idUser";
    private static final String ARG_PARAM2 = "name";
    private String idUser;
    private Token token = new Token();
    private FirebaseAuth firebaseAuth;
    private FragmentNotificationToConcretePlayerBinding fragmentNotificationToConcretePlayerBinding;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNotificationToConcretePlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_to_concrete_player, container, false);
        fragmentNotificationToConcretePlayerBinding.setCallback(this);
        View view = fragmentNotificationToConcretePlayerBinding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();

        fragmentNotificationToConcretePlayerBinding.tittle.setText("Send to " + name);
        checkUser();
        //TODO:Send message to all
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUser = getArguments().getString(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            leadData();
        }
    }

    public static FragmentNotificationConcretePlayer newInstance(String param1,String param2) {
        FragmentNotificationConcretePlayer fragment = new FragmentNotificationConcretePlayer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getActivity(), AuthActivity.class));
        }
    }

    private void leadData() {
        if(idUser != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference ref = db.collection("Tokens").document(idUser);
            ref.get().addOnSuccessListener(documentSnapshot -> token.setToken(documentSnapshot.getString("token")));
        }
    }

    public void onClickSend() {
        System.out.println("Send message!");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> notification = new HashMap<>();
        notification.put("title",fragmentNotificationToConcretePlayerBinding.editTextTitle.getText().toString());
        notification.put("description",fragmentNotificationToConcretePlayerBinding.editTextMessage.getText().toString());
        notification.put("id",idUser);
        long dateTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(dateTime);
        notification.put("time",timestamp);

        db.collection("Notifications").add(notification);

        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token.getToken(),
                fragmentNotificationToConcretePlayerBinding.editTextTitle.getText().toString(),
                fragmentNotificationToConcretePlayerBinding.editTextMessage.getText().toString(),
                getActivity(), getActivity());
        notificationsSender.SendNotifications();
    }
}
