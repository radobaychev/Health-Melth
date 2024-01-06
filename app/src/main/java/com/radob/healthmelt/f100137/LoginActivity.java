package com.radob.healthmelt.f100137;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;


public class LoginActivity extends AppCompatActivity {
    EditText edEmail, edPassword;
    Button btnLogin;
    TextView tvUsername, tvRegister, tvPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.editTextLoginUsername);
        edPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tvUsername = findViewById(R.id.textViewLoginUsername);
        tvRegister = findViewById(R.id.textViewRegister);
        tvPassword = findViewById(R.id.textViewLoginPassword);

        btnLogin.setOnClickListener(view -> {
            String username = edEmail.getText().toString();
            String password = edPassword.getText().toString();
            Database db = new Database(getApplicationContext(), "HealthMelt", null, 1);
            if(username.length() == 0 || password.length() == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Попълнете всички полета!");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, i) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.show();
            } else{
                if(db.login(username, password)){
                    SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.apply();
                    db.close();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Невалидно потребителско име или парола!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, i) -> dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        tvRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));
    }
}