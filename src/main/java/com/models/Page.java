package com.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

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

//    public Page(int id, String theme, String description, String parameters, String answer, String date, Doctor doctor) {
//        this.id = id;
//        this.theme = theme;
//        this.description = description;
//        this.parameters = parameters;
//        this.answer = answer;
//        this.date = date;
//        this.doctor = doctor;
//    }

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
//    public Page(String theme, String description, String parameters, String answer, String date) {
//        this.theme = theme;
//        this.description = description;
//        this.parameters = parameters;
//        this.answer = answer;
//        this.date = date;
//    }

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

//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }

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

//    @Override
//    public String toString() {
//        return "Page{" +
//                "id=" + id +
//                ", theme='" + theme + '\'' +
//                ", description='" + description + '\'' +
//                ", parameters='" + parameters + '\'' +
//                ", answer='" + answer + '\'' +
//                ", date='" + date + '\'' +
//                ", doctor='" + doctor.getName() + '\'' +
//                '}';
//    }

    public void setPage(Page page) {
        this.theme = page.getTheme();
        this.description = page.getDescription();
        this.parameters = page.getParameters();
        this.answer = page.getAnswer();
        this.date = page.getDate();
    }

//    public Page fromResponse(HttpResponse response) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//        String json = reader.readLine();
//        return new Gson().fromJson(json, Page.class);
//    }

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

//    public String getDateFormatted(){
//        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
//        return formatter1.format(date);
//    }

    public String getDoctorInfo(){
        return doctor.getName() + " " + doctor.getSurname();
    }
}
