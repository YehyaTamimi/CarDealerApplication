package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project.R;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.Intent;




import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    String DealerID;
    String uid;

    private boolean isBackButtonDisabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        DealerID = intent.getStringExtra("dealerid");
        uid = intent.getStringExtra("uid");
        DataBaseHelper helper = new DataBaseHelper(UserActivity.this, "project", null, 1);
        CarDealer dealer = helper.getDealerById(DealerID);

//        List<Cars> test =CarsJsonParser.carsOG;
//        Log.d("HELLO", String.valueOf(test.size()));
//        for (int i = 0; i < test.size(); i++) {
//            Cars element = test.get(i);
//
//            Log.d("test"+i,element.getType() );
//        }

        setTitle(dealer.getDealerName());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null ){
            HomeFragment homeFragment = new HomeFragment();

            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putString("dealerid", DealerID);// Replace "key" and "value" with your actual data

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {

            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putString("dealerid", DealerID);
            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        } else if (itemId == R.id.nav_carmenu) {

            CarMenuFragement carFragment = new CarMenuFragement();
            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putString("dealerid", DealerID);// Replace "key" and "value" with your actual data
            carFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, carFragment).commit();

        } else if (itemId == R.id.nav_reservations) {

            ReservationsFragment reservationsFragment = new ReservationsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putString("dealerid", DealerID);// Replace "key" and "value" with your actual data
            reservationsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, reservationsFragment).commit();

        } else if (itemId == R.id.nav_favorites) {

            FavoritesFragment favoritesFragment = new FavoritesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putString("dealerid", DealerID);// Replace "key" and "value" with your actual data
            favoritesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, favoritesFragment).commit();

        } else if (itemId == R.id.nav_specialoffers) {

            SpecialOffersFragment specialOffersFragment = new SpecialOffersFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putString("dealerid", DealerID);// Replace "key" and "value" with your actual data
            specialOffersFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, specialOffersFragment).commit();

        } else if (itemId == R.id.nav_profile) {

            ProfileFragment profileFragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            profileFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

        } else if (itemId == R.id.nav_callus) {

            CallUsFragment callUsFragment = new CallUsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("dealerid", DealerID);
            callUsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, callUsFragment).commit();

        } else if (itemId == R.id.nav_logout) {
            Intent intent = new Intent(UserActivity.this, MainActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            if (!isBackButtonDisabled) {
                super.onBackPressed();
            } else {
                // Do nothing or show a message indicating that the back button is disabled
            }
        }
    }


}