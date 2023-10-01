package com.example.medicalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class AgeCalculatorActivity extends AppCompatActivity {

    TextView description;
    private EditText bYear;
    private EditText bMonth;
    private EditText bDay;
    private EditText aYear;
    private EditText aMonth;
    private EditText aDay;
    private EditText year;
    private EditText month;
    private TextView result;
    Button calc;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    final Calendar cal= Calendar.getInstance();
    String birth_date;
    int days,months,years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calculator);

        description = findViewById(R.id.tvResult);
        result = description;
        result.setEnabled(false);
        bYear=  findViewById(R.id.etBirthdayYear);
        bYear.setEnabled(false);
        bMonth= findViewById(R.id.etBirthdayMonth);
        bMonth.setEnabled(false);
        bDay= findViewById(R.id.etBirthdayDay);
        bDay.setEnabled(false);
        aYear=  findViewById(R.id.etAgeYear);
        aYear.setEnabled(false);
        aMonth=  findViewById(R.id.etAgeMonth);
        aMonth.setEnabled(false);
        aDay = findViewById(R.id.etAgeDay);
        aDay.setEnabled(false);
        year =  findViewById(R.id.etYear);
        year.setEnabled(false);
        month =  findViewById(R.id.etMonth);
        month.setEnabled(false);
        calc=  findViewById(R.id.buttonCalculate);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser users = mAuth.getCurrentUser();
        String finalUser = Objects.requireNonNull(users).getEmail();
        String resultEmail = Objects.requireNonNull(finalUser).replace(".", "");
        userRef = FirebaseDatabase.getInstance().getReference("users").child(resultEmail).child("UserDetails");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // get data from firebase and store in string variables
                    birth_date = dataSnapshot.child("birthdate").getValue(String.class);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date;

                    try {
                        date = dateFormat.parse(birth_date);

                        if (date != null) {
                            Calendar calendars = Calendar.getInstance();
                            calendars.setTime(date);

                            days = calendars.get(Calendar.DAY_OF_MONTH);
                            months = calendars.get(Calendar.MONTH) + 1; // Adding 1 because months are 0-based
                            years = calendars.get(Calendar.YEAR);
                        }

                        bDay.setText(String.valueOf(days));
                        bMonth.setText(String.valueOf(months));
                        bYear.setText(String.valueOf(years));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String yr = String.valueOf(calendar.get(Calendar.YEAR));
        int thisMn = (calendar.get(Calendar.MONTH)) + 1;
        String mn = String.valueOf(thisMn);
        String da = String.valueOf(calendar.get(Calendar.DATE));

        aYear.setText(yr);
        aMonth.setText(mn);
        aDay.setText(da);

        calc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                try {
                    long birthYear = Long.parseLong(bYear.getText().toString());
                    long birthMonth = Long.parseLong(bMonth.getText().toString());
                    long birthDay = Long.parseLong(bDay.getText().toString());
                    long ageYear = Long.parseLong(aYear.getText().toString());
                    long ageMonth = Long.parseLong(aMonth.getText().toString());
                    long ageDay = Long.parseLong(aDay.getText().toString());
                    if (ageMonth>birthMonth&&ageDay>birthDay){
                        long ageYearDiff = ageYear - birthYear;
                        long ageMonthDiff = ageMonth - birthMonth;
                        long ageDayDiff = ageDay - birthDay;
                        if (birthDay < 32 && ageDay < 32 && ageMonth < 13)
                        {
                            year.setText(String.format(ageYearDiff+" Year"));
                            month.setText(String.format(ageMonthDiff+" Month"));
                            result.setText(String.format(ageDayDiff+" Days"));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter DD and MM value!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (ageMonth > birthMonth){
                        long ageYearDiff = (ageYear - birthYear);


                        long ageMonthDiff = (ageMonth - birthMonth) -1;
                        long ageDayDiff;
                        if (ageMonth == 1 || ageMonth == 3 || ageMonth == 5 || ageMonth == 7 || ageMonth == 8 || ageMonth == 10 || ageMonth == 12 ) {
                            ageDayDiff = (ageDay - birthDay) + 31;
                        }
                        else if (ageMonth == 2 ) {
                            ageDayDiff = (ageDay - birthDay) + 28;
                        }
                        else {
                            ageDayDiff = (ageDay - birthDay) + 30;
                        }
                        if (birthDay < 32 && ageMonth < 13)
                        {
                            year.setText(String.format(ageYearDiff+" Year"));
                            month.setText(String.format(ageMonthDiff+" Month"));
                            result.setText(String.format(ageDayDiff+" Days"));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter DD and MM value!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (ageDay>birthDay){
                        long ageYearDiff = (ageYear - birthYear) - 1;
                        long ageMonthDiff = (ageMonth - birthMonth) + 12;
                        long ageDayDiff = ageDay - birthDay;

                        if (birthDay<32 &&ageDay<32 &&ageMonth<13 &&birthMonth<13)
                        {
                            year.setText(String.format(ageYearDiff+" Year"));
                            month.setText(String.format(ageMonthDiff+" Month"));
                            result.setText(String.format(ageDayDiff+" Days"));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter DD and MM value!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (ageMonth<= birthMonth){
                        long ageYearDiff = (ageYear - birthYear) - 1;
                        long ageMonthDiff = (ageMonth - birthMonth) + 11;
                        long ageDayDiff;
                        if (ageMonth == 3 || ageMonth == 1 || ageMonth == 5 || ageMonth == 7 || ageMonth == 8 || ageMonth == 10 || ageMonth == 12 ) {
                            ageDayDiff = (ageDay - birthDay) + 31;
                        }
                        else if (ageMonth == 2 ) {
                            ageDayDiff = (ageDay - birthDay) + 28;
                        }
                        else {
                            ageDayDiff = (ageDay - birthDay) + 30;
                        }
                        if (birthDay < 32 && ageMonth < 13 && birthMonth < 13)
                        {


                            year.setText(String.format(ageYearDiff+" Year"));
                            month.setText(String.format(ageMonthDiff+" Month"));
                            result.setText(String.format(ageDayDiff+" Days"));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enter DD and MM value!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Month Problem",Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"Please enter all data!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AgeCalculatorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}