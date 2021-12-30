package com.example.mobileappvolley.fragment.auth;

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

import com.example.mobileappvolley.CallbackFragment;
import com.example.mobileappvolley.activity.MainActivity;
import com.example.mobileappvolley.R;
import com.example.mobileappvolley.databinding.FragmentLoginBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentLogin extends Fragment{
    private FragmentLoginBinding loginFragmentBinding;
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    CallbackFragment callbackFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        loginFragmentBinding.setCallback(this);
        View view = loginFragmentBinding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        return view;
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void firebaseLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void onSubmit() {
        if (!loginFragmentBinding.inputEmailText.getText().toString().isEmpty() &&
                !loginFragmentBinding.editTextPassword.getText().toString().isEmpty()) {
            firebaseLogin(loginFragmentBinding.inputEmailText.getText().toString(), loginFragmentBinding.editTextPassword.getText().toString());
        }

    }

    public void onSignUpTextClick() {
        if (callbackFragment != null) {
            callbackFragment.changeFragment();
        }
    }

    public void setCallbackFragment(CallbackFragment callbackFragment) {
        this.callbackFragment = callbackFragment;
    }
}
