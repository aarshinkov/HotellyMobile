package com.aarshinkov.mobile.hotelly.activities.hotels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.activities.MainActivity;
import com.aarshinkov.mobile.hotelly.db.DBHelper;
import com.aarshinkov.mobile.hotelly.requests.hotels.HotelCreateRequest;
import com.aarshinkov.mobile.hotelly.responses.users.UserGetResponse;

import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_NAME;
import static com.aarshinkov.mobile.hotelly.utils.Utils.getLoggedUser;

public class HotelCreateActivity extends AppCompatActivity {

    //    private ImageView hotelCreateImageIV;
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

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_create);

        getSupportActionBar().setTitle("Create a hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());

        pref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

//        hotelCreateImageIV = findViewById(R.id.hotelCreateImageIV);
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

//        hotelCreateImageIV.setOnLongClickListener(v -> {
////            Toast.makeText(getApplicationContext(), "Image", Toast.LENGTH_LONG).show();
//
//            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//            photoPickerIntent.setType("image/*");
//            startActivityForResult(photoPickerIntent, 100);
//            return true;
//        });

        hotelCreateBtn.setOnClickListener(v -> {

            loadingDialog.show();

//            byte[] imageInByte = null;
//            try {
//                Bitmap bitmap = ((BitmapDrawable) hotelCreateImageIV.getDrawable()).getBitmap();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
//                imageInByte = baos.toByteArray();
//                try {
//                    baos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } catch (ClassCastException e) {
//
//            }

            if (isFieldsValid()) {
                loadingDialog.hide();
                return;
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
//            hcr.setMainImage(imageInByte);
            hcr.setDescription(description);

            UserGetResponse loggedUser = getLoggedUser(pref);
            hcr.setUserId(loggedUser.getUserId());

            dbHelper.insertHotel(hcr);
            loadingDialog.hide();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("page", "hotels");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

//        if (reqCode == 100 && resultCode == RESULT_OK) {
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//
//                hotelCreateImageIV.setImageBitmap(selectedImage);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//            }
//
//        } else {
//            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    private boolean isFieldsValid() {

        boolean hasErrors = false;

        String name = hotelCreateNameET.getText().toString();
        String city = hotelCreateCityET.getText().toString();
        String street = hotelCreateStreetET.getText().toString();
        Integer number = null;

        try {
            number = Integer.parseInt(hotelCreateNumberET.getText().toString());
        } catch (NumberFormatException ignore) {

        }

        Integer stars = null;
        try {
            stars = Integer.parseInt(hotelCreateStarsET.getText().toString());

            if (stars < 0 || stars > 8) {
                hotelCreateStarsET.setError("Stars of hotel must be between 0 and 8");
                hasErrors = true;
            }
        } catch (NumberFormatException ignore) {

        }

        String description = hotelCreateDescriptionET.getText().toString();

        if (name.isEmpty()) {
            hotelCreateNameET.setError("Name must not be empty");
            hasErrors = true;
        }

        if (city.isEmpty()) {
            hotelCreateCityET.setError("City must not be empty");
            hasErrors = true;
        }

        if (street.isEmpty()) {
            hotelCreateStreetET.setError("Street must not be empty");
            hasErrors = true;
        }

        if (number == null) {
            hotelCreateNumberET.setError("Number must not be empty");
            hasErrors = true;
        }

        if (stars == null) {
            hotelCreateStarsET.setError("Stars must not be empty");
            hasErrors = true;
        }

        if (description.isEmpty()) {
            hotelCreateDescriptionET.setError("Description must not be empty");
            hasErrors = true;
        }

        return hasErrors;
    }
}