package com.example.medicalcare;

public class Appointment {

    // Declaration of instance variables
    private String username;
    private String appointmentDetails;
    private String address;
    private String contact;
    private String date;
    private String time;
    private float fees;

    // Constructor to initialize the Appointment object
    public Appointment(String username, String appointmentDetails, String address, String contact, String date, String time, float fees) {
        this.username = username;
        this.appointmentDetails = appointmentDetails;
        this.address = address;
        this.contact = contact;
        this.date = date;
        this.time = time;
        this.fees = fees;
    }

    // getters and setters method
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppointmentDetails() {
        return appointmentDetails;
    }

    public void setAppointmentDetails(String appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getFees() {
        return fees;
    }

    public void setFees(float fees) {
        this.fees = fees;
    }
}

