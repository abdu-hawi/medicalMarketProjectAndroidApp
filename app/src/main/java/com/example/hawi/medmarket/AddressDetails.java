package com.example.hawi.medmarket;

/**
 * Created by Hawi on 20/02/18.
 */

public class AddressDetails {
    private String name,country,city,address;
    private int mobile;

    public AddressDetails() {
    }

    public AddressDetails(String name, String country, String city, String address, int mobile) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }
}
