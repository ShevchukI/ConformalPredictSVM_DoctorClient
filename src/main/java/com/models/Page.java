package com.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

/**
 * Created by Admin on 26.01.2019.
 */
public class Page {

    private int id;
    private String theme;
    private String description;
    private String parameters;
    private String answer;
    private Date date;
    private Doctor doctor;

    public Page() {
    }

    public Page(String theme, String description, String parameters, String answer, Date date) {
        this.theme = theme;
        this.description = description;
        this.parameters = parameters;
        this.answer = answer;
        this.date = date;
    }

    public Page(String theme, String description, String parameters, String answer) {
        this.theme = theme;
        this.description = description;
        this.parameters = parameters;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPageWithoutDate(Page page) {
        this.id = page.getId();
        this.theme = page.getTheme();
        this.description = page.getDescription();
        this.parameters = page.getParameters();
        this.answer = page.getAnswer();
        this.doctor = page.getDoctor();
    }

    public Page fromResponse(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.fromJson(json, Page.class);
    }

    public String getDoctorName(){
        return doctor.getName();
    }

    public String getDoctorSpecialization(){
        return doctor.getSpecialization().getName();
    }


    public String getDoctorInfo(){
        return doctor.getName() + " " + doctor.getSurname();
    }

//    public Date getDateFormatter(){
//        r
//    }
}
