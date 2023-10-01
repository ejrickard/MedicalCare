package com.example.medicalcare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class UserProfileActivity extends AppCompatActivity {

    TextInputEditText fullName, email, phoneNo, birthdate;
    TextView orderLabel, usernameLabel;
    String full_name, e_mail, contact, birth_date,user_name;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    Button update_button;
    ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int IMAGE_WIDTH = 800;  // Specify your desired width
    private static final int IMAGE_HEIGHT = 800; // Specify your desired height
    User user;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    String resultEmail,orders;
    DatabaseReference orderPlaceRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //hooks
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phone_profile);
        birthdate = findViewById(R.id.birthdate_profile);
        update_button = findViewById(R.id.update_profile);
        imageView = findViewById(R.id.profile_image);
        orderLabel = findViewById(R.id.order_label);
        usernameLabel = findViewById(R.id.full_name);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser users = mAuth.getCurrentUser();
        String finalUser = Objects.requireNonNull(users).getEmail();
        resultEmail = Objects.requireNonNull(finalUser).replace(".", "");
        userRef = FirebaseDatabase.getInstance().getReference("users").child(resultEmail).child("UserDetails");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // get data from firebase and store in string variables
                    user_name = dataSnapshot.child("username").getValue(String.class);
                    full_name = dataSnapshot.child("fullName").getValue(String.class);
                    e_mail = dataSnapshot.child("email").getValue(String.class);
                    birth_date = dataSnapshot.child("birthdate").getValue(String.class);
                    contact = dataSnapshot.child("contact").getValue(String.class);

                    // display the data to the textInputEditText fields
                        fullName.setText(full_name);
                        email.setText(e_mail);
                        phoneNo.setText(contact);
                        birthdate.setText(birth_date);
                        usernameLabel.setText(user_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        orderPlaceRef = FirebaseDatabase.getInstance().getReference("orderPlace").child(resultEmail);;

        orderPlaceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalOrders = 0;

                // Iterate through each user's orders
                 if (dataSnapshot.hasChildren()){
                    totalOrders += dataSnapshot.getChildrenCount(); // Count orders for the current user

                }

                // Now 'totalOrders' holds the count of all orders
                // You can use this value in your app as needed
                // For example, you can display it in a TextView
                orders = Integer.toString(totalOrders);
                orderLabel.setText(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });


        update_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = Objects.requireNonNull(fullName.getText()).toString();
                        String newContact = Objects.requireNonNull(phoneNo.getText()).toString();
                        String newEmail = Objects.requireNonNull(email.getText()).toString();
                        String newBirthdate = Objects.requireNonNull(birthdate.getText()).toString();

                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                full_name = dataSnapshot.child("fullName").getValue(String.class);
                                e_mail = dataSnapshot.child("email").getValue(String.class);
                                birth_date = dataSnapshot.child("birthdate").getValue(String.class);
                                contact = dataSnapshot.child("contact").getValue(String.class);
                                if (!full_name.equals(newName) ||
                                        !contact.equals(newContact) ||
                                        !e_mail.equals(newEmail) ||
                                        !birth_date.equals(newBirthdate)) {

                                    // Update the values
                                    userRef.child("fullName").setValue(newName);
                                    userRef.child("contact").setValue(newContact);
                                    userRef.child("email").setValue(newEmail);
                                    userRef.child("birthdate").setValue(newBirthdate);

                                    // Inform the user about the update
                                    // You can display a Toast or a Snack bar here
                                    Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Inform the user that no changes were made
                                    // You can display a Toast or a Snack bar here
                                    Toast.makeText(UserProfileActivity.this, "No changes were made", Toast.LENGTH_SHORT).show();
                                }
                            }
                                }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle errors
                            }
                        });
                    }
                });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            // Resize the selected image
                            Bitmap resizedBitmap = resizeImage(selectedImageUri, IMAGE_WIDTH, IMAGE_HEIGHT);

                            // Upload the resized image to Firebase
                            uploadImageToFirebase(selectedImageUri, resizedBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });



        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });


        // Assuming you have a User object with the profile image URL
        user = new User(); // Initialize the user object
        String profileImageUrl = user.getProfileImageUrl();
// Load and display the profile image using Glide
        Glide.with(this).load(profileImageUrl).into(imageView);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Resize the selected image and upload to Firebase Storage
            try {
                Bitmap resizedBitmap = resizeImage(imageUri, IMAGE_WIDTH, IMAGE_HEIGHT);
                uploadImageToFirebase(imageUri, resizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri, Bitmap resizedBitmap) {
        String resultEmail = mAuth.getCurrentUser().getEmail().replace(".", "");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(resultEmail);
        StorageReference profileImageRef = storageRef.child("profile_images/" + UUID.randomUUID().toString());

        // Convert the resizedBitmap to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        profileImageRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        // Now you can update the user's profile with this downloadUrl
                        updateProfileWithImage(downloadUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle upload failure
                    e.printStackTrace();
                });
    }


    private void updateProfileWithImage(String downloadUrl) {
        // Assuming you have a User class or FirebaseUser instance
        // Update the user's profile information (e.g., profileImageUrl)
        user.setProfileImageUrl(downloadUrl);

        // Update the user's profile information in the database
        DatabaseReference profileImageRef = FirebaseDatabase.getInstance().getReference("users")
                .child(resultEmail).child("UserDetails").child("profileImageUrl");
        profileImageRef.setValue(downloadUrl)
                .addOnSuccessListener(aVoid -> {
                    // Display a success message to the user
                    Toast.makeText(UserProfileActivity.this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    e.printStackTrace();
                });
    }

    private Bitmap resizeImage(Uri imageUri, int desiredWidth, int desiredHeight) throws IOException {
        InputStream input = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();

        return Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, false);
    }


}