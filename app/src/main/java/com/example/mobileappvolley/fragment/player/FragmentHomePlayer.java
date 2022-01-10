package com.example.mobileappvolley.fragment.player;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.GlideApp;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentHomePlayerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FragmentHomePlayer extends Fragment {
    private FragmentHomePlayerBinding fragmentHomePlayerBinding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Integer positionId;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomePlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_player, container, false);
        fragmentHomePlayerBinding.setCallback(this);
        View view = fragmentHomePlayerBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();
        getPlayerInfo();


        return view;
    }

    private void getPlayerInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        CollectionReference ref = db.collection("Players");

        Query query = ref.whereEqualTo("idUser", firebaseUser.getUid());

        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    System.out.println("DocumentSnapshot data: " + document.getData());
                    setTextView(document.getString("name"));
                    int temp = document.getLong("age").intValue();
                    setTextView("Age: " + temp);
                    temp = document.getLong("attackRange").intValue();
                    setTextView("Attack range: " + temp + " cm");
                    temp = document.getLong("blockRange").intValue();
                    setTextView("Block range: " + temp+ " cm");
                    temp = document.getLong("height").intValue();
                    setTextView("Height: " + temp+ " cm");
                    temp = document.getLong("weight").intValue();
                    setTextView("Weight: " + temp + " kg");
                    setTextView("Position: " + document.getString("position"));


                    String pathURL = "gs://mobileappvolley-16965.appspot.com/" + (document.getLong("id").intValue()) + ".jpg";
                    StorageReference gsReference = storage.getReferenceFromUrl(pathURL);

                    GlideApp.with(getContext())
                            .load(gsReference)
                            .into(fragmentHomePlayerBinding.photo);
                }
            }
            else{
                System.out.println("get failed with " + task.getException());
            }
        });


    }

    private void setTextView(String text) {
        TextView textView = new TextView(getActivity());
        textView.setTextSize(40);
        textView.setPadding(60,0,20,0);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        Typeface face = ResourcesCompat.getFont(getActivity(),R.font.dongle_regular);
        textView.setTypeface(face);
        textView.setText(text);
        fragmentHomePlayerBinding.cardPlayer.addView(textView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getActivity(), AuthActivity.class));
        }
    }

}
