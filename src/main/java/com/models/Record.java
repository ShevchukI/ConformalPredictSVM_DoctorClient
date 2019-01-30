package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Admin on 26.01.2019.
 */
public class Record {
//    private int id;
    private double weight;
    private double height;
    private String bloodGroup;
    private boolean sex;
    private String birthday;

    public Record() {
    }

    public Record(double weight, double height, String bloodGroup, boolean sex, String birthday) {
        this.weight = weight;
        this.height = height;
        this.bloodGroup = bloodGroup;
        this.sex = sex;
        this.birthday = birthday;
    }

    public Record(double weight, double height, String bloodGroup, String birthday) {
        this.weight = weight;
        this.height = height;
        this.bloodGroup = bloodGroup;
        this.birthday = birthday;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Record fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, Record.class);
    }
}
