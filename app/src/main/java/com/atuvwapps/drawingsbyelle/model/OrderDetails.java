package com.atuvwapps.drawingsbyelle.model;
//Class to store final Order details to save in Firestore
public class OrderDetails {
    private String name;
    private String number;
    private String email;
    private boolean collection;
    private String address;
    private String county;
    private String eircode;

    public OrderDetails(){}

    public OrderDetails(String name, String number, String email, boolean collection, String address, String county, String eircode) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.collection = collection;
        this.address = address;
        this.county = county;
        this.eircode = eircode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getEircode() {
        return eircode;
    }

    public void setEircode(String eircode) {
        this.eircode = eircode;
    }
}
