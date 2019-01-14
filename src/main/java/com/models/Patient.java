package com.models;

/**
 * Created by Admin on 14.01.2019.
 */
public class Patient {

    private String name;
    private String surname;
    private String telephone;
    private String address;
    private String email;

    public Patient(String name, String surname, String telephone, String address, String email) {
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
