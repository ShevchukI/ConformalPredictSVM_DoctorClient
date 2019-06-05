package com.models;

import com.google.gson.Gson;
import com.tools.Constant;
import com.tools.Encryptor;
import com.tools.HazelCastMap;
import javafx.scene.control.Alert;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;

import java.io.*;
import java.util.Base64;

import static com.controllers.requests.MainController.crudEntity;
import static com.controllers.requests.MainController.getUrl;
import static com.tools.Constant.getAlert;
import static com.tools.HazelCastMap.getDoctorMap;

/**
 * Created by Admin on 06.01.2019.
 */
public class Doctor implements Serializable {

    private int id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private Specialization specialization;

    public Doctor(String name, String surname, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

    public Doctor() {

    }

    public Doctor(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public static HttpResponse getDoctorAuth(String[] authorization) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl() + "/authorization");
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }

    public int changeInfo(String name, String surname, Specialization specialization) {
        this.name = name;
        this.surname = surname;
        this.specialization = specialization;
        HttpResponse response = changeInfo(this);
        int statusCode = response.getStatusLine().getStatusCode();
        if (Constant.checkStatusCode(statusCode)) {
            getDoctorMap().put(1, this);
            getAlert(null, "Information changed!", Alert.AlertType.INFORMATION);
            return statusCode;
        } else {
            return statusCode;
        }
    }

    private HttpResponse changeInfo(Doctor doctor) {
        String json = new Gson().toJson(doctor);
        String url = getUrl() + "/" + doctor.getSpecialization().getId();
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), null, null, request, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void changePassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(Constant.getAuth()[1])) {
            HttpResponse response = changePassword(newPassword);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200 || statusCode == 201) {
                HazelCastMap.getMapByName(HazelCastMap.getUserMapName()).put("password",
                        Encryptor.encrypt(HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get("key").toString(),
                                HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get("vector").toString(),
                                newPassword));
                getAlert(null, "Password changed!", Alert.AlertType.INFORMATION);
            }
        } else {
            getAlert(null, "Error! Current password incorrect, please try again.", Alert.AlertType.ERROR);
        }
    }

    private HttpResponse changePassword(String newPassword) {
        String url = getUrl() + "/psw";
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(newPassword), null, null, request, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }


    public Doctor fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();

        return new Gson().fromJson(json, Doctor.class);
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }


}
