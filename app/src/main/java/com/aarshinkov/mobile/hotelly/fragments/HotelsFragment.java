package com.aarshinkov.mobile.hotelly.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.adapters.HotelAdapter;

import java.util.ArrayList;
import java.util.List;

public class HotelsFragment extends Fragment {

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private List<String> hotels;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_hotels, container, false);

        List<String> hotels = getHotels();

        recyclerView = root.findViewById(R.id.hotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotelAdapter = new HotelAdapter(getContext(), hotels);
        recyclerView.setAdapter(hotelAdapter);

        return root;
    }

    private List<String> getHotels() {

        List<String> hotels = new ArrayList<>();
        hotels.add("Hotel 1");
        hotels.add("Hotel 2");
        hotels.add("Hotel 3");
        hotels.add("Hotel 4");

        return hotels;
    }
}