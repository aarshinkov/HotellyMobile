package com.aarshinkov.mobile.hotelly.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.adapters.HotelAdapter;
import com.aarshinkov.mobile.hotelly.api.HotelsApi;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.aarshinkov.mobile.hotelly.utils.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotelsFragment extends Fragment {

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private List<HotelGetResponse> hotels;
    private ProgressDialog loadingDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_hotels, container, false);

        recyclerView = root.findViewById(R.id.hotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hotels = new ArrayList<>();
        hotelAdapter = new HotelAdapter(getContext(), hotels);
        recyclerView.setAdapter(hotelAdapter);

        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setMessage("Loading hotels...");
        loadingDialog.show();

        Retrofit retrofit = Api.getRetrofit();

        HotelsApi hotelsApi = retrofit.create(HotelsApi.class);

        hotelsApi.getHotels().enqueue(new Callback<List<HotelGetResponse>>() {
            @Override
            public void onResponse(Call<List<HotelGetResponse>> call, Response<List<HotelGetResponse>> response) {
                List<HotelGetResponse> storedHotels = response.body();

                hotels.addAll(storedHotels);
                hotelAdapter.notifyDataSetChanged();
                loadingDialog.hide();
            }

            @Override
            public void onFailure(Call<List<HotelGetResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Error getting hotels", Toast.LENGTH_SHORT).show();
                loadingDialog.hide();
            }
        });

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