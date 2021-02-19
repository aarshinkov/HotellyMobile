package com.aarshinkov.mobile.hotelly.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.activities.HotelCreateActivity;
import com.aarshinkov.mobile.hotelly.adapters.HotelAdapter;
import com.aarshinkov.mobile.hotelly.api.HotelsApi;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.aarshinkov.mobile.hotelly.utils.Api;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotelsFragment extends Fragment {

    private RecyclerView recyclerView;
    private HotelAdapter hotelAdapter;
    private TextView hotelsCountTV;
    private List<HotelGetResponse> hotels;
    private Button hotelsCheckBtn;
    private Button hotelsAddHotelBtn;
    private ProgressDialog loadingDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_hotels, container, false);

        recyclerView = root.findViewById(R.id.hotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hotelsCountTV = root.findViewById(R.id.hotelsCountTV);
//        hotelsCheckBtn = root.findViewById(R.id.hotelsCheckBtn);
        hotelsAddHotelBtn = root.findViewById(R.id.hotelsAddHotelBtn);

        hotels = new ArrayList<>();
        hotelAdapter = new HotelAdapter(getContext(), hotels);
        recyclerView.setAdapter(hotelAdapter);

        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setCanceledOnTouchOutside(false);
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
                hotelsCountTV.setText(String.valueOf(hotelAdapter.getItemCount()));
                loadingDialog.hide();
            }

            @Override
            public void onFailure(Call<List<HotelGetResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Error getting hotels", Toast.LENGTH_SHORT).show();
                hotelsCountTV.setText("0");
                loadingDialog.hide();
            }
        });

        hotelsAddHotelBtn.setOnClickListener(v -> {
//            Snackbar.make(v, "Here", BaseTransientBottomBar.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), HotelCreateActivity.class);
            startActivity(intent);
        });

        return root;
    }
}