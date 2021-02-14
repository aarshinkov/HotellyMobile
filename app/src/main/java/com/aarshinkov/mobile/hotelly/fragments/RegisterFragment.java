package com.aarshinkov.mobile.hotelly.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.api.AuthApi;
import com.aarshinkov.mobile.hotelly.api.UsersApi;
import com.aarshinkov.mobile.hotelly.requests.users.UserCreateRequest;
import com.aarshinkov.mobile.hotelly.responses.users.UserCreatedResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.aarshinkov.mobile.hotelly.utils.Constants.API_URL;

public class RegisterFragment extends Fragment {

    private EditText registerEmailET;
    private EditText registerPasswordET;
    private EditText registerConfirmPasswordET;
    private EditText registerFirstNameET;
    private EditText registerLastNameET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register, container, false);

        registerEmailET = root.findViewById(R.id.registerEmailET);
        registerPasswordET = root.findViewById(R.id.registerPasswordET);
        registerConfirmPasswordET = root.findViewById(R.id.registerConfirmPasswordET);
        registerFirstNameET = root.findViewById(R.id.registerFirstNameET);
        registerLastNameET = root.findViewById(R.id.registerLastNameET);
        Button registerBtn = root.findViewById(R.id.registerBtn);

        ProgressDialog registeringDialog = new ProgressDialog(getContext());
        registeringDialog.setMessage("Creating your account...");

        registerBtn.setOnClickListener(v -> {
            registeringDialog.show();

            String email = registerEmailET.getText().toString();
            String password = registerPasswordET.getText().toString();
            String confirmPassword = registerConfirmPasswordET.getText().toString();
            String firstName = registerFirstNameET.getText().toString();
            String lastName = registerLastNameET.getText().toString();

            if (isFieldsValid()) {
                registeringDialog.hide();
                return;
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsersApi usersApi = retrofit.create(UsersApi.class);

            UserCreateRequest ucr = new UserCreateRequest();
            ucr.setEmail(email);
            ucr.setPassword(password);
            ucr.setFirstName(firstName);
            ucr.setLastName(lastName);

            usersApi.createUser(ucr).enqueue(new Callback<UserCreatedResponse>() {
                @Override
                public void onResponse(Call<UserCreatedResponse> call, Response<UserCreatedResponse> response) {
                    if (!response.isSuccessful()) {
                        registeringDialog.hide();
                        Toast.makeText(getContext(), "An error occurred while creating your account. Please try again!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    UserCreatedResponse ucr = response.body();

                    Toast.makeText(getContext(), ucr.getFullName() + ", your profile was successfully created!", Toast.LENGTH_LONG).show();

                    getActivity().findViewById(R.id.nav_view).findViewById(R.id.nav_login).performClick();
                }

                @Override
                public void onFailure(Call<UserCreatedResponse> call, Throwable t) {
                    registeringDialog.hide();
                    Toast.makeText(getContext(), "An error occurred while creating your account. Please try again!", Toast.LENGTH_LONG).show();
                }
            });
        });

        TextView registerHaveAccountTV = root.findViewById(R.id.registerHaveAccountTV);
        registerHaveAccountTV.setOnClickListener(v -> {
            getActivity().findViewById(R.id.nav_view).findViewById(R.id.nav_login).performClick();
        });

        return root;
    }

    private boolean isFieldsValid() {

        boolean hasErrors = false;

        String email = registerEmailET.getText().toString();
        String password = registerPasswordET.getText().toString();
        String confirmPassword = registerConfirmPasswordET.getText().toString();
        String firstName = registerFirstNameET.getText().toString();

        if (email.isEmpty()) {
            registerEmailET.setError("Email must not be empty");
            hasErrors = true;
        }

        if (password.isEmpty()) {
            registerPasswordET.setError("Password must not be empty");
            hasErrors = true;
        }

        if (confirmPassword.isEmpty()) {
            registerConfirmPasswordET.setError("The confirmation password must not be empty");
            hasErrors = true;
        }

        if (!password.equals(confirmPassword)) {
            String errorMessage = "The two passwords must match!";
            registerPasswordET.setError(errorMessage);
            registerConfirmPasswordET.setError(errorMessage);
            hasErrors = true;
        }

        if (firstName.isEmpty()) {
            registerFirstNameET.setError("First name must not be empty");
            hasErrors = true;
        }

        return hasErrors;
    }
}