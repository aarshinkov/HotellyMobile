package com.aarshinkov.mobile.hotelly.activities.hotels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.activities.MainActivity;
import com.aarshinkov.mobile.hotelly.db.DBHelper;
import com.aarshinkov.mobile.hotelly.requests.hotels.HotelCreateRequest;
import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;
import com.aarshinkov.mobile.hotelly.responses.users.UserGetResponse;

import static com.aarshinkov.mobile.hotelly.utils.Utils.getLoggedUser;

public class HotelUpdateActivity extends AppCompatActivity {

    private EditText hotelUpdateNameET;
    private EditText hotelUpdateDescriptionET;
    private Spinner hotelUpdateCountrySpinner;
    private EditText hotelUpdateCityET;
    private EditText hotelUpdateStreetET;
    private EditText hotelUpdateNumberET;
    private EditText hotelUpdateStarsET;
    private Button hotelUpdateBtn;

    private ProgressDialog loadingDialog;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_update);

        getSupportActionBar().setTitle("Edit hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());

        hotelUpdateNameET = findViewById(R.id.hotelUpdateNameET);
        hotelUpdateCountrySpinner = findViewById(R.id.hotelUpdateCountrySpinner);
        hotelUpdateCityET = findViewById(R.id.hotelUpdateCityET);
        hotelUpdateStreetET = findViewById(R.id.hotelUpdateStreetET);
        hotelUpdateNumberET = findViewById(R.id.hotelUpdateNumberET);
        hotelUpdateStarsET = findViewById(R.id.hotelUpdateStarsET);
        hotelUpdateDescriptionET = findViewById(R.id.hotelUpdateDescriptionET);
        hotelUpdateBtn = findViewById(R.id.hotelUpdateBtn);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setMessage("Updating hotel...");

        String hotelId = getIntent().getStringExtra("hotelId");

        HotelGetResponse hotel = dbHelper.getHotel(hotelId);

        hotelUpdateNameET.setText(hotel.getName());
        hotelUpdateCityET.setText(hotel.getCity());
        hotelUpdateStreetET.setText(hotel.getStreet());
        hotelUpdateNumberET.setText(String.valueOf(hotel.getNumber()));
        hotelUpdateStarsET.setText(String.valueOf(hotel.getStars()));
        hotelUpdateDescriptionET.setText(hotel.getDescription());

        hotelUpdateBtn.setOnClickListener(v -> {

            loadingDialog.show();

            if (isFieldsValid()) {
                loadingDialog.hide();
                return;
            }

            String name = hotelUpdateNameET.getText().toString();
//            String countryCode = (String) hotelUpdateCountrySpinner.getSelectedItem();
            String countryCode = "bg";
            String city = hotelUpdateCityET.getText().toString();
            String street = hotelUpdateStreetET.getText().toString();
            Integer number = Integer.parseInt(hotelUpdateNumberET.getText().toString());
            Integer stars = Integer.parseInt(hotelUpdateStarsET.getText().toString());
            String description = hotelUpdateDescriptionET.getText().toString();

            HotelGetResponse hgr = new HotelGetResponse();
            hgr.setHotelId(hotelId);
            hgr.setName(name);
            hgr.setCountryCode(countryCode);
            hgr.setCity(city);
            hgr.setStreet(street);
            hgr.setNumber(number);
            hgr.setStars(stars);
//            hur.setMainImage(imageInByte);
            hgr.setDescription(description);

            dbHelper.updateHotel(hgr);
            loadingDialog.hide();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("page", "hotels");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    private boolean isFieldsValid() {

        boolean hasErrors = false;

        String name = hotelUpdateNameET.getText().toString();
        String city = hotelUpdateCityET.getText().toString();
        String street = hotelUpdateStreetET.getText().toString();
        Integer number = null;

        try {
            number = Integer.parseInt(hotelUpdateNumberET.getText().toString());
        } catch (NumberFormatException ignore) {

        }

        Integer stars = null;
        try {
            stars = Integer.parseInt(hotelUpdateStarsET.getText().toString());

            if (stars < 0 || stars > 8) {
                hotelUpdateStarsET.setError("Stars of hotel must be between 0 and 8");
                hasErrors = true;
            }
        } catch (NumberFormatException ignore) {

        }

        String description = hotelUpdateDescriptionET.getText().toString();

        if (name.isEmpty()) {
            hotelUpdateNameET.setError("Name must not be empty");
            hasErrors = true;
        }

        if (city.isEmpty()) {
            hotelUpdateCityET.setError("City must not be empty");
            hasErrors = true;
        }

        if (street.isEmpty()) {
            hotelUpdateStreetET.setError("Street must not be empty");
            hasErrors = true;
        }

        if (number == null) {
            hotelUpdateNumberET.setError("Number must not be empty");
            hasErrors = true;
        }

        if (stars == null) {
            hotelUpdateStarsET.setError("Stars must not be empty");
            hasErrors = true;
        }

        if (description.isEmpty()) {
            hotelUpdateDescriptionET.setError("Description must not be empty");
            hasErrors = true;
        }

        return hasErrors;
    }
}