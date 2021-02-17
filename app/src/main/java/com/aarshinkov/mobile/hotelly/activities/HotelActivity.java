package com.aarshinkov.mobile.hotelly.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.api.HotelsApi;
import com.aarshinkov.mobile.hotelly.responses.hotels.AddressGetResponse;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.aarshinkov.mobile.hotelly.utils.Api;
import com.aarshinkov.mobile.hotelly.utils.Utils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.aarshinkov.mobile.hotelly.utils.Constants.BASE_URL;

public class HotelActivity extends AppCompatActivity {

    private ImageView hotelImageIV;
    private TextView hotelNameTV;
    private RatingBar hotelStarsRB;
    private TextView hotelAddressTV;
    private TextView hotelDescriptionTV;
    private ProgressDialog dialog;

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

        Retrofit retrofit = Api.getRetrofit();

        HotelsApi hotelsApi = retrofit.create(HotelsApi.class);

        hotelsApi.getHotel(hotelId).enqueue(new Callback<HotelGetResponse>() {
            @Override
            public void onResponse(Call<HotelGetResponse> call, Response<HotelGetResponse> response) {

                HotelGetResponse hotel = response.body();

                String imageUrl = BASE_URL + "images/hotels/" + hotel.getMainImage();
                Picasso.get().load(imageUrl).into(hotelImageIV);

                hotelNameTV.setText(hotel.getName());
                hotelStarsRB.setRating(20);
                hotelStarsRB.setNumStars(hotel.getStars());

                AddressGetResponse address = hotel.getAddress();
                StringBuilder builder = new StringBuilder();
                builder.append(address.getStreet()).append(" ");
                builder.append(address.getNumber()).append(", ");
                builder.append(address.getCity()).append(", ");
                builder.append(Utils.getStringResource(getApplicationContext(), "country_" + address.getCountryCode()));

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}