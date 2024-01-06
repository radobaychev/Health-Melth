package com.radob.healthmelt.f100137;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
 * Use the {@link MedicineCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineCartFragment extends Fragment {

    public MedicineCartFragment() {
        // Required empty public constructor
    }

    public static MedicineCartFragment newInstance() {
        return new MedicineCartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = requireActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_medicine_cart, container, false);

        ListView lv = view.findViewById(R.id.listViewMedicineCart);

        Database db = new Database(requireActivity().getApplicationContext(),"HealthMelt", null, 1);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        ArrayList<String> dbData = db.getCartData(username);
        String[][] medicineDetails = new String[dbData.size()][];
        for(int i = 0; i < medicineDetails.length; i++){
            String arrData = dbData.get(i);
            String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
            medicineDetails[i] = strData;
        }

        HashMap<String,String> item;
        SimpleAdapter sa;
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        for (String[] medicineDetail : medicineDetails) {
            item = new HashMap<>();
            item.put("name", medicineDetail[0]);
            item.put("amount", medicineDetail[1]);
            String price = NumberFormat.getCurrencyInstance(new Locale("bg", "BG")).format(Float.parseFloat(medicineDetail[2]));
            item.put("price", price);
            list.add(item);
        }
        Float totalPrice = db.getCartSum(username);
        db.close();
        item = new HashMap<>();
        item.put("name", "Сума:" + NumberFormat.getCurrencyInstance(new Locale("bg", "BG")).format(totalPrice));
        item.put("amount", "");
        item.put("price", "");
        list.add(item);

        sa = new SimpleAdapter(getContext(),
                list,
                R.layout.multi_lines,
                new String[]{"name", "amount", "price", "", ""},
                new int[]{R.id.line_a,R.id.line_b, R.id.line_c, R.id.line_d,R.id.line_e});

        lv.setAdapter(sa);

        return view;
    }
}