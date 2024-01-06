package com.radob.healthmelt.f100137;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

        Intent service = new Intent(HomeActivity.this, GreeterService.class);
        startService(service);

        CardView exit = findViewById(R.id.cardLogout);

        exit.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            stopService(service);
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        });

        CardView findDoctor = findViewById(R.id.cardFindDoctors);
        findDoctor.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, DoctorsActivity.class)));

        CardView orderDetails = findViewById(R.id.cardOrderDetails);
        orderDetails.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, OrderDetailsActivity.class)));

        CardView buyMedicine = findViewById(R.id.cardMedicine);
        buyMedicine.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, MedicineActivity.class)));

        CardView hospitals = findViewById(R.id.cardHospitals);
        hospitals.setOnClickListener(view-> startActivity(new Intent(HomeActivity.this, HospitalsActivity.class)));
    }
}