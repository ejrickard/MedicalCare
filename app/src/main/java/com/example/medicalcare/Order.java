package com.example.medicalcare;

public class Order {
    private String username;
    private String fullName;
    private String address;
    private String contact;
    private int pinCode;
    private String date;
    private String time;
    private float amount;
    private String otype;

    Order(){

    }
    Order(String username, String fullName, String address, String contact, int pinCode, String date, String time, float amount, String otype) {
        this.username = username;
        this.fullName = fullName;
        this.address = address;
        this.contact = contact;
        this.pinCode = pinCode;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.otype = otype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }
}

