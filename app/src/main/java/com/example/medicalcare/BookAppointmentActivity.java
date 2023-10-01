package com.example.medicalcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class BookAppointmentActivity extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    TextView tv;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton;
    private Button timeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        // Initialize UI components
        tv = findViewById(R.id.textViewAppTitle);
        ed1 = findViewById(R.id.editTextAppFullName);
        ed2 = findViewById(R.id.editTextAppAddress);
        ed3 = findViewById(R.id.editTextAppContact);
        ed4 = findViewById(R.id.editTextAppFees);
        dateButton = findViewById(R.id.buttonAppDate);
        timeButton = findViewById(R.id.buttonAppTime);
        Button btnBook = findViewById(R.id.buttonBookAppointment);
        Button btnBack = findViewById(R.id.buttonAppBack);

        // Make EditTexts non-editable
        ed1.setKeyListener(null);
        ed2.setKeyListener(null);
        ed3.setKeyListener(null);
        ed4.setKeyListener(null);

        // Get data from previous activity
        Intent it = getIntent();
        String title = it.getStringExtra("text1");
        String fullName = it.getStringExtra("text2");
        String address = it.getStringExtra("text3");
        String contact = it.getStringExtra("text4");
        String fees = it.getStringExtra("text5");

        // Display data in UI elements
        tv.setText(title);
        ed1.setText(fullName);
        ed2.setText(address);
        ed3.setText(contact);
        ed4.setText(String.format("Cons Fees:"+fees+"/="));

        initDatePicker();
        dateButton.setOnClickListener(view -> datePickerDialog.show());

        initTimePicker();
        timeButton.setOnClickListener(view -> timePickerDialog.show());

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(BookAppointmentActivity.this, FindDoctorActivity.class));
            finish(); // Finish the current activity (BookAppointmentActivity)
        });

        btnBook.setOnClickListener(view -> {

            // Retrieve user's username from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username","");
            Database db = new Database(getApplicationContext());

            db.checkAppointmentExists(username, title + " => " + fullName, address, contact, dateButton.getText().toString(), timeButton.getText().toString(), new AppointmentExistsCallback() {
                @Override
                public void onAppointmentCheckResult(int result) {
                    if (result == 1) {
                        Toast.makeText(getApplicationContext(), "Appointment already done", Toast.LENGTH_SHORT).show();
                    } else {
                        db.addOrder(username, title + " => " + fullName, address, contact, 0, dateButton.getText().toString(), timeButton.getText().toString(), Float.parseFloat(Objects.requireNonNull(fees)), "appointment");
                                    Toast.makeText(getApplicationContext(), "Your appointment is done successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(BookAppointmentActivity.this, MainActivity.class));
                                    finish();
                    }
                }
            });


        });
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