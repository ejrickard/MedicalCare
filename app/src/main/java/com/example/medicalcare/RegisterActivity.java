package com.example.medicalcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText edEmail,edPassword,edFullName,edUserName,edContact,edBirthdate;
    TextInputLayout layEmail,layPassword,layFullName,layUsername,layContact,layBirthdate;
    Button button;
    TextView txtView;
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edPassword = findViewById(R.id.editTextRegisterPassword);
        layPassword = findViewById(R.id.layoutRegisterPassword);
        edEmail = findViewById(R.id.editTextRegisterEmail);
        layEmail = findViewById(R.id.layoutRegisterEmail);
        edFullName = findViewById(R.id.editTextRegisterFullName);
        layFullName = findViewById(R.id.layoutRegisterFullName);
        edUserName = findViewById(R.id.editTextRegisterUserName);
        layUsername = findViewById(R.id.layoutRegisterUserName);
        edContact = findViewById(R.id.editTextRegisterContact);
        layContact = findViewById(R.id.layoutRegisterContact);
        button = findViewById(R.id.buttonRegister);
        txtView = findViewById(R.id.textViewLoginUser);
        edBirthdate = findViewById(R.id.editTextRegisterBirthday);
        layBirthdate = findViewById(R.id.layoutRegisterBirthday);
        mAuth = FirebaseAuth.getInstance();

        txtView.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });


        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                if(password.length() >= 8){
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(password);
                    boolean isPwdContainsSpeChar = matcher.find();
                    if (isPwdContainsSpeChar){
                        layPassword.setHelperText("Strong Password");
                        layPassword.setError("");
                    }else {
                        layPassword.setHelperText("");
                        layPassword.setError("Weak Password. Include minimum 1 special char");
                    }
                }else {
                    layPassword.setHelperText("Enter minimum 8 char");
                    layPassword.setError("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(view -> {
            String email = Objects.requireNonNull(edEmail.getText()).toString();
            String birthdate = Objects.requireNonNull(edBirthdate.getText()).toString();
            String password = Objects.requireNonNull(edPassword.getText()).toString();
            String username = Objects.requireNonNull(edUserName.getText()).toString();
            String fullName = Objects.requireNonNull(edFullName.getText()).toString();
            String contact = Objects.requireNonNull(edContact.getText()).toString();

            if (TextUtils.isEmpty(email)){
                layEmail.setError("Email is required");
            }else{
                layEmail.setHelperText(" ");
            }
            if (TextUtils.isEmpty(password)){
                layPassword.setError("Password is Required");
            }else{
                layEmail.setHelperText(" ");
            }
            if (TextUtils.isEmpty(username)){
                layUsername.setError("Username is required");
            }else{
                layEmail.setHelperText(" ");
            }
            if (TextUtils.isEmpty(fullName)){
                layFullName.setError("fullName is required");
            }else{
                layEmail.setHelperText(" ");
            }
            if (TextUtils.isEmpty(contact)){
                layContact.setError("Contact is required");
            }else{
                layEmail.setHelperText(" ");
            }
           if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(contact)){
               if (!email.matches(emailPattern)){
                   Toast.makeText(RegisterActivity.this, "Please Enter correct Email", Toast.LENGTH_SHORT).show();
                   return;
               }
               if(isValid(password)){

                   mAuth.createUserWithEmailAndPassword(email, password)
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       FirebaseUser usernameInFirebase = mAuth.getCurrentUser();
                                       String UserID= Objects.requireNonNull(usernameInFirebase).getEmail();
                                       String resultEmail = Objects.requireNonNull(UserID).replace(".","");
                                       User user = new User(fullName, username, email, birthdate,contact);
                                       FirebaseDatabase.getInstance().getReference("users")
                                               .child(resultEmail).child("UserDetails")
                                               .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       if (task.isSuccessful()) {
                                                           Toast.makeText(RegisterActivity.this, "Registration successful.",
                                                                   Toast.LENGTH_SHORT).show();
                                                           startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                           finish();
                                                       } else {
                                                           Toast.makeText(RegisterActivity.this, "Failed to create user data.",
                                                                   Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               });
                                   } else {
                                       Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                               Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                }
               else {
                   Toast.makeText(getApplicationContext(), "Password must contain at least 6 characters, having letters,digits and special symbols" , Toast.LENGTH_SHORT).show();

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

}