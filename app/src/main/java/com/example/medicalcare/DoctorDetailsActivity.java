package com.example.medicalcare;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

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

public class DoctorDetailsActivity extends AppCompatActivity {
    /*
    private final String[][] doctor_Details1 = {
            {"Doctor Name : Michael Justin", "Hospital Address : Kairuki", "Exp : 5yrs","Mobile No:0746253456","600000"},
            {"Doctor Name : Warren Julius", "Hospital Address : Muhimbili", "Exp : 8yrs","Mobile No:0645553452","900000"},
            {"Doctor Name : Julius Brown", "Hospital Address : AgaKhan", "Exp : 3yrs","Mobile No:0746253222","650000"},
            {"Doctor Name : Joseph Christopher", "Hospital Address : Moi", "Exp : 5yrs","Mobile No:0746211456","760000"},
            {"Doctor Name : Flora Peter", "Hospital Address : Mireni", "Exp : 4yrs","Mobile No:0646253451","560000"}
    };

    private final String[][] doctor_Details2 = {
            {"Doctor Name : Emily Smith", "Hospital Address : Mercy Hospital", "Exp : 6yrs","Mobile No:0746256789","720000"},
            {"Doctor Name : David Miller", "Hospital Address : St. Jude's Medical", "Exp : 7yrs","Mobile No:0645551234","820000"},
            {"Doctor Name : Olivia Davis", "Hospital Address : Sunshine Clinic", "Exp : 2yrs","Mobile No:0746259876","480000"},
            {"Doctor Name : Ethan Wilson", "Hospital Address : Good Health Center", "Exp : 4yrs","Mobile No:0746215432","560000"},
            {"Doctor Name : Sophia Anderson", "Hospital Address : Bright Care Hospital", "Exp : 5yrs","Mobile No:0646259876","620000"}
    };

    private final String[][] doctor_Details3 = {
            {"Doctor Name : Liam Johnson", "Hospital Address : Grace Medical Center", "Exp : 7yrs","Mobile No:0746251111","780000"},
            {"Doctor Name : Ava Martinez", "Hospital Address : Life Care Clinic", "Exp : 6yrs","Mobile No:0645552222","700000"},
            {"Doctor Name : Noah Wilson", "Hospital Address : Hope Hospital", "Exp : 3yrs","Mobile No:0746253333","610000"},
            {"Doctor Name : Isabella Taylor", "Hospital Address : Healing Hands Hospital", "Exp : 5yrs","Mobile No:0746214444","690000"},
            {"Doctor Name : Oliver Brown", "Hospital Address : Comfort Health Center", "Exp : 4yrs","Mobile No:0646255555","620000"}
    };

    private final String[][] doctor_Details4 = {
            {"Doctor Name : Mia Johnson", "Hospital Address : Gentle Care Clinic", "Exp : 8yrs","Mobile No:0746257777","800000"},
            {"Doctor Name : Elijah Davis", "Hospital Address : Wellness Hospital", "Exp : 5yrs","Mobile No:0645556666","670000"},
            {"Doctor Name : Sophia Thomas", "Hospital Address : Harmony Medical Center", "Exp : 4yrs","Mobile No:0746258888","620000"},
            {"Doctor Name : Lucas Anderson", "Hospital Address : Caring Touch Hospital", "Exp : 6yrs","Mobile No:0746219999","740000"},
            {"Doctor Name : Amelia White", "Hospital Address : Serene Health Clinic", "Exp : 3yrs","Mobile No:0646250000","590000"}
    };

    private final String[][] doctor_Details5 = {
            {"Doctor Name : Jackson Smith", "Hospital Address : Graceful Healing Hospital", "Exp : 5yrs","Mobile No:0746253333","670000"},
            {"Doctor Name : Olivia Davis", "Hospital Address : Tranquil Care Clinic", "Exp : 7yrs","Mobile No:0645554444","780000"},
            {"Doctor Name : Liam Johnson", "Hospital Address : Peaceful Wellness Center", "Exp : 4yrs","Mobile No:0746255555","610000"},
            {"Doctor Name : Emma Wilson", "Hospital Address : Comforting Hands Hospital", "Exp : 6yrs","Mobile No:0746216666","720000"},
            {"Doctor Name : Noah Thomas", "Hospital Address : Serenity Medical Center", "Exp : 3yrs","Mobile No:0646257777","590000"}
    }; */


    TextView tv;
    Button btn;
    String[][] doctor_Details;
    RecyclerView recyclerView;
    HealthArticleAdapter adapter;
    List<Map<String, String>> list;
    DatabaseReference doctorDetailsRef;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        //hooks
        tv = findViewById(R.id.textViewDDTitle);
        btn = findViewById(R.id.buttonBack);
        recyclerView = findViewById(R.id.listViewDD);
        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tv.setText(title);
        list = new ArrayList<>();
        doctor_Details = new String[5][5];


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


        /*
        // storing data in firebase
        for (int i = 0; i < doctor_Details1.length; i++) {
            String doctorName = doctor_Details1[i][0];
            String hospitalAddress = doctor_Details1[i][1];
            String experience = doctor_Details1[i][2];
            String mobileNumber = doctor_Details1[i][3];
            String salary = doctor_Details1[i][4];

            String doctorId = "doctor" + (i + 1);

            Map<String, String> doctorData = new HashMap<>();
            doctorData.put("doctorName", doctorName);
            doctorData.put("hospitalAddress", hospitalAddress);
            doctorData.put("experience", experience);
            doctorData.put("mobileNumber", mobileNumber);
            doctorData.put("salary", salary);

            doctorDetailsRef.child(doctorId).setValue(doctorData);
        }

        doctorDetailsRef = FirebaseDatabase.getInstance().getReference("Dietician");
        for (int i = 0; i < doctor_Details2.length; i++) {
            String doctorName = doctor_Details2[i][0];
            String hospitalAddress = doctor_Details2[i][1];
            String experience = doctor_Details2[i][2];
            String mobileNumber = doctor_Details2[i][3];
            String salary = doctor_Details2[i][4];

            String doctorId = "doctor" + (i + 1);

            Map<String, String> doctorData = new HashMap<>();
            doctorData.put("doctorName", doctorName);
            doctorData.put("hospitalAddress", hospitalAddress);
            doctorData.put("experience", experience);
            doctorData.put("mobileNumber", mobileNumber);
            doctorData.put("salary", salary);

            doctorDetailsRef.child(doctorId).setValue(doctorData);
        }

        doctorDetailsRef = FirebaseDatabase.getInstance().getReference("Dentist");
        for (int i = 0; i < doctor_Details3.length; i++) {
            String doctorName = doctor_Details3[i][0];
            String hospitalAddress = doctor_Details3[i][1];
            String experience = doctor_Details3[i][2];
            String mobileNumber = doctor_Details3[i][3];
            String salary = doctor_Details3[i][4];

            String doctorId = "doctor" + (i + 1);

            Map<String, String> doctorData = new HashMap<>();
            doctorData.put("doctorName", doctorName);
            doctorData.put("hospitalAddress", hospitalAddress);
            doctorData.put("experience", experience);
            doctorData.put("mobileNumber", mobileNumber);
            doctorData.put("salary", salary);

            doctorDetailsRef.child(doctorId).setValue(doctorData);
        }

        doctorDetailsRef = FirebaseDatabase.getInstance().getReference("Surgeon");
        for (int i = 0; i < doctor_Details4.length; i++) {
            String doctorName = doctor_Details4[i][0];
            String hospitalAddress = doctor_Details4[i][1];
            String experience = doctor_Details4[i][2];
            String mobileNumber = doctor_Details4[i][3];
            String salary = doctor_Details4[i][4];

            String doctorId = "doctor" + (i + 1);

            Map<String, String> doctorData = new HashMap<>();
            doctorData.put("doctorName", doctorName);
            doctorData.put("hospitalAddress", hospitalAddress);
            doctorData.put("experience", experience);
            doctorData.put("mobileNumber", mobileNumber);
            doctorData.put("salary", salary);

            doctorDetailsRef.child(doctorId).setValue(doctorData);
        }

        doctorDetailsRef = FirebaseDatabase.getInstance().getReference("Cardiologist");
        for (int i = 0; i < doctor_Details5.length; i++) {
            String doctorName = doctor_Details5[i][0];
            String hospitalAddress = doctor_Details5[i][1];
            String experience = doctor_Details5[i][2];
            String mobileNumber = doctor_Details5[i][3];
            String salary = doctor_Details5[i][4];

            String doctorId = "doctor" + (i + 1);

            Map<String, String> doctorData = new HashMap<>();
            doctorData.put("doctorName", doctorName);
            doctorData.put("hospitalAddress", hospitalAddress);
            doctorData.put("experience", experience);
            doctorData.put("mobileNumber", mobileNumber);
            doctorData.put("salary", salary);

            doctorDetailsRef.child(doctorId).setValue(doctorData);
        } */


        if (title.compareTo("FamilyPhysician") == 0) {
            retrieveFrmFirebase(title,title);
        }
        if (title.compareTo("Dietician") == 0) {
            retrieveFrmFirebase(title,title);
        }
        if (title.compareTo("Dentist") == 0) {
            retrieveFrmFirebase(title,title);
        }
        if (title.compareTo("Surgeon") == 0) {
            retrieveFrmFirebase(title,title);
        }
        if (title.compareTo("Cardiologist") == 0) {
            retrieveFrmFirebase(title,title);

        }

        btn.setOnClickListener(view -> {
            startActivity(new Intent(DoctorDetailsActivity.this, FindDoctorActivity.class));
            finish();
        });

    }


    private void retrieveFrmFirebase (String pathString,String title) {
        // return data back to the array string variable for usage.
        doctorDetailsRef = FirebaseDatabase.getInstance().getReference(pathString);
        doctorDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String[]> retrievedDoctorDetails = new ArrayList<>();
                String[][] doctorDetails;
                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                    String doctorName = doctorSnapshot.child("doctorName").getValue(String.class);
                    String hospitalAddress = doctorSnapshot.child("hospitalAddress").getValue(String.class);
                    String experience = doctorSnapshot.child("experience").getValue(String.class);
                    String mobileNumber = doctorSnapshot.child("mobileNumber").getValue(String.class);
                    String salary = doctorSnapshot.child("salary").getValue(String.class);

                    String[] doctorArray = {doctorName, hospitalAddress, experience, mobileNumber, salary};
                    retrievedDoctorDetails.add(doctorArray);
                }
                // Convert the list to a 2D array
                doctorDetails = new String[retrievedDoctorDetails.size()][];
                for (int i = 0; i < retrievedDoctorDetails.size(); i++) {
                    doctorDetails[i] = retrievedDoctorDetails.get(i);
                }

                handler.post(() -> {
                    // Convert the retrieved data to the format of your original array

                    updateUI(doctorDetails,title);  // Update the RecyclerView's data
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });


    }

    private void updateUI (String[][] doctor_Details,String title){
        list.clear();
        for (String[] doctorDetail : doctor_Details) {
            Map<String, String> item = new HashMap<>();
            item.put("line1", doctorDetail[0]);
            item.put("line2", doctorDetail[1]);
            item.put("line3", doctorDetail[2]);
            item.put("line4", doctorDetail[3]);
            item.put("line5", "Fees: " + doctorDetail[4] + "/=");
            list.add(item);
        }
        adapter.notifyDataSetChanged(); // Notify the adapter of the data change


        adapter.setOnItemClickListener((i) -> {
            Intent it1 = new Intent(DoctorDetailsActivity.this, BookAppointmentActivity.class);
            it1.putExtra("text1", title);
            it1.putExtra("text2", doctor_Details[i][0]);
            it1.putExtra("text3", doctor_Details[i][1]);
            it1.putExtra("text4", doctor_Details[i][3]);
            it1.putExtra("text5", doctor_Details[i][4]);
            startActivity(it1);
            finish();
        });

    }
}