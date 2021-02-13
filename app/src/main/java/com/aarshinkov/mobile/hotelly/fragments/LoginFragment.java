package com.aarshinkov.mobile.hotelly.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.activities.RootActivity;
import com.aarshinkov.mobile.hotelly.api.AuthApi;
import com.aarshinkov.mobile.hotelly.requests.LoginRequest;
import com.aarshinkov.mobile.hotelly.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.aarshinkov.mobile.hotelly.utils.Constants.API_URL;
import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_NAME;
import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_USER_EMAIL;
import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_USER_FIRST_NAME;
import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_USER_ID;
import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_USER_LAST_NAME;

public class LoginFragment extends Fragment {

    private EditText loginEmailET;
    private EditText loginPasswordET;
    private Button loginBtn;
    private TextView loginRegisterTV;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ProgressDialog loginDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        pref = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = pref.edit();

        loginEmailET = root.findViewById(R.id.loginEmailET);
        loginPasswordET = root.findViewById(R.id.loginPasswordET);
        loginBtn = root.findViewById(R.id.loginBtn);
        loginRegisterTV = root.findViewById(R.id.loginRegisterTV);

        loginRegisterTV.setOnClickListener(v -> {
            getActivity().findViewById(R.id.nav_view).findViewById(R.id.nav_registration).performClick();
        });

        loginDialog = new ProgressDialog(getContext());
        loginDialog.setMessage("Logging you in...");

        loginBtn.setOnClickListener(v -> {

            loginDialog.show();

            String email = loginEmailET.getText().toString();
            String password = loginPasswordET.getText().toString();

            boolean hasErrors = false;

            if (email.isEmpty()) {
                loginEmailET.setError("Email must not be empty");
                hasErrors = true;
            }

            if (password.isEmpty()) {
                loginPasswordET.setError("Password must not be empty");
                hasErrors = true;
            }

            if (hasErrors) {
                loginDialog.hide();
                return;
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthApi authApi = retrofit.create(AuthApi.class);

            authApi.login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

//                    if (!response.isSuccessful()) {
//                        Toast.makeText(getContext(), R.string.login_bad_credentials, Toast.LENGTH_LONG).show();
//                        loginDialog.hide();
//                        return;
//                    }

                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getUserId() == null || loginResponse.getUserId().isEmpty()) {
                        Toast.makeText(getContext(), R.string.login_bad_credentials, Toast.LENGTH_LONG).show();
                        loginDialog.hide();
                        return;
                    }

                    editor.putString(SHARED_PREF_USER_ID, loginResponse.getUserId());
                    editor.putString(SHARED_PREF_USER_EMAIL, loginResponse.getEmail());
                    editor.putString(SHARED_PREF_USER_FIRST_NAME, loginResponse.getFirstName());
                    editor.putString(SHARED_PREF_USER_LAST_NAME, loginResponse.getLastName());
                    editor.apply();

                    loginDialog.hide();

                    Intent intent = new Intent(getContext(), RootActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "SOME ERROR", Toast.LENGTH_LONG).show();
                    Log.e("ERROR", "onFailure: ", t);
                    loginDialog.hide();
                }
            });
        });

        return root;
    }
}