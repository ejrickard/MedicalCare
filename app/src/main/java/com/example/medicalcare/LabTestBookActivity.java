package com.example.medicalcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class LabTestBookActivity extends AppCompatActivity {

    EditText edName,edAddress,edContact,edPinCode;
    Button btnBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_book);

        edName = findViewById(R.id.editTextLTBFullName);
        edAddress = findViewById(R.id.editTextLTBAddress);
        edContact = findViewById(R.id.editTextLTBContact);
        edPinCode = findViewById(R.id.editTextLTBPinCode);
        btnBooking = findViewById(R.id.buttonLTBBook);

        Intent intent = getIntent();
        String[] price = Objects.requireNonNull(intent.getStringExtra("price")).split(java.util.regex.Pattern.quote(":"));
        String date = intent.getStringExtra("date");
        String time= intent.getStringExtra("time");

        btnBooking.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username","");
            Database db = new Database(LabTestBookActivity.this);
            db.addOrder(username, edName.getText().toString(), edAddress.getText().toString(), edContact.getText().toString(), Integer.parseInt(edPinCode.getText().toString()), date, time, Float.parseFloat(price[1]), "lab");
                        Toast.makeText(getApplicationContext(), "Your Booking is done successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LabTestBookActivity.this, CartLabActivity.class));
                        finish();

            db.removeCart(username, "lab");
                        // Removal from Firebase successful
                        Toast.makeText(getApplicationContext(), "Your Booking is done successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LabTestBookActivity.this, MainActivity.class));
                        finish();

        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LabTestBookActivity.this, CartLabActivity.class));
        finish();
    }
}