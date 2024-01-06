package com.radob.healthmelt.f100137;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    HashMap<String, String> item;
    ArrayList<HashMap<String,String>> list;
    SimpleAdapter sa;
    ListView lst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        lst = findViewById(R.id.listViewOrderDetails);

        Database db = new Database(getApplicationContext(), "HealthMelt", null, 1);
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        ArrayList<String> dbData = db.getOrderData(username);
        db.close();

        String[][] orderDetails = new String[dbData.size()][];
        for(int i = 0; i < orderDetails.length; ++i){
            orderDetails[i] = new String[5];
            String arrData = dbData.get(i);
            String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
            orderDetails[i][0] = strData[0];
            orderDetails[i][1] = strData[1];
            if(strData[5].compareTo("medicine") == 0){
                orderDetails[i][3] = strData[2];
            } else{
                orderDetails[i][3] = strData[2] + " " + strData[3];
            }
            orderDetails[i][2] = NumberFormat.getCurrencyInstance(new Locale("bg", "BG")).format(java.lang.Float.parseFloat(strData[4]));
            orderDetails[i][4] = strData[5];
        }

        list = new ArrayList<>();
        for (String[] orderDetail : orderDetails) {
            item = new HashMap<>();
            item.put("line1", orderDetail[0]);
            item.put("line2", orderDetail[1]);
            item.put("line3", orderDetail[2]);
            item.put("line4", orderDetail[3]);
            item.put("line5", orderDetail[4]);
            list.add(item);
        }

        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[] {"line1", "line2", "line3", "line4", "line5"},
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        lst.setAdapter(sa);
    }
}