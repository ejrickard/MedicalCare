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

public class BuyMedicineActivity extends AppCompatActivity {

        // Declare necessary variables
        String[][] packages;
        String[] package_details;
        List<Map<String,String>> list;
        HealthArticleAdapter adapter;
        RecyclerView recyclerView;
        Button btnBack,btnGoToCart;
        DatabaseReference packagesRef;

       // Handler for updating UI from a different thread
        Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine);

        // Initialize UI elements or creating hooks
        recyclerView = findViewById(R.id.listViewBM);
        btnBack = findViewById(R.id.buttonBMBack);
        btnGoToCart = findViewById(R.id.buttonBMGoToCart);
        packages = new String[9][5];
        package_details = new String[9];
        list = new ArrayList<>();

        // Initialize and set up the RecyclerView
        adapter = new HealthArticleAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize Firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        packagesRef = database.getReference("medicinePackages");

/*
 // Loop through each medicine package in the 'packages' array and package_details into firebase
 // for storage
        for (int i = 0; i < packages.length; i++) {

            // Extract the name, details, and price for the current package
            String name = packages[i][0];
            String details = package_details[i];
            String price = packages[i][4];

            // Create a unique package ID based on the index (e.g., "package1", "package2", ...)
            String packageId = "package" + (i + 1);

            // Create a HashMap to store the package data
            Map<String, String> packageData = new HashMap<>();
            packageData.put("name", name);
            packageData.put("details", details);
            packageData.put("price", price);

            // Set the package data under the specific package ID in the database
            packagesRef.child(packageId).setValue(packageData);
        }   */


        // Fetch data from Firebase using a ValueEventListener
        packagesRef = FirebaseDatabase.getInstance().getReference("medicinePackages");
        packagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Lists to hold retrieved data temporarily
                List<String[]> retrievedPackages = new ArrayList<>();
                List<String> retrievedPackageDetails = new ArrayList<>();

                // Loop through Firebase data snapshots
                for (DataSnapshot packageSnapshot : dataSnapshot.getChildren()) {
                    String name = packageSnapshot.child("name").getValue(String.class);
                    String details =  packageSnapshot.child("details").getValue(String.class);
                    String price = packageSnapshot.child("price").getValue(String.class);

                    // Store retrieved data in lists
                    String[] packageArray = {name, "", "", "", price};
                    retrievedPackages.add(packageArray);
                    retrievedPackageDetails.add(details);
                }

                // Convert the retrieved data to the format of your original arrays
                String[][] retrievedPackagesArray = retrievedPackages.toArray(new String[0][0]);
                String[] retrievedPackageDetailsArray = retrievedPackageDetails.toArray(new String[0]);

                handler.post(() -> {
                    packages = retrievedPackagesArray;
                    package_details = retrievedPackageDetailsArray;
                    updateUI();  // Update the RecyclerView's data
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        btnGoToCart.setOnClickListener(view -> {
            startActivity(new Intent(BuyMedicineActivity.this, CartBuyMedicineActivity.class));
            finish();
        });

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(BuyMedicineActivity.this, MainActivity.class));
            finish();
        });


        // Create a new DividerItemDecoration with a black divider line for RecyclerView items
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider); // Replace with your divider drawable
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        if (dividerDrawable != null) {
            dividerItemDecoration.setDrawable(dividerDrawable);
        }
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setOnItemClickListener((i) -> {
            Intent it = new Intent(BuyMedicineActivity.this,BuyMedicineDetailsActivity.class);
            it.putExtra("text1",packages[i][0]);
            it.putExtra("text2",package_details[i]);
            it.putExtra("text3",packages[i][4]);
            startActivity(it);
            finish();
        });

    }

    private boolean isArrayFUll(String[] array) {
        for (String value : array) {
            if (value == null) {
                return false;
            }
        }
        return true;
    }

    private boolean is2DArrayFUll(String[][] array) {
        for (String[] subArray : array) {
            for (String value : subArray) {
                if (value == null) {
                    return false;
                }
            }
        }
        return true;
    }

    // Update the RecyclerView's data with fetched data
    private void updateUI() {
        list.clear();
        for (String[] aPackage : packages) {
            Map<String, String> item = new HashMap<>();
            item.put("line1", aPackage[0]);
            item.put("line2", aPackage[1]);
            item.put("line3", aPackage[2]);
            item.put("line4", aPackage[3]);
            item.put("line5", "Total Cost:" + aPackage[4] + "/=");
            list.add(item);
        }
        adapter.notifyDataSetChanged(); // Notify the adapter of the data change
    }

}