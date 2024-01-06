package com.radob.healthmelt.f100137;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookAppointmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "hospital";
    private static final String ARG_PARAM3 = "fee";

    // TODO: Rename and change types of parameters
    private String name;
    private String hospital;
    private Float fee;

    public BookAppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name Parameter 1.
     * @param hospital Parameter 2.
     * @param fee Parameter 3.
     * @return A new instance of fragment BookAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookAppointmentFragment newInstance(String name, String hospital, Float fee) {
        BookAppointmentFragment fragment = new BookAppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        args.putString(ARG_PARAM2, hospital);
        args.putFloat(ARG_PARAM3, fee);
        fragment.setArguments(args);
        return fragment;
    }

    private Button btnDate, btnTime;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_PARAM1);
            hospital = getArguments().getString(ARG_PARAM2);
            fee = getArguments().getFloat(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = requireActivity();
        LayoutInflater lf = activity.getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_book_appointment, container, false);
        EditText edName = view.findViewById(R.id.editTextAppFullName);
        EditText edHospital = view.findViewById(R.id.editTextAppHospital);
        EditText edFee = view.findViewById(R.id.editTextAppFee);

        Button btnBook = view.findViewById(R.id.buttonBook);
        btnDate = view.findViewById(R.id.buttonAppDate);
        btnTime = view.findViewById(R.id.buttonAppTime);

        edName.setText(name);
        edName.setInputType(InputType.TYPE_NULL);

        edHospital.setText(hospital);
        edHospital.setInputType(InputType.TYPE_NULL);

        edFee.setText(NumberFormat.getCurrencyInstance(new Locale("bg", "BG")).format(fee));
        edFee.setInputType(InputType.TYPE_NULL);

        initDatePicker();
        btnDate.setOnClickListener(v -> datePickerDialog.show());

        initTimePicker();
        btnTime.setOnClickListener(v -> timePickerDialog.show());

        btnBook.setOnClickListener(v->{
            SharedPreferences sharedPreferences = activity.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String date = btnDate.getText().toString();
            String time = btnTime.getText().toString();

            AppointmentWriterRunnable runnable = new AppointmentWriterRunnable(username, name, hospital, date, time, fee);
            new Thread(runnable).start();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }

    class AppointmentWriterRunnable implements Runnable{
        private final String username, name, hospital, date, time;
        private final float fee;
        public AppointmentWriterRunnable(String username, String name, String hospital, String date, String time, float fee){
            this.username = username;
            this.name = name;
            this.hospital = hospital;
            this.date = date;
            this.time = time;
            this.fee = fee;
        }

        @Override
        public void run() {
            Database db = new Database(requireActivity().getApplicationContext(), "HealthMelt", null, 1);
            if(db.AppointmentExists(name,hospital,date,time)){
                activity.runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Часът вече е записан! Моля изберете друг");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", (dialog, i) -> dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                });
            } else{
                db.addOrder(username,name,hospital,date,time,fee, "Час");
                activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), "Часът е запазен успешно.", Toast.LENGTH_LONG).show());
            }
            db.close();
        }
    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            ++i1;
            String time = i2+"/" + i1 + "/" + i;
            btnDate.setText(time);
        };
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);


        datePickerDialog = new DatePickerDialog(getContext(), 0, dateSetListener, y, m, d);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()+86400000);
    }

    private String prependZero(int n){
        String result = n + "";
        if(n < 10){
            result = "0" + result;
        }
        return result;
    }

    private void initTimePicker(){
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, i, i1) -> btnTime.setText(prependZero(i)+":"+prependZero(i1));

        Calendar cal = Calendar.getInstance();
        int h = cal.get(Calendar.HOUR);
        int m = cal.get(Calendar.MINUTE);


        timePickerDialog = new TimePickerDialog(getContext(), 0, timeSetListener, h, m, true);
    }
}