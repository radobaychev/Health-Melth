package com.radob.healthmelt.f100137;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegistrationActivity extends AppCompatActivity {
    EditText edEmail, edPassword, edConfirm, edUsername;
    Button btnConfirm;
    TextView tvExistingUser, tvEmail, tvPassword, tvConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edEmail = findViewById(R.id.editTextRegEmail);
        edUsername = findViewById(R.id.editTextRegUsername);
        edPassword = findViewById(R.id.editTextRegPassword);
        edConfirm = findViewById(R.id.editTextConfirmPassword);
        btnConfirm = findViewById(R.id.buttonRegister);
        tvExistingUser = findViewById(R.id.textViewExistingUser);
        tvEmail = findViewById(R.id.textViewRegEmail);
        tvPassword = findViewById(R.id.textViewRegPassword);
        tvConfirmPassword = findViewById(R.id.textViewConfirmPassword);

        btnConfirm.setOnClickListener(view -> {
            String email = edEmail.getText().toString();
            String username = edUsername.getText().toString();
            String password = edPassword.getText().toString();
            String confirmPassword = edConfirm.getText().toString();
            Database db = new Database(getApplicationContext(), "HealthMelt", null, 1);

            if (email.length() == 0 || password.length() == 0 || confirmPassword.length() == 0 || username.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Попълнете всички полета!");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, i) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.show();
            } else {
                if (password.compareTo(confirmPassword) == 0) {
                    if(isValidPassword(password)){
                        db.register(username, email, password);
                        Toast.makeText(getApplicationContext(), "Регистрацията е успешна", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Невалидна парола");
                        builder.setMessage("Паролата трябва да е дълга поне 8 символа и трябва да съдържа цифра, буква и специален символ!");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, i) -> dialog.cancel());

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Паролите не съвпадат");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, i) -> dialog.cancel());

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            db.close();
        });

        tvExistingUser.setOnClickListener(view -> startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)));
    }

    public static boolean isValidPassword(String password) {
        boolean containsNumber = false, containsLetter = false, containsSpecial = false;
        int len = password.length();
        if(len < 8){ return false; }

        for(int i = 0; i < len; ++i){
            char c = password.charAt(i);
            containsNumber = containsNumber || Character.isDigit(c);
            containsLetter = containsLetter || Character.isLetter(c);
            containsSpecial = containsSpecial || (c >= 33 && c <= 46) || c == 64;
        }

        return containsNumber && containsLetter && containsSpecial;
    }
}
