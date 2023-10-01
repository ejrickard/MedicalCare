package com.example.medicalcare;

// Import Firebase libraries

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

public class Database {

    DatabaseReference usersRef;
    DatabaseReference cartRef;
    DatabaseReference orderRef;
    DatabaseReference appointmentRef;
    FirebaseAuth mAuth;
    Context context;
    int result;

    public Database(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        appointmentRef = FirebaseDatabase.getInstance().getReference("appointment");
    }

    public int logout()
    {
        result = 0;
        mAuth.signOut();
        Toast.makeText(context,"LOGOUT SUCCESSFUL", Toast.LENGTH_SHORT).show();
        return result = 1;
    }


    public void addCart(String username, String product, float price, String otype) {
        cartRef = FirebaseDatabase.getInstance().getReference("cart"+otype);
        Cart cartItem = new Cart(username, product, price, otype);
        FirebaseUser users = mAuth.getCurrentUser();
        String finalUser= Objects.requireNonNull(users).getEmail();
        String resultEmail = Objects.requireNonNull(finalUser).replace(".","");
        cartRef.child(resultEmail).child(username+"_"+otype).child(product+"_"+otype).setValue(cartItem);
    }
    public void checkCart(String username, String product, ValueEventListener listener) {
        cartRef = FirebaseDatabase.getInstance().getReference("cart");
        Query cartQuery = cartRef.orderByChild("username_product")
                .equalTo(username + "_" + product)
                .limitToFirst(1);
        cartQuery.addListenerForSingleValueEvent(listener);
    }
    public void removeCart(String username, String otype) {
        cartRef = FirebaseDatabase.getInstance().getReference("cart" + otype);

        FirebaseUser users = mAuth.getCurrentUser();
        String finalUser = Objects.requireNonNull(users).getEmail();
        String resultEmail = Objects.requireNonNull(finalUser).replace(".", "");

        cartRef.child(resultEmail).child(username + "_" + otype).removeValue();
    }

    public void getCartData(String username, String otype, ValueEventListener listener) {
        FirebaseUser users = mAuth.getCurrentUser();
        String finalUser = Objects.requireNonNull(users).getEmail();
        String resultEmail = Objects.requireNonNull(finalUser).replace(".", "");

        cartRef = FirebaseDatabase.getInstance().getReference("cart").child(resultEmail).child(username+"_"+otype);
        cartRef.addListenerForSingleValueEvent(listener);
    }


    public void addOrder(String username, String fullName, String address, String contact, int pinCode, String date, String time, float price, String otype) {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        orderRef = FirebaseDatabase.getInstance().getReference("orderPlace");
        Order order = new Order(username, fullName, address, contact, pinCode, date, time, price, otype);
        FirebaseUser users = mAuth.getCurrentUser();
        String finalUser= Objects.requireNonNull(users).getEmail();
        String resultEmail = Objects.requireNonNull(finalUser).replace(".","");
        orderRef.child(resultEmail).child("order"+randomNumber).setValue(order);
    }
    public void getOrderData(String username, ValueEventListener valueEventListener) {
        orderRef = FirebaseDatabase.getInstance().getReference("orderlab");
        orderRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(valueEventListener);
    }

    public void addAppointment(String username, String appointmentDetails, String address, String contact, String date, String time, float fees, DatabaseReference.CompletionListener completionListener) {
        Appointment appointment = new Appointment(username, appointmentDetails, address, contact, date, time, fees);
        String appointmentKey = appointmentRef.push().getKey();
        if (appointmentKey != null) {
            appointmentRef.child(appointmentKey).setValue(appointment, completionListener);
        }
    }

    public void checkAppointmentExists(String username, String fullName, String address, String contact, String date, String time, AppointmentExistsCallback callback) {
        orderRef = FirebaseDatabase.getInstance().getReference("appointment");

        // Create a query to search for the specific data
        Query query = orderRef.orderByChild("otype").equalTo("medicine");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int result = 0;
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                   // String product = orderSnapshot.child("product").getValue(String.class);
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null && order.getFullName().equals(fullName) && order.getAddress().equals(address) && order.getContact().equals(contact) && order.getDate().equals(date) && order.getTime().equals(time)) {
                        result = 1;
                        break;
                    }
                }
                callback.onAppointmentCheckResult(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
