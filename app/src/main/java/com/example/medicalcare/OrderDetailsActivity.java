package com.example.medicalcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderDetailsActivity extends AppCompatActivity {

    String[][] order_details;
    List<Map<String, String>> list;
    RecyclerView recyclerView;
    HealthArticleAdapter adapter;
    Button btn;
    DatabaseReference orderRef;
    FirebaseAuth mAuth;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        btn = findViewById(R.id.buttonODBack);
        recyclerView= findViewById(R.id.listViewOD);
        list = new ArrayList<>();

        // Initialize and set up the RecyclerView
        adapter = new HealthArticleAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btn.setOnClickListener(view -> {
            startActivity(new Intent(OrderDetailsActivity.this, MainActivity.class));
            finish();
        });


        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        Database db = new Database(OrderDetailsActivity.this);
        ArrayList<String> dbData  = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser users = mAuth.getCurrentUser();
        String finalUser = Objects.requireNonNull(users).getEmail();
        String resultEmail = Objects.requireNonNull(finalUser).replace(".", "");
        orderRef = FirebaseDatabase.getInstance().getReference("orderPlace").child(resultEmail);

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    if (orderSnapshot.hasChildren()){
                        String username = orderSnapshot.child("username").getValue(String.class);
                        String fullName = orderSnapshot.child("fullName").getValue(String.class);
                        String address = orderSnapshot.child("address").getValue(String.class);
                        String contact = orderSnapshot.child("contact").getValue(String.class);
                        String date = orderSnapshot.child("date").getValue(String.class);
                        String time = orderSnapshot.child("time").getValue(String.class);
                        Long amount = orderSnapshot.child("amount").getValue(Long.class); // Retrieve as Long
                        String otype = orderSnapshot.child("otype").getValue(String.class);

                        String orderData = username + "$" + fullName + "$" + address + "$" + contact +
                                "$" + date + "$" + time + "$" + amount + "$" + otype;
                        dbData.add(orderData);

                    }
                }

                // Now, you have the order data in the 'arr' ArrayList
                handler.post(() -> {


                    order_details = new String[dbData.size()][];
                    for (int i=0;i<order_details.length;i++){
                        order_details[i] = new String[5];
                        String arrData = dbData.get(i);
                        String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                        order_details[i][0] = strData[0];
                        order_details[i][1] = strData[1];
                        if (strData[7].compareTo("medicine")==0){
                            order_details[i][3] = "Del:"+strData[4];
                        }else {
                            order_details[i][3] = "Del:"+strData[4]+" "+strData[5];
                        }
                        order_details[i][2] = "Tsh."+strData[6];
                        order_details[i][4] = strData[7];
                    }
                    updateUI();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Create a new DividerItemDecoration with a black divider line
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider); // Replace with your divider drawable
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        if (dividerDrawable != null) {
            dividerItemDecoration.setDrawable(dividerDrawable);
        }
        recyclerView.addItemDecoration(dividerItemDecoration);


    }

    // Update the RecyclerView's data with fetched data
    private void updateUI() {
        list.clear();
        for (String[] orderDetail : order_details) {
            Map<String, String> item = new HashMap<>();
            item.put("line1", orderDetail[0]);
            item.put("line2", orderDetail[1]);
            item.put("line3", orderDetail[2]);
            item.put("line4", orderDetail[3]);
            item.put("line5", orderDetail[4]);
            list.add(item);
        }
        adapter.notifyDataSetChanged(); // Notify the adapter of the data change
    }

}