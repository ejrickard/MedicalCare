package com.example.medicalcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class VerifyPhoneNoActivity extends AppCompatActivity {

    Button verify_btn;
    EditText phoneNumberEntered;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        verify_btn = findViewById(R.id.buttonVerify);
        phoneNumberEntered = findViewById(R.id.editTextOTP);
        progressBar = findViewById(R.id.progressBarOTP);
        progressBar.setVisibility(View.GONE);

        verify_btn.setOnClickListener(view -> {
            String code= phoneNumberEntered.getText().toString();

            if (code.isEmpty() || code.length() <6){
                phoneNumberEntered.setError("Wrong Verification code...");
                phoneNumberEntered.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
        });
    }





}