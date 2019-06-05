package com.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.tools.Constant.*;

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

    public static HttpResponse getAllPageByPatientId(int id) {
        String url = getUrl() + "/page/" + id + "/all";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static ArrayList getAllPage(HttpResponse response) {
        ArrayList<Page> pages;
        StringBuilder stringBuilder = new StringBuilder();
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(response.getEntity().getContent());
            String line;
            while ((line = dataInputStream.readLine()) != null) {
                stringBuilder.append(line);
            }
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = stringBuilder.toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type founderListType = new TypeToken<ArrayList<Page>>() {
        }.getType();
        pages = gson.fromJson(json, founderListType);
        return pages;
    }

    public void changePage() {
        Page pageWithoutDate = new Page();
        pageWithoutDate.setPageWithoutDate(this);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(this.getDate());
        String json = new Gson().toJson(pageWithoutDate);
        json = json.substring(0, json.length() - 1) + ",\"date\":\"" + date + "\"}";
        String url = getUrl() + "/page/" + this.getId();
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), null, null, request, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            getAlert(null, "Changed!", Alert.AlertType.INFORMATION);
        }
    }

    public void createPage(int id) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(this.getDate());
        String json = new Gson().toJson(this);
        String[] content = json.split(",");
        content[content.length - 2] = "\"date\":\"" + date + "\"}";
        json = "";
        for (int i = 0; i < content.length - 1; i++) {
            json = json + content[i];
            if (i != content.length - 2) {
                json = json + ",";
            }
        }
        String url = getUrl() + "/page/" + id;
        HttpPost request = new HttpPost(url);
        HttpResponse response = crudEntity(new StringEntity(json), request, null, null, null);
        int statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            getAlert(null, "Saved!", Alert.AlertType.INFORMATION);
        }
    }

    public int deletePage() {
        int statusCode = 0;
        try {
            HttpResponse response = deletePage(this);
            statusCode = response.getStatusLine().getStatusCode();
            if (checkStatusCode(statusCode)) {
                getAlert(null, "Page deleted!", Alert.AlertType.INFORMATION);
                return statusCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return statusCode;
        }
        return statusCode;
    }

    private HttpResponse deletePage(Page page) throws IOException {
        String url = getUrl() + "/page/" + page.getId();
        HttpDelete request = new HttpDelete(url);
        HttpResponse response = crudEntity(null, null, null, null, request);
        return response;
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

    public Date getDatePlusDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return new Date(calendar.getTimeInMillis());
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

    public String getDoctorName() {
        return doctor.getName();
    }

    public String getDoctorSpecialization() {
        return doctor.getSpecialization().getName();
    }


    public String getDoctorInfo() {
        return doctor.getName() + " " + doctor.getSurname();
    }


}
