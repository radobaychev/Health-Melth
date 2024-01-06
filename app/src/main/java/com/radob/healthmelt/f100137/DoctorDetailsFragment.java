package com.radob.healthmelt.f100137;

import static java.lang.Float.parseFloat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "specialty";

    // TODO: Rename and change types of parameters
    private String specialty;

    public DoctorDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param specialty Parameter 1.
     * @return A new instance of fragment DoctorDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorDetailsFragment newInstance(String specialty) {
        DoctorDetailsFragment fragment = new DoctorDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, specialty);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            specialty = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = requireActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_doctor_details, container, false);

        ListView lv = view.findViewById(R.id.listViewDoctorDetails);
        Database db = new Database(requireActivity().getApplicationContext(), "HealthMelt", null, 1);
        ArrayList<String> dbData;
        if(specialty != null){
             dbData = db.getDoctorData(specialty);
        } else{
            dbData = db.getDoctorData("Всички");
        }
        db.close();
        String[][] doctorDetails = new String[dbData.size()][];

        for(int i = 0; i < doctorDetails.length; ++i){
            String arrData = dbData.get(i);
            String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
            doctorDetails[i] = strData;
        }

        HashMap<String,String> item;
        SimpleAdapter sa;
        ArrayList<HashMap<String,String>> list = new ArrayList<>();

        for (String[] doctorDetail : doctorDetails) {
            item = new HashMap<>();
            item.put("name", doctorDetail[0]);
            item.put("specialty", doctorDetail[1]);
            item.put("hospital", doctorDetail[2]);
            item.put("phone_num", doctorDetail[3]);
            item.put("fee", NumberFormat.getCurrencyInstance(new Locale("bg", "BG")).format(parseFloat(doctorDetail[4])));
            list.add(item);
        }

        sa = new SimpleAdapter(getContext(),
                list,
                R.layout.multi_lines,
                new String[]{"name", "specialty", "hospital", "phone_num", "fee"},
                new int[]{R.id.line_a, R.id.line_b,R.id.line_c, R.id.line_d,R.id.line_e});

        lv.setAdapter(sa);

        lv.setOnItemClickListener((adapterView, view1, i, l) -> {
            Fragment fragment = BookAppointmentFragment.newInstance(doctorDetails[i][0],doctorDetails[i][2], Float.parseFloat(doctorDetails[i][4]));
            FragmentTransaction t = requireActivity().getSupportFragmentManager().beginTransaction();
            t.replace(((ViewGroup) requireView().getParent()).getId(), fragment, "AppointmentFragment");
            t.setReorderingAllowed(true);
            t.addToBackStack("AppointmentFragment");
            t.commit();
        });
        return view;
    }
}