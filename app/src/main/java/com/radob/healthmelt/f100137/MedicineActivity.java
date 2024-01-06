package com.radob.healthmelt.f100137;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

public class MedicineActivity extends AppCompatActivity {

    Button btnCart, btnMedicine, btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        btnCart = findViewById(R.id.buttonToCart);
        btnMedicine = findViewById(R.id.buttonMedicines);
        btnCheckout = findViewById(R.id.buttonCheckout);

        Fragment fragment = MedicineListFragment.newInstance();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fragmentContainerViewMedicine, fragment);
        t.setReorderingAllowed(true);
        t.addToBackStack("MedicineList");
        t.commit();

        btnCart.setOnClickListener(view -> {
            Fragment cartFragment = getSupportFragmentManager().findFragmentByTag("MedicineCart");
            if(!(cartFragment instanceof MedicineCartFragment)){
                cartFragment = MedicineCartFragment.newInstance();
            }
            FragmentTransaction t1 = getSupportFragmentManager().beginTransaction();
            t1.replace(R.id.fragmentContainerViewMedicine, cartFragment);
            t1.setReorderingAllowed(true);
            t1.addToBackStack("MedicineCart");
            t1.commit();
        });

        btnMedicine.setOnClickListener(view -> {
            Fragment listFragment = getSupportFragmentManager().findFragmentByTag("MedicineList");
            if(!(listFragment instanceof MedicineListFragment)){
                listFragment = new MedicineListFragment();
            }
            FragmentTransaction t12 = getSupportFragmentManager().beginTransaction();
            t12.replace(R.id.fragmentContainerViewMedicine, listFragment);
            t12.setReorderingAllowed(true);
            t12.addToBackStack("MedicineList");
            t12.commit();
        });

        btnCheckout.setOnClickListener(view -> {
            Fragment checkoutFragment = getSupportFragmentManager().findFragmentByTag("Checkout");
            if(!(checkoutFragment instanceof CheckoutFragment)){
                checkoutFragment = new CheckoutFragment();
            }
            FragmentTransaction t13 = getSupportFragmentManager().beginTransaction();
            t13.replace(R.id.fragmentContainerViewMedicine, checkoutFragment);
            t13.setReorderingAllowed(true);
            t13.addToBackStack("Checkout");
            t13.commit();
        });
    }
}