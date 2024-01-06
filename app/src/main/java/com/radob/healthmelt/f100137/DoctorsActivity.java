package com.radob.healthmelt.f100137;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

public class DoctorsActivity extends AppCompatActivity {

    Button btnDentists, btnCardiologists, btnSurgeons, btnAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        btnDentists = findViewById(R.id.buttonDentists);
        btnCardiologists = findViewById(R.id.buttonCardiologists);
        btnSurgeons = findViewById(R.id.buttonSurgeons);
        btnAll = findViewById(R.id.buttonAll);

        Fragment fragmentAll = DoctorDetailsFragment.newInstance("Всички");
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fragmentContainerViewDoctors, fragmentAll);
        t.setReorderingAllowed(true);
        t.addToBackStack("AllDoctorsFragment");
        t.commit();

        btnDentists.setOnClickListener(view -> {
            Fragment fragmentDentists = DoctorDetailsFragment.newInstance("Зъболекар");
            FragmentTransaction t12 = getSupportFragmentManager().beginTransaction();
            t12.replace(R.id.fragmentContainerViewDoctors, fragmentDentists);
            t12.setReorderingAllowed(true);
            t12.addToBackStack("DentistsFragment");
            t12.commit();
        });

        btnCardiologists.setOnClickListener(view -> {
            Fragment fragmentCardiologists = DoctorDetailsFragment.newInstance("Кардиолог");
            FragmentTransaction t13 = getSupportFragmentManager().beginTransaction();
            t13.replace(R.id.fragmentContainerViewDoctors, fragmentCardiologists);
            t13.setReorderingAllowed(true);
            t13.addToBackStack("CardiologistsFragment");
            t13.commit();
        });

        btnSurgeons.setOnClickListener(view -> {
            Fragment fragmentSurgeons = DoctorDetailsFragment.newInstance("Хирург");
            FragmentTransaction t14 = getSupportFragmentManager().beginTransaction();
            t14.replace(R.id.fragmentContainerViewDoctors, fragmentSurgeons);
            t14.setReorderingAllowed(true);
            t14.addToBackStack("SurgeonsFragment");
            t14.commit();
        });

        btnAll.setOnClickListener(view -> {
            FragmentTransaction t1 = getSupportFragmentManager().beginTransaction();
            t1.replace(R.id.fragmentContainerViewDoctors, fragmentAll);
            t1.setReorderingAllowed(true);
            t1.addToBackStack("AllDoctorsFragment");
            t1.commit();
        });
    }
}