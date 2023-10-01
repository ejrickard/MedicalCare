package com.example.medicalcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LabTestDetailsActivity extends AppCompatActivity {
    TextView tvPackageName,tvTotalCost;
    EditText edDetails;
    Button btnAddToCart,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_details);

        tvPackageName = findViewById(R.id.textViewLDPackageName);
        tvTotalCost = findViewById(R.id.textViewLDTotalCost);
        edDetails = findViewById(R.id.editTextLDTextMultiLine);
        btnBack = findViewById(R.id.buttonLDGoBack);
        btnAddToCart = findViewById(R.id.buttonLDAddToCart);

        edDetails.setKeyListener(null);

        Intent intent = getIntent();
        tvPackageName.setText(intent.getStringExtra("text1"));
        edDetails.setText(intent.getStringExtra("text2"));
        tvTotalCost.setText(String.format("Total Cost : "+intent.getStringExtra("text3")+"/="));

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(LabTestDetailsActivity.this, LabTestActivity.class));
            finish();
        });

        btnAddToCart.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username","");
            String product = tvPackageName.getText().toString();
            float price = Float.parseFloat(Objects.requireNonNull(intent.getStringExtra("text3")));

            Database db = new Database(LabTestDetailsActivity.this);
            // Assuming you have a FirebaseHelper instance called firebaseHelper
            db.checkCart(username, product, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Cart item exists for the given username and product
                        Toast.makeText(getApplicationContext(), "Product Already Added", Toast.LENGTH_SHORT).show();
                    } else {
                        // Cart item doesn't exist, add it to Firebase
                        db.addCart(username, product, price, "lab");
                        Toast.makeText(getApplicationContext(), "Record Inserted to Cart", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LabTestDetailsActivity.this, LabTestActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });

        });


    }
}