package com.example.medicalcare;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class HealthArticlesActivity extends AppCompatActivity {

 /*
    private final String[][] health_details =
            {
                    {"Walking daily","","","","Click More Details"},
                    {"Home Care of COVID-19","","","","Click More Details"},
                    {"Stop Smoking","","","","Click More Details"},
                    {"Menstrual Cramps","","","","Click More Details"},
                    {"Healthy Gut","","","","Click More Details"},
            }; */

    private final int[] images = {
            R.drawable.health1,
            R.drawable.health2,
            R.drawable.health3,
            R.drawable.health4,
            R.drawable.health5
    };

    RecyclerView recyclerView;
    HealthArticleAdapter adapter;
    List<Map<String,String>> list;
    Button btnBack;
    private String[][] health_details;

    // Handler for updating UI from a different thread
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles);

        btnBack = findViewById(R.id.buttonHABack);
        recyclerView = findViewById(R.id.listViewHA);
        list = new ArrayList<>();
        health_details = new String[5][5];

        DatabaseReference packagesRef = FirebaseDatabase.getInstance().getReference("healthDetailsPackage");
/*
        // store data to firebase from the created array
        for (int i = 0; i < health_details.length; i++) {
            String health_details_name = health_details[i][0];
            String health_details_choice = health_details[i][4];

            String packageId = "health_details" + (i + 1);

            Map<String, String> packageData = new HashMap<>();
            packageData.put("healthDetailsName", health_details_name);
            packageData.put("healthDetailsChoice", health_details_choice);

            packagesRef.child(packageId).setValue(packageData);
        } */


        // Fetch data from Firebase using a ValueEventListener
        packagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Lists to hold retrieved data temporarily
                List<String[]> retrievedPackages = new ArrayList<>();

                // Loop through Firebase data snapshots
                for (DataSnapshot packageSnapshot : dataSnapshot.getChildren()) {
                    String healthDetailsName = packageSnapshot.child("healthDetailsName").getValue(String.class);
                    String healthDetailsChoice = packageSnapshot.child("healthDetailsChoice").getValue(String.class);

                    // Store retrieved data in lists
                    String[] packageArray = {healthDetailsName, "", "", "", healthDetailsChoice};
                    retrievedPackages.add(packageArray);
                }

                // Convert the retrieved data to the format of your original arrays
                String[][] retrievedPackagesArray = retrievedPackages.toArray(new String[0][0]);

                handler.post(() -> {
                    health_details = retrievedPackagesArray;
                    updateUI();  // Update the RecyclerView's data
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(HealthArticlesActivity.this, MainActivity.class));
            finish();
        });


        // Initialize and set up the RecyclerView
        adapter = new HealthArticleAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        // Create a new DividerItemDecoration with a black divider line
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider); // Replace with your divider drawable
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        if (dividerDrawable != null) {
            dividerItemDecoration.setDrawable(dividerDrawable);
        }
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setOnItemClickListener((position) -> {
            Intent it = new Intent(HealthArticlesActivity.this, HealthArticlesDetailsActivity.class);
            it.putExtra("text1", health_details[position][0]);
            it.putExtra("text2", images[position]);
            startActivity(it);
            finish();
        });
    }

    private void updateUI() {
        list.clear();
        for (String[] healthDetail : health_details) {
            Map<String,String> item = new HashMap<>();
            item.put("line1", healthDetail[0]);
            item.put("line2", healthDetail[1]);
            item.put("line3", healthDetail[2]);
            item.put("line4", healthDetail[3]);
            item.put("line5", healthDetail[4]);
            list.add(item);
        }
        adapter.notifyDataSetChanged(); // Notify the adapter of the data change
    }

}