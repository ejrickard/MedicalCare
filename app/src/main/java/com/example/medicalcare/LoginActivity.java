package com.example.medicalcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private long backPressedTime = 0;
    TextInputEditText edEmail, edPassword;
    TextInputLayout layEmail,layPassword;
    Button button;
    TextView txtView;
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }

        edEmail = findViewById(R.id.editTextLoginUsername);
        layEmail = findViewById(R.id.layoutLoginUsername);
        edPassword = findViewById(R.id.editTextLoginPassword);
        layPassword = findViewById(R.id.layoutLoginPassword);
        button = findViewById(R.id.buttonLogin);
        txtView = findViewById(R.id.textViewNewUser);
        txtView.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        button.setOnClickListener(view -> {
            String email = Objects.requireNonNull(edEmail.getText()).toString();
            String password = Objects.requireNonNull(edPassword.getText()).toString();

            if (TextUtils.isEmpty(email)){
                layEmail.setError("Username is required");
                if (TextUtils.isEmpty(password)){
                    layPassword.setError("Password is Required");
                }
            }
            else if (email.length() == 0 || password.length() == 0) {
                Toast.makeText(LoginActivity.this, "Please Enter All the Details", Toast.LENGTH_SHORT).show();
            }
            else{
                if(isValid(password)){

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username",email);
                                        editor.apply();
                                        Toast.makeText(LoginActivity.this, "Login Successful.",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish(); // Close the LoginActivity
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }else {
                    Toast.makeText(LoginActivity.this, "Password must contain at least 6 characters, having letters,digits and special symbols" , Toast.LENGTH_SHORT).show();
                }
            }

        });




    }

    public static boolean isValid(String passwordHere) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordHere.length() >= 8) {
            for (int p = 0; p < passwordHere.length(); p++) {
                if (Character.isLetter(passwordHere.charAt(p))) {
                    f1 = 1;
                }
            }
            for (int r = 0; r < passwordHere.length(); r++) {
                if (Character.isDigit(passwordHere.charAt(r))) {
                    f2 = 1;
                }
            }
            for (int s = 0; s < passwordHere.length(); s++) {
                char c = passwordHere.charAt(s);
                if (c > 33 && c <= 46 || c == 64) {
                    f3 = 1;
                }
            }
            return f1 == 1 && f2 == 1 && f3 == 1;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}