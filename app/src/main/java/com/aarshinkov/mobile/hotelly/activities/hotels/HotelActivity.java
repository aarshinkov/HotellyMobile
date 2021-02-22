package com.aarshinkov.mobile.hotelly.activities.hotels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.activities.MainActivity;
import com.aarshinkov.mobile.hotelly.api.HotelsApi;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.aarshinkov.mobile.hotelly.utils.Utils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.aarshinkov.mobile.hotelly.api.Api.getRetrofit;
import static com.aarshinkov.mobile.hotelly.utils.Constants.BASE_URL;

public class HotelActivity extends AppCompatActivity {

    private ImageView hotelImageIV;
    private TextView hotelNameTV;
    private RatingBar hotelStarsRB;
    private TextView hotelAddressTV;
    private TextView hotelDescriptionTV;
    private ProgressDialog dialog;

    private Activity parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        getSupportActionBar().setTitle("Hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hotelImageIV = findViewById(R.id.hotelImageIV);
        hotelNameTV = findViewById(R.id.hotelNameTV);
        hotelStarsRB = findViewById(R.id.hotelStarsRB);
        hotelAddressTV = findViewById(R.id.hotelAddressTV);
        hotelDescriptionTV = findViewById(R.id.hotelDescriptionTV);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading hotel...");
        dialog.show();

        String hotelId = getIntent().getStringExtra("hotelId");

        Retrofit retrofit = getRetrofit();

        HotelsApi hotelsApi = retrofit.create(HotelsApi.class);

        hotelsApi.getHotel(hotelId).enqueue(new Callback<HotelGetResponse>() {
            @Override
            public void onResponse(Call<HotelGetResponse> call, Response<HotelGetResponse> response) {

                parent = getParent();

                HotelGetResponse hotel = response.body();

                String imageUrl = BASE_URL + "images/hotels/" + hotel.getMainImage();
                Picasso.get().load(imageUrl).into(hotelImageIV);

                hotelNameTV.setText(hotel.getName());
                hotelStarsRB.setRating(20);
                hotelStarsRB.setNumStars(hotel.getStars());

                StringBuilder builder = new StringBuilder();
                builder.append(hotel.getStreet()).append(" ");
                builder.append(hotel.getNumber()).append(", ");
                builder.append(hotel.getCity()).append(", ");
                builder.append(Utils.getStringResource(getApplicationContext(), "country_" + hotel.getCountryCode()));

                String addressText = String.valueOf(builder);

                hotelAddressTV.setText(addressText);

                hotelDescriptionTV.setText(Html.fromHtml(hotel.getDescription()));

                dialog.hide();
            }

            @Override
            public void onFailure(Call<HotelGetResponse> call, Throwable t) {
                dialog.hide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.hotel_operations, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.hotel_action_edit) {
            Toast.makeText(getApplicationContext(), "EDIT", Toast.LENGTH_LONG).show();
            return false;
        } else if (item.getItemId() == R.id.hotel_action_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HotelActivity.this);

            builder.setTitle("Delete hotel.");

            builder.setMessage("Are you sure you want to delete this hotel?");

            ProgressDialog loadingDialog = new ProgressDialog(this);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setMessage("Deleting hotel...");

            builder.setPositiveButton("Yes", (dialog, which) -> {

                loadingDialog.show();

                Retrofit retrofit = getRetrofit();

                HotelsApi hotelsApi = retrofit.create(HotelsApi.class);

                String hotelId = getIntent().getStringExtra("hotelId");

                hotelsApi.deleteHotel(hotelId).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        if (response.code() == 404) {
                            Toast.makeText(getApplicationContext(), "Hotel not found", Toast.LENGTH_LONG).show();
                            loadingDialog.hide();
                            return;
                        }

                        Boolean result = response.body();

                        if (result) {
                            loadingDialog.hide();
                            Toast.makeText(getApplicationContext(), "Hotel deleted successfully!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
//                            onBackPressed();
//                            parent.findViewById(R.id.nav_view).findViewById(R.id.nav_hotels).performClick();
                            return;
                        }

                        Toast.makeText(getApplicationContext(), "Error deleting the hotel!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error deleting the hotel!", Toast.LENGTH_LONG).show();
                        loadingDialog.hide();
                    }
                });
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            });

            AlertDialog dialog = builder.create();

            dialog.show();
            return false;
        }

        onBackPressed();
        return true;
    }
}