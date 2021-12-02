package com.example.mobileappvolley.fragment.coach;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobileappvolley.Model.Player;
import com.example.mobileappvolley.Model.Token;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.activity.AuthActivity;
import com.example.mobileappvolley.databinding.FragmentPlayerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PlayerFragment extends Fragment {
    private FragmentPlayerBinding fragmentPlayerBinding;
    private FirebaseAuth firebaseAuth;
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "id";
    private static final String ARG_PARAM3 = "position";
    private static final String ARG_PARAM4 = "blockRange";
    private static final String ARG_PARAM5 = "attackRange";
    private static final String ARG_PARAM6 = "height";
    private static final String ARG_PARAM7 = "weight";
    private static final String ARG_PARAM8 = "age";
    private static final String ARG_PARAM9 = "idUser";
    private final Player player = new Player();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        fragmentPlayerBinding.setCallback(this);
        View view = fragmentPlayerBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();

        Typeface face = ResourcesCompat.getFont(getActivity(), R.font.dongle_regular);
        TextView textViewName = new TextView(getActivity());
        textViewName.setTextSize(50);
        textViewName.setPadding(0, 10, 0, 0);
        textViewName.setTextColor(Color.parseColor("#FFFFFF"));
        textViewName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewName.setTypeface(face);
        textViewName.setText(player.getName());
        fragmentPlayerBinding.infoPlayerContainer.addView(textViewName);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        setAttibutesLinearLayout(linearLayout);
        EditText editText = new EditText(getActivity());
        setAttributesEditText(face,editText);
        editText.setId(R.id.position);
        editText.setText(player.getPosition());
        TextView textView = new TextView(getActivity());
        setAttributesTextView(face, textView);
        textView.setText("Position: ");
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        fragmentPlayerBinding.infoPlayerContainer.addView(linearLayout);

        linearLayout = new LinearLayout(getActivity());
        setAttibutesLinearLayout(linearLayout);
        editText = new EditText(getActivity());
        setAttributesEditText(face,editText);
        editText.setId(R.id.age);
        editText.setText(String.valueOf(player.getAge()));
        textView = new TextView(getActivity());
        setAttributesTextView(face, textView);
        textView.setText("Age: ");
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        fragmentPlayerBinding.infoPlayerContainer.addView(linearLayout);

        linearLayout = new LinearLayout(getActivity());
        setAttibutesLinearLayout(linearLayout);
        editText = new EditText(getActivity());
        setAttributesEditText(face,editText);
        editText.setId(R.id.attackRange);
        editText.setText(String.valueOf(player.getAttackRange()));
        textView = new TextView(getActivity());
        setAttributesTextView(face, textView);
        textView.setText("Attack range: ");
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        fragmentPlayerBinding.infoPlayerContainer.addView(linearLayout);

        linearLayout = new LinearLayout(getActivity());
        setAttibutesLinearLayout(linearLayout);
        editText = new EditText(getActivity());
        setAttributesEditText(face,editText);
        editText.setId(R.id.blockRange);
        editText.setText(String.valueOf(player.getBlockRange()));
        textView = new TextView(getActivity());
        setAttributesTextView(face, textView);
        textView.setText("Block range: ");
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        fragmentPlayerBinding.infoPlayerContainer.addView(linearLayout);

        linearLayout = new LinearLayout(getActivity());
        setAttibutesLinearLayout(linearLayout);
        editText = new EditText(getActivity());
        setAttributesEditText(face,editText);
        editText.setId(R.id.height);
        editText.setText(String.valueOf(player.getHeight()));
        textView = new TextView(getActivity());
        setAttributesTextView(face, textView);
        textView.setText("Height: ");
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        fragmentPlayerBinding.infoPlayerContainer.addView(linearLayout);

        linearLayout = new LinearLayout(getActivity());
        setAttibutesLinearLayout(linearLayout);
        editText = new EditText(getActivity());
        setAttributesEditText(face,editText);
        editText.setId(R.id.weight);
        editText.setText(String.valueOf(player.getWeight()));
        textView = new TextView(getActivity());
        setAttributesTextView(face, textView);
        textView.setText("Weight: ");
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        fragmentPlayerBinding.infoPlayerContainer.addView(linearLayout);

        return view;
    }

    private void setAttibutesLinearLayout(LinearLayout linearLayout) {
        linearLayout.setPadding(0, 10, 0, 10);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void setAttributesTextView(Typeface face, TextView textView) {
        textView.setTextSize(35);
        textView.setPadding(100, 10, 0, 10);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        textView.setTypeface(face);
    }

    private void setAttributesEditText(Typeface face, EditText editText) {
        editText.setTextSize(35);
        editText.setPadding(30, 0, 30, 0);
        editText.setTextColor(Color.parseColor("#FFFFFF"));
        editText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        editText.setTypeface(face);
        editText.setClickable(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setBackgroundResource(R.drawable.round_field_background);
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getActivity(), AuthActivity.class));
        }
    }

    public static PlayerFragment newInstance(String param1, String param2, String param3,String param4, String param5, String param6,String param7, String param8,String param9){
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        args.putString(ARG_PARAM7, param7);
        args.putString(ARG_PARAM8, param8);
        args.putString(ARG_PARAM9, param9);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            player.setName(getArguments().getString(ARG_PARAM1));
            player.setId(Integer.parseInt(getArguments().getString(ARG_PARAM2)));
            player.setPosition(getArguments().getString(ARG_PARAM3));
            player.setBlockRange(Integer.parseInt(getArguments().getString(ARG_PARAM4)));
            player.setAttackRange(Integer.parseInt(getArguments().getString(ARG_PARAM5)));
            player.setHeight(Integer.parseInt(getArguments().getString(ARG_PARAM6)));
            player.setWeight(Integer.parseInt(getArguments().getString(ARG_PARAM7)));
            player.setAge(Integer.parseInt(getArguments().getString(ARG_PARAM8)));
            player.setIdUser(getArguments().getString(ARG_PARAM9));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public void onClickSave(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText age = getActivity().findViewById(R.id.age);
        EditText attackRange = getActivity().findViewById(R.id.attackRange);
        EditText blockRange = getActivity().findViewById(R.id.blockRange);
        EditText height = getActivity().findViewById(R.id.height);
        EditText weight = getActivity().findViewById(R.id.weight);
        EditText position = getActivity().findViewById(R.id.position);
        Map<String, Object> playerP = new HashMap<>();
        playerP.put("age",Integer.parseInt(age.getText().toString()));
        playerP.put("attackRange",Integer.parseInt(attackRange.getText().toString()));
        playerP.put("blockRange",Integer.parseInt(blockRange.getText().toString()));
        playerP.put("height",Integer.parseInt(height.getText().toString()));
        playerP.put("position",position.getText().toString());
        playerP.put("weight",Integer.parseInt(weight.getText().toString()));

    db.collection("Players").document(String.valueOf(player.getId())).update(playerP).addOnCompleteListener(task -> {
     if(task.isSuccessful()){
         Toast.makeText(getActivity(),"Values added!",Toast.LENGTH_SHORT).show();
            fragmentPlayerBinding.save.setBackgroundResource(R.drawable.save_green);
     }
     else{
         Toast.makeText(getActivity(),"Error!",Toast.LENGTH_SHORT).show();
     }
    });

    }

    public void onClickSend(){
        FragmentNotifications myFragment= FragmentNotifications.newInstance(player.getIdUser());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, myFragment).addToBackStack("okj").commit();
    }
}
