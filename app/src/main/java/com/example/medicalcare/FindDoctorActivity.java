package com.example.medicalcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FindDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        CardView exit = findViewById(R.id.cardFDBack);
        exit.setOnClickListener(view -> startActivity(new Intent(FindDoctorActivity.this, MainActivity.class)));

        CardView familyPhysician = findViewById(R.id.cardFDFamilyPhysician);
        familyPhysician.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorDetailsActivity.class);
            it.putExtra("title","FamilyPhysician");
            startActivity(it);
            finish();
        });

        CardView dietician = findViewById(R.id.cardFDDietician);
        dietician.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorDetailsActivity.class);
            it.putExtra("title","Dietician");
            startActivity(it);
            finish();
        });

        CardView dentist = findViewById(R.id.cardFDDentist);
        dentist.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorDetailsActivity.class);
            it.putExtra("title","Dentist");
            startActivity(it);
            finish();
        });

        CardView surgeon = findViewById(R.id.cardFDSurgeon);
        surgeon.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorDetailsActivity.class);
            it.putExtra("title","Surgeon");
            startActivity(it);
            finish();
        });

        CardView cardiologists = findViewById(R.id.cardFDCardiologists);
        cardiologists.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorDetailsActivity.class);
            it.putExtra("title","Cardiologist");
            startActivity(it);
            finish();
        });
    }
}