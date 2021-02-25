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
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.activities.MainActivity;
import com.aarshinkov.mobile.hotelly.api.HotelsApi;
import com.aarshinkov.mobile.hotelly.db.DBHelper;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.aarshinkov.mobile.hotelly.utils.Utils;
import com.google.android.material.progressindicator.CircularProgressIndicator;
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

    private CircularProgressIndicator progress;

    private Activity parent;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        getSupportActionBar().setTitle("Hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());

        hotelImageIV = findViewById(R.id.hotelImageIV);
        hotelNameTV = findViewById(R.id.hotelNameTV);
        hotelStarsRB = findViewById(R.id.hotelStarsRB);
        hotelAddressTV = findViewById(R.id.hotelAddressTV);
        hotelDescriptionTV = findViewById(R.id.hotelDescriptionTV);

        progress = findViewById(R.id.hotelProgress);
        progress.setVisibility(View.VISIBLE);

        String hotelId = getIntent().getStringExtra("hotelId");

        parent = getParent();

        HotelGetResponse hotel = dbHelper.getHotel(hotelId);

        String imageUrl;
        if (hotel.getMainImage() == null) {
            imageUrl = BASE_URL + "images/hotels/unknown.jpg";
        } else {
            imageUrl = BASE_URL + "images/hotels/" + hotel.getMainImage();
        }
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

        progress.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.hotel_operations, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.hotel_action_edit) {
            Intent intent = new Intent(getApplicationContext(), HotelUpdateActivity.class);
            String hotelId = getIntent().getStringExtra("hotelId");
            intent.putExtra("hotelId", hotelId);
            startActivity(intent);
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

                String hotelId = getIntent().getStringExtra("hotelId");

                dbHelper.deleteHotel(hotelId);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("page", "hotels");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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