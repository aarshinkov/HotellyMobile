package com.aarshinkov.mobile.hotelly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.api.HotelsApi;
import com.aarshinkov.mobile.hotelly.requests.hotels.HotelCreateRequest;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.aarshinkov.mobile.hotelly.responses.users.UserGetResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.aarshinkov.mobile.hotelly.api.Api.getRetrofit;
import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_NAME;
import static com.aarshinkov.mobile.hotelly.utils.Utils.getLoggedUser;

public class HotelCreateActivity extends AppCompatActivity {

    private ImageView hotelCreateImageIV;
    private EditText hotelCreateNameET;
    private EditText hotelCreateDescriptionET;
    private Spinner hotelCreateCountrySpinner;
    private EditText hotelCreateCityET;
    private EditText hotelCreateStreetET;
    private EditText hotelCreateNumberET;
    private EditText hotelCreateStarsET;
    private Button hotelCreateBtn;

    private ProgressDialog loadingDialog;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_create);

        getSupportActionBar().setTitle("Create a hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        hotelCreateImageIV = findViewById(R.id.hotelCreateImageIV);
        hotelCreateNameET = findViewById(R.id.hotelCreateNameET);
        hotelCreateCountrySpinner = findViewById(R.id.hotelCreateCountrySpinner);
        hotelCreateCityET = findViewById(R.id.hotelCreateCityET);
        hotelCreateStreetET = findViewById(R.id.hotelCreateStreetET);
        hotelCreateNumberET = findViewById(R.id.hotelCreateNumberET);
        hotelCreateStarsET = findViewById(R.id.hotelCreateStarsET);
        hotelCreateDescriptionET = findViewById(R.id.hotelCreateDescriptionET);
        hotelCreateBtn = findViewById(R.id.hotelCreateBtn);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setMessage("Creating hotel...");

        hotelCreateImageIV.setOnLongClickListener(v -> {
//            Toast.makeText(getApplicationContext(), "Image", Toast.LENGTH_LONG).show();

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 100);
            return true;
        });

        hotelCreateBtn.setOnClickListener(v -> {

            loadingDialog.show();

            Bitmap bitmap = ((BitmapDrawable) hotelCreateImageIV.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] imageInByte = baos.toByteArray();
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String name = hotelCreateNameET.getText().toString();
//            String countryCode = (String) hotelCreateCountrySpinner.getSelectedItem();
            String countryCode = "bg";
            String city = hotelCreateCityET.getText().toString();
            String street = hotelCreateStreetET.getText().toString();
            Integer number = Integer.parseInt(hotelCreateNumberET.getText().toString());
            Integer stars = Integer.parseInt(hotelCreateStarsET.getText().toString());
            String description = hotelCreateDescriptionET.getText().toString();

            HotelCreateRequest hcr = new HotelCreateRequest();
            hcr.setName(name);
            hcr.setCountryCode(countryCode);
            hcr.setCity(city);
            hcr.setStreet(street);
            hcr.setNumber(number);
            hcr.setStars(stars);
            hcr.setMainImage(imageInByte);
            hcr.setDescription(description);

            UserGetResponse loggedUser = getLoggedUser(pref);
            hcr.setUserId(loggedUser.getUserId());

            Retrofit retrofit = getRetrofit();

            HotelsApi hotelsApi = retrofit.create(HotelsApi.class);

            hotelsApi.createHotel(hcr).enqueue(new Callback<HotelGetResponse>() {
                @Override
                public void onResponse(Call<HotelGetResponse> call, Response<HotelGetResponse> response) {
                    Toast.makeText(getApplicationContext(), "CREATED MAYBE", Toast.LENGTH_LONG).show();
                    loadingDialog.hide();

                    getParent().findViewById(R.id.nav_view).findViewById(R.id.nav_hotels).performClick();
                }

                @Override
                public void onFailure(Call<HotelGetResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "ERROR CREATING HOTEL", Toast.LENGTH_LONG).show();
                    loadingDialog.hide();
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == 100 && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                hotelCreateImageIV.setImageBitmap(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}