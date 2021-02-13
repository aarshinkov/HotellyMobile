package com.aarshinkov.mobile.hotelly.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aarshinkov.mobile.hotelly.R;

public class RegisterFragment extends Fragment {

    private TextView registerHaveAccountTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register, container, false);

        registerHaveAccountTV = root.findViewById(R.id.registerHaveAccountTV);

        registerHaveAccountTV.setOnClickListener(v -> {
            getActivity().findViewById(R.id.nav_view).findViewById(R.id.nav_login).performClick();
        });

        return root;
    }
}