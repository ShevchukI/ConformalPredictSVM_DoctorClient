package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Patient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Base64;

/**
 * Created by Admin on 14.01.2019.
 */
public class PatientController {


    private final static String URL = "http://localhost:8888";

    private final static String DELIMETER = " ";

    public HttpResponse createPatient(String name, String password, Patient patient) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        String json = new Gson().toJson(patient);
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(URL + "/doctor-system/doctor/patient");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        request.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(request);


        return response;
    }

    public HttpResponse getAllPatient(String name, String password) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL + "/doctor-system/doctor/patient/all");
        // add request header
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = client.execute(request);
        return response;
    }

    public HttpResponse findPatient(String name, String password, String search, int searchType) throws IOException {
        String[] subStr = search.split(DELIMETER);
        String searchName = "";
        String searchSurname = "";
        //all - 0; name - 1; surname - 2;
        switch (searchType) {
            case 0:
                if (subStr.length == 1) {
                    searchName = subStr[0];
                    searchSurname = subStr[0];
                } else {
                    searchName = subStr[0];
                    searchSurname = subStr[1];
                }
                break;
            case 1:
                if(subStr.length == 1){
                    searchName = search;
                    searchSurname="";
                } else {
                    searchName = subStr[0];
                    searchSurname="";
                }
                System.out.println(searchName);
                break;
            case 2:
                if(subStr.length == 1){
                    searchName = "";
                    searchSurname = search;
                } else {
                    searchSurname = subStr[1];
                    searchName = "";
                }
                break;
        }

        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL + "/doctor-system/doctor/patient/params?name="+searchName+"&surname="+searchSurname);
        // add request header
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = client.execute(request);
        return response;
    }

}
