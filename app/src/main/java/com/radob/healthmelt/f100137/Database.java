package com.radob.healthmelt.f100137;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users(" +
                "email TEXT, " +
                "username TEXT, " +
                "password TEXT, " +
                "PRIMARY KEY(username, email));");

        sqLiteDatabase.execSQL("CREATE TABLE cart(" +
                "username TEXT, " +
                "product TEXT, " +
                "amount TEXT," +
                "price FLOAT);");

        sqLiteDatabase.execSQL("CREATE TABLE orders(" +
                "username TEXT, " +
                "fullname TEXT, " +
                "address TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "price FLOAT, " +
                "otype TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE doctors(" +
                "name TEXT, " +
                "specialty TEXT, " +
                "hospital TEXT, " +
                "phone_number TEXT," +
                "fee FLOAT);");

        sqLiteDatabase.execSQL("CREATE TABLE medicine(" +
                "name TEXT," +
                "amount TEXT," +
                "price FLOAT," +
                "PRIMARY KEY(name, amount));");
        generateDefaultDatabaseTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void register(String username, String email, String password){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        db.insert("users", null, cv);

        db.close();
    }

    public boolean login(String username, String password){
        String[] queryArgs = new String[2];
        queryArgs[0] = username; queryArgs[1] = password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from users where username=? and password=?", queryArgs);
        boolean result = c.moveToFirst();
        c.close();
        db.close();

        return result;
    }

    public void addToCart(String username, String product, String amount, float price){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("product", product);
        cv.put("amount", amount);
        cv.put("price", price);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("cart", null, cv);
        db.close();
    }

    public void removeCart(String username){
        String[] args = new String[1];
        args[0] = username;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "username=?", args);
        db.close();
    }

    public ArrayList<String> getCartData(String username){
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] args = new String[1];
        args[0] = username;
        Cursor c = db.rawQuery("SELECT * FROM cart WHERE username = ?", args);
        if(c.moveToFirst()) {
            do {
                String product = c.getString(1);
                String price = c.getString(2);
                String amount = c.getString(3);
                data.add(product + "$" + price + "$" + amount);
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        return data;
    }

    public Float getCartSum(String username){
        float sum = 0f;
        SQLiteDatabase db = getReadableDatabase();
        String[] args = new String[1];
        args[0] = username;
        Cursor c = db.rawQuery("SELECT SUM(price) FROM cart WHERE username=?", args);
        if(c.moveToFirst()){
            sum = c.getFloat(0);
        }
        c.close();
        return sum;
    }

    public void addOrder(String username, String fullname, String address, String date, String time, Float price, String otype){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("price", price);
        cv.put("otype", otype);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("orders", null, cv);
        db.close();
    }

    private void addMedicine(String name, String amount, float price, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("amount", amount);
        cv.put("price", price);
        db.insert("medicine", null, cv);
    }
    private void addDoctor(String name, String specialty, String hospital, String phone, int fee, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("specialty", specialty);
        cv.put("hospital", hospital);
        cv.put("phone_number", phone);
        cv.put("fee", fee);
        db.insert("doctors", null, cv);
    }
    public ArrayList<String> getOrderData(String username){
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] args = new String[1];
        args[0] = username;
        Cursor c = db.rawQuery("SELECT * FROM orders WHERE username = ?", args);
        if(c.moveToFirst()){
            do{
                data.add(c.getString(1) + "$" + c.getString(2) + "$" + c.getString(3) + "$" + c.getString(4) + "$" + c.getString(5) + "$" + c.getString(6));
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return data;
    }

    public ArrayList<String> getMedicineData(){
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("Select * FROM medicine", null);

        if(c.moveToFirst()){
            do{
                data.add(c.getString(0) + "$" +
                        c.getString(1) + "$" +
                        c.getString(2));
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return data;
    }
    public ArrayList<String> getDoctorData(@NonNull String specialty){
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] args = new String[1];
        args[0] = specialty;
        Cursor c;

        if(specialty.equals("Всички")){
            c = db.rawQuery("SELECT * FROM doctors", null);
        } else{
            c = db.rawQuery("SELECT * FROM doctors WHERE specialty = ?", args);
        }

        if(c.moveToFirst()){
            do{
                data.add(c.getString(0) + "$" +
                         c.getString(1) + "$" +
                         c.getString(2) + "$" +
                         c.getString(3) + "$" +
                         c.getString(4));
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return data;
    }
    public boolean AppointmentExists(String fullname, String address, String date, String time){
        String[] args = new String[4];
        args[0] = fullname;
        args[1] = address;
        args[2] = date;
        args[3] = time;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM orders WHERE fullname = ? and address = ?  and date = ? and time =?", args);
        boolean result = c.moveToFirst();
        c.close();
        db.close();

        return result;
    }

    private void generateDefaultDatabaseTables(SQLiteDatabase db){
        //Doctors
        addDoctor("Петър Петров", "Кардиолог", "Софиямед", "123456", 100, db);
        addDoctor("Димитър Димитров", "Зъболекар", "Александровска", "234567", 20, db);
        addDoctor("Име Фамилиев", "Кардиолог", "Лозенец", "345678", 45, db);
        addDoctor("Доктор Докторов", "Ветеринар", "Зоо", "456789", 670, db);
        addDoctor("Лекар Лекаров", "Психолог", "Токуда", "456789", 600, db);
        addDoctor("Истинско Име", "Диетолог", "Токуда", "356789", 3500, db);
        addDoctor("Зъбо Лекар", "Зъболекар", "Болница", "256789", 6, db);
        addDoctor("Александър Александров", "Неврохирург", "Там", "156789", 6700, db);

        //Medicine
        addMedicine("Алергозан", "10mg", 2.40f, db);
        addMedicine("Стрепсилс", "10mg", 5, db);
        addMedicine("Студал", "300ml", 12, db);
        addMedicine("Стрепсилс", "20mg", 9.50f, db);
    }
}
