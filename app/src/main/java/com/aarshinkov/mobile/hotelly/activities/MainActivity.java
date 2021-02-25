package com.aarshinkov.mobile.hotelly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aarshinkov.mobile.hotelly.R;
import com.aarshinkov.mobile.hotelly.fragments.HotelsFragment;
import com.aarshinkov.mobile.hotelly.responses.users.UserGetResponse;
import com.google.android.material.navigation.NavigationView;

import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_NAME;
import static com.aarshinkov.mobile.hotelly.utils.Utils.getLoggedUser;
import static com.aarshinkov.mobile.hotelly.utils.Utils.isLoggedIn;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (isLoggedIn(pref)) {

            UserGetResponse loggedUser = getLoggedUser(pref);

            navigationView.getHeaderView(0).setVisibility(View.VISIBLE);

            ImageView headerImage = navigationView.getHeaderView(0).findViewById(R.id.header_image);
            TextView headerFullName = navigationView.getHeaderView(0).findViewById(R.id.header_name);
            TextView headerEmail = navigationView.getHeaderView(0).findViewById(R.id.header_email);

            headerFullName.setText(loggedUser.getFullName());
            headerEmail.setText(loggedUser.getEmail());

            navigationView.getMenu().findItem(R.id.nav_login_group).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);

            navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(item -> {
//                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
                logout();
                return true;
            });

            headerImage.setOnClickListener(v -> {
//                profile();
            });

            headerFullName.setOnClickListener(v -> {
//                profile();
            });

            headerEmail.setOnClickListener(v -> {
//                profile();
            });
        } else {
            navigationView.getHeaderView(0).setVisibility(View.GONE);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_hotels, R.id.nav_login, R.id.nav_registration)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        Toast.makeText(getApplicationContext(), "HERE", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        String page = intent.getStringExtra("page");

        if (page != null) {
            if (page.equals("hotels")) {
                navigationView.getMenu().findItem(R.id.nav_hotels).setChecked(true);
                getSupportActionBar().setTitle(R.string.menu_hotels);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HotelsFragment.class, null)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        pref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = pref.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getApplicationContext(), getString(R.string.logout_success), Toast.LENGTH_LONG).show();

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}