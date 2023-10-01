package com.example.medicalcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long backPressedTime = 0;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hooks
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        menuIcon = findViewById(R.id.menu_icon);
        Database db = new Database(MainActivity.this);
       
        navigationDrawer();

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        Toast.makeText(MainActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();

        CardView exit = findViewById(R.id.cardEXit);
        exit.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            if (db.logout() == 1) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(MainActivity.this,"LOGOUT SUCCESSFUL", Toast.LENGTH_SHORT).show();
            }
        });

        CardView findDoctor = findViewById(R.id.cardFindDoctor);
        findDoctor.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, FindDoctorActivity.class));
            finish();
        });

        CardView labTest = findViewById(R.id.cardLabTest);
        labTest.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,LabTestActivity.class));
            finish();
        });

        CardView orderDetails = findViewById(R.id.cardOrderDetails);
        orderDetails.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,OrderDetailsActivity.class));
            finish();
        });

        CardView buyMedicine = findViewById(R.id.cardBuyMedicine);
        buyMedicine.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,BuyMedicineActivity.class));
            finish();
        });

        CardView health = findViewById(R.id.cardHealthDoctor);
        health.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,HealthArticlesActivity.class));
            finish();
        });
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(view -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    // back press button for user control code is here.
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId(); // Get the ID of the selected item

        if (itemId == R.id.nav_home) {
            Intent intent = new Intent(MainActivity.this, FindDoctorActivity.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.nav_bmi) {
            Intent intent = new Intent(MainActivity.this, BmiCalculatorActivity.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.nav_current_age) {
            Intent intent = new Intent(MainActivity.this, AgeCalculatorActivity.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}