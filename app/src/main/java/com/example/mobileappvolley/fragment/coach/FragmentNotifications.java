package com.example.mobileappvolley.fragment.coach;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Token;
import com.example.mobileappvolley.Notifications.FcmNotificationsSender;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentNotificationsCoachBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class FragmentNotifications extends Fragment {
    private static final String ARG_PARAM1 = "idUser";
    private String idUser;
    private Token token = new Token();
    private FragmentNotificationsCoachBinding fragmentNotificationsCoachBinding;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNotificationsCoachBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications_coach, container, false);
        fragmentNotificationsCoachBinding.setCallback(this);
        View view = fragmentNotificationsCoachBinding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();
        //TODO:Send message to all
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        return view;
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getActivity(), AuthActivity.class));
        }
    }

    public void onClickSendAll() {
        System.out.println("Send message!");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> notification = new HashMap<>();
        notification.put("title",fragmentNotificationsCoachBinding.editTextTitle.getText().toString());
        notification.put("description",fragmentNotificationsCoachBinding.editTextMessage.getText().toString());
        notification.put("id","all");
        db.collection("Notifications").add(notification);

        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                fragmentNotificationsCoachBinding.editTextTitle.getText().toString(),
                fragmentNotificationsCoachBinding.editTextMessage.getText().toString(),
                getActivity(), getActivity());
        notificationsSender.SendNotifications();
    }

}
