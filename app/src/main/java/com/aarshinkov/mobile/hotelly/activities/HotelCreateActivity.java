package com.aarshinkov.mobile.hotelly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aarshinkov.mobile.hotelly.R;

public class HotelCreateActivity extends AppCompatActivity {

    private ImageView hotelCreateImageIV;
    private EditText hotelCreateNameET;
    private Spinner hotelCreateCountrySpinner;
    private EditText hotelCreateCityET;
    private EditText hotelCreateStreetET;
    private EditText hotelCreateNumberET;
    private EditText hotelCreateStarsET;
    private Button hotelCreateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_create);

        getSupportActionBar().setTitle("Create a hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hotelCreateImageIV = findViewById(R.id.hotelCreateImageIV);
        hotelCreateNameET = findViewById(R.id.hotelCreateNameET);
        hotelCreateCountrySpinner = findViewById(R.id.hotelCreateCountrySpinner);
        hotelCreateCityET = findViewById(R.id.hotelCreateCityET);
        hotelCreateStreetET = findViewById(R.id.hotelCreateStreetET);
        hotelCreateNumberET = findViewById(R.id.hotelCreateNumberET);
        hotelCreateStarsET = findViewById(R.id.hotelCreateStarsET);
        hotelCreateBtn = findViewById(R.id.hotelCreateBtn);

        hotelCreateImageIV.setOnLongClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Image", Toast.LENGTH_LONG).show();
            return false;
        });

        hotelCreateBtn.setOnClickListener(v -> {

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}