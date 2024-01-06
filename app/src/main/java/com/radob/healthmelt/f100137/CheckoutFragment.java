package com.radob.healthmelt.f100137;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



public class CheckoutFragment extends Fragment {

    private Activity activity;
    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = requireActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_checkout, container, false);
        activity = requireActivity();
        EditText edName = view.findViewById(R.id.editTextCheckoutName);
        EditText edAddress = view.findViewById(R.id.editTextCheckoutAddress);
        Button btnCheckout = view.findViewById(R.id.buttonCheckout);

        btnCheckout.setOnClickListener(view1 -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String name = edName.getText().toString();
            String address = edAddress.getText().toString();
            if(name.length() == 0 || address.length() == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Попълнете всички полета!");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", (dialog, i) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.show();
                return;
            }

            OrderWriterRunnable runnable = new OrderWriterRunnable(username, name, address);
            new Thread(runnable).start();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private class OrderWriterRunnable implements Runnable{
        private final String username;
        private final String name;
        private final String address;
        public OrderWriterRunnable(String username, String name, String address){
            this.username = username;
            this.name = name;
            this.address = address;
        }

        @Override
        public void run() {
            Database db = new Database(requireActivity().getApplicationContext(), "HealthMelt", null, 1);
            Float totalPrice = db.getCartSum(username);
            if(totalPrice == 0f){
                activity.runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Кошницата е празна!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", (dialog, i)->dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                });
                return;
            }
            db.addOrder(username,
                    name,
                    address,
                    java.time.LocalDate.now().toString(),
                    java.time.LocalTime.now().toString(),
                    totalPrice,
                    "Лекарства");
            db.removeCart(username);
            db.close();
            activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), "Поръчката е приета!", Toast.LENGTH_LONG).show());
        }
    }
}