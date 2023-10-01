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

public class BuyMedicineBookActivity extends AppCompatActivity {

    EditText edName,edAddress,edContact,edPinCode;
    Button btnBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_book);

        edName = findViewById(R.id.editTextBMBFullName);
        edAddress = findViewById(R.id.editTextBMBAddress);
        edContact = findViewById(R.id.editTextBMBContact);
        edPinCode = findViewById(R.id.editTextBMBPinCode);
        btnBooking = findViewById(R.id.buttonBMBBook);


        Intent intent = getIntent();
        String[] price = Objects.requireNonNull(intent.getStringExtra("price")).split(java.util.regex.Pattern.quote(":"));
        String date = intent.getStringExtra("date");

        btnBooking.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username","");
            Database db = new Database(BuyMedicineBookActivity.this);
            db.addOrder(username, edName.getText().toString(), edAddress.getText().toString(), edContact.getText().toString(), Integer.parseInt(edPinCode.getText().toString()), Objects.requireNonNull(date), "", Float.parseFloat(price[1]), "medicine");
                        Toast.makeText(getApplicationContext(), "Your Booking is done successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BuyMedicineBookActivity.this, CartBuyMedicineActivity.class));
                        finish();
            db.removeCart(username, "medicine");
                        // Removal from Firebase successful
                        Toast.makeText(getApplicationContext(), "Your Booking is done successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BuyMedicineBookActivity.this, MainActivity.class));
                        finish();
        });
    }
}