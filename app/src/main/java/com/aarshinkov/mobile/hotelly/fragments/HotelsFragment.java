package com.aarshinkov.mobile.hotelly.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.aarshinkov.mobile.hotelly.activities.hotels.HotelCreateActivity;
import com.aarshinkov.mobile.hotelly.adapters.HotelAdapter;
import com.aarshinkov.mobile.hotelly.api.Api;
import com.aarshinkov.mobile.hotelly.api.HotelsApi;
import com.aarshinkov.mobile.hotelly.db.DBHelper;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private CircularProgressIndicator progress;

    private DBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_hotels, container, false);

        dbHelper = new DBHelper(getContext());

        recyclerView = root.findViewById(R.id.hotels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hotelsCountTV = root.findViewById(R.id.hotelsCountTV);
        hotelsCheckBtn = root.findViewById(R.id.hotelsCheckBtn);
        hotelsAddHotelBtn = root.findViewById(R.id.hotelsAddHotelBtn);

        hotels = new ArrayList<>();
        hotelAdapter = new HotelAdapter(getContext(), hotels);
        recyclerView.setAdapter(hotelAdapter);

        progress = root.findViewById(R.id.hotelsProgress);
        progress.setVisibility(View.VISIBLE);

        Retrofit retrofit = Api.getRetrofit();

        HotelsApi hotelsApi = retrofit.create(HotelsApi.class);

        hotelsApi.getHotels().enqueue(new Callback<List<HotelGetResponse>>() {
            @Override
            public void onResponse(Call<List<HotelGetResponse>> call, Response<List<HotelGetResponse>> response) {
                List<HotelGetResponse> storedHotels = response.body();

                for (HotelGetResponse storedHotel : storedHotels) {
                    dbHelper.insertHotel(storedHotel);
                }

                List<HotelGetResponse> localHotels = dbHelper.getHotels();

                hotels.addAll(localHotels);
                hotelAdapter.notifyDataSetChanged();
                hotelsCountTV.setText(String.valueOf(hotelAdapter.getItemCount()));
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<HotelGetResponse>> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_server, Toast.LENGTH_LONG).show();
                hotelsCountTV.setText("0");
                progress.setVisibility(View.GONE);
            }
        });

        hotelsCheckBtn.setOnClickListener(v -> {

            List<HotelGetResponse> localHotels = dbHelper.getHotels();

            for (HotelGetResponse hotel : localHotels) {

                hotelsApi.getHotel(hotel.getHotelId()).enqueue(new Callback<HotelGetResponse>() {
                    @Override
                    public void onResponse(Call<HotelGetResponse> call, Response<HotelGetResponse> response) {

                        if (response.code() == 404) {
                            return;
                        }

                        HotelGetResponse dbHotel = response.body();

                        Log.i("Local hotel", hotel.getName());
                        Log.i("DB hotel", dbHotel.getName());

                        if (hasChanges(hotel, dbHotel)) {

                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("Changes detected for hotel \"" + hotel.getName() + "\"");
                            alertDialog.setMessage("What do you want to do with the changes?");

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update locally", (dialog, which) -> {
                                dbHelper.updateHotel(dbHotel);
                                Toast.makeText(getContext(), "Hotel updated locally.", Toast.LENGTH_SHORT).show();
                                getActivity().findViewById(R.id.nav_view).findViewById(R.id.nav_hotels).performClick();
                            });

                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Do nothing", (dialog, which) -> {
//                                Toast.makeText(getContext(), "Update canceled", Toast.LENGTH_LONG).show();
                            });

                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Duplicate", (dialog, which) -> {
                                Toast.makeText(getContext(), "Duplicate", Toast.LENGTH_LONG).show();
                                dbHotel.setHotelId(UUID.randomUUID().toString());
                                dbHelper.insertHotel(dbHotel);

                                hotels.clear();
                                hotels.addAll(dbHelper.getHotels());
                                hotelAdapter.notifyDataSetChanged();
                            });

                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HotelGetResponse> call, Throwable t) {

                    }
                });
            }

//            hotels.addAll(localHotels);
//            hotelAdapter.notifyDataSetChanged();
        });

        hotelsAddHotelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HotelCreateActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private boolean hasChanges(HotelGetResponse hotel, HotelGetResponse dbHotel) {

        boolean result = false;

        if (!hotel.getName().equals(dbHotel.getName())) {
            result = true;
        }

        if (!hotel.getDescription().equals(dbHotel.getDescription())) {
            result = true;
        }

        if (!hotel.getCountryCode().equals(dbHotel.getCountryCode())) {
            result = true;
        }

        if (!hotel.getCity().equals(dbHotel.getCity())) {
            result = true;
        }

        if (!hotel.getStreet().equals(dbHotel.getStreet())) {
            result = true;
        }

        if (!hotel.getNumber().equals(dbHotel.getNumber())) {
            result = true;
        }

        if (!hotel.getStars().equals(dbHotel.getStars())) {
            result = true;
        }

        if (!hotel.getMainImage().equals(dbHotel.getMainImage())) {
            result = true;
        }

        return result;
    }
}