package com.example.mexpenses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mexpenses.sqlite.Database;
import com.example.mexpenses.sqlite.RoomH;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashFragment extends Fragment {


    public SplashFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate();
            }
        }, 2000);

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
    private void navigate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Navigation.findNavController(getView()).navigate(user == null ? R.id.signInFragment : R.id.mainFragment);
    }
}