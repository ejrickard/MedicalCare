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

public class LabTestActivity extends AppCompatActivity {

    /*
    private String[][] packages =
            {
                    {"Package 1 : Full Body Checkup","","","","76000"},
                    {"Package 2 : Blood Glucose Fasting","","","","80000"},
                    {"Package 3 : COVID_19 Antibody - IgG","","","","90000"},
                    {"Package 4 : Thyroid Check","","","","60000"},
                    {"Package 5 : Immunity Check","","","","98000"}
            };

    private String[] package_details = {
            "Blood Glucose Fasting\n" +
                    " Complete Hemogram\n" +
                    "Hb1c\n" +
                    " Iron Studies\n" +
                    "Kidney Function Test\n" +
                    "LDH Lactate Dehydrogenase, Serum\n"+
                    "Lipid Profile\n"+
                    "Liver Function Test",
            "Blood Glucose Fasting",
            "COVID_19 Antibody - IgG",
            "Thyroid Profile-Total (T3, T4 & TS Ultra-sensitive)",
            "Complete Hemogram\n"+
                    "CRP (C Reactive Protein) Quantitative, Serum\n"+
                    " Iron Studies\n"+
                    "Kidney Function Test\n"+
                    "Vitamin D Total-25 Hydroxyl\n"+
                    "Liver Function Test\n"+
                    "Lipid Profile"
    };  */
    List<Map<String, String>> list;
    Button btnGoToCart,btnBack;
    RecyclerView recyclerView;
    HealthArticleAdapter adapter;
    private String[][] packages;
    private String[] package_details;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test);

        btnGoToCart = findViewById(R.id.buttonLTGoToCart);
        btnBack = findViewById(R.id.buttonLTBack);
        recyclerView = findViewById(R.id.listViewLT);
        list = new ArrayList<>();
        packages = new String[9][5];
        package_details = new String[9];

        DatabaseReference packagesRef = FirebaseDatabase.getInstance().getReference("labPackages");
        /*
        // store data to firebase from the created array
        for (int i = 0; i < packages.length; i++) {
            String packageName = packages[i][0];
            String details = package_details[i];
            String price = packages[i][4];

            String packageId = "package" + (i + 1);

            Map<String, String> packageData = new HashMap<>();
            packageData.put("packageName", packageName);
            packageData.put("details", details);
            packageData.put("price", price);

            packagesRef.child(packageId).setValue(packageData);
        } */

        // Fetch data from Firebase using a ValueEventListener
        // restore data to array variable from the firebase
        packagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Lists to hold retrieved data temporarily
                List<String[]> retrievedPackages = new ArrayList<>();
                List<String> retrievedPackageDetails = new ArrayList<>();

                // Loop through Firebase data snapshots
                for (DataSnapshot packageSnapshot : dataSnapshot.getChildren()) {
                    String packageName = packageSnapshot.child("packageName").getValue(String.class);
                    String details = packageSnapshot.child("details").getValue(String.class);
                    String price = packageSnapshot.child("price").getValue(String.class);

                    // Store retrieved data in lists
                    String[] packageArray = {packageName, "", "", "", price};
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


        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(LabTestActivity.this, MainActivity.class));
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

        adapter.setOnItemClickListener(position -> {
            Intent it = new Intent(LabTestActivity.this, LabTestDetailsActivity.class);
            it.putExtra("text1", packages[position][0]);
            it.putExtra("text2", package_details[position]);
            it.putExtra("text3", packages[position][4]);
            startActivity(it);
            finish();
        });

        btnGoToCart.setOnClickListener(view -> {
            startActivity(new Intent(LabTestActivity.this,CartLabActivity.class));
            finish();
        });
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