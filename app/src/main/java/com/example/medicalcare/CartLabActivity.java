package com.example.medicalcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CartLabActivity extends AppCompatActivity {

    List<Map<String, String>> list;
    HealthArticleAdapter adapter;
    TextView tvTotal;
    RecyclerView recyclerView;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton;
    private Button timeButton;
    private   float totalAmount = 0;
    ArrayList<String> dbData;
    long priceItem;
    float price;
    Handler handler = new Handler();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_lab);
        dateButton = findViewById(R.id.buttonCartDate);
        timeButton = findViewById(R.id.buttonCartTime);
        Button btnCheckout = findViewById(R.id.buttonCheckout);
        Button btnBack = findViewById(R.id.buttonCartBack);
        tvTotal = findViewById(R.id.textViewCartTotalCost);
        recyclerView = findViewById(R.id.listViewCart);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser users = mAuth.getCurrentUser();
        String finalUser= Objects.requireNonNull(users).getEmail();
        String resultEmail = Objects.requireNonNull(finalUser).replace(".","");

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        Database db = new Database(CartLabActivity.this);
        dbData = new ArrayList<>();
        list = new ArrayList<>();

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart"+"lab").child(resultEmail);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> dbArrData = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                  //  for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        if (userSnapshot.hasChildren()) {
                            for (DataSnapshot itemSnapshot : userSnapshot.getChildren()) {
                                String product = itemSnapshot.child("product").getValue(String.class);
                                priceItem = itemSnapshot.child("price").getValue(Long.class);
                                price = priceItem;
                                totalAmount += price;
                                dbArrData.add(product + "$" + price);
                            }
                      //  }
                    }
                }

                handler.post(() -> {
                    dbData = dbArrData;

                    String[][] packages = new String[dbData.size()][];
                    for (int i = 0; i< packages.length; i++){
                        packages[i] = new String[5];
                    }

                    for (int i=0;i<dbData.size();i++){
                        String arrData = dbData.get(i); // dbData.get(i).toString();
                        String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                        packages[i][0] = strData[0];
                        packages[i][4] = "Cost : "+strData[1]+"/=";
                        totalAmount = totalAmount + Float.parseFloat(strData[1]);
                    }

                    tvTotal.setText(String.format("Total Cost : "+totalAmount));

                    for (String[] aPackage : packages) {
                        Map<String, String> item = new HashMap<>();
                        item.put("line1", aPackage[0]);
                        item.put("line2", aPackage[1]);
                        item.put("line3", aPackage[2]);
                        item.put("line4", aPackage[3]);
                        item.put("line5", aPackage[4]);
                        list.add(item);

                    }
                    adapter.notifyDataSetChanged();
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
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

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(CartLabActivity.this, LabTestActivity.class));
            finish();
        });

        btnCheckout.setOnClickListener(view -> {
            Intent it = new Intent(CartLabActivity.this, LabTestBookActivity.class);
            it.putExtra("price",tvTotal.getText());
            it.putExtra("date",dateButton.getText());
            it.putExtra("time",timeButton.getText());
            startActivity(it);
            finish();

        });

        initDatePicker();
        dateButton.setOnClickListener(view -> datePickerDialog.show());

        initTimePicker();
        timeButton.setOnClickListener(view -> timePickerDialog.show());
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            i1+=1;
            dateButton.setText(String.format(i2+"/"+i1+"/"+i));
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()+86400000);
    }

    private void initTimePicker(){
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, i, i1) -> timeButton.setText(String.format(i+":"+i1));

        Calendar cal = Calendar.getInstance();
        int hrs = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this,style,timeSetListener,hrs,mins,true);
    }
}