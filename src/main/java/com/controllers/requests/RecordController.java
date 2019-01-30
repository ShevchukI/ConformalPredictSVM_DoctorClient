package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Record;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Base64;

/**
 * Created by Admin on 26.01.2019.
 */
public class RecordController {

    private final static String URL = "http://localhost:8888";

    public HttpResponse changeRecord(String name, String password, Record record, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        String json = new Gson().toJson(record);
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPut request = new HttpPut(URL + "/doctor-system/doctor/record/"+id);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        request.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(request);
        return response;
    }

    public HttpResponse getRecordByPatientId(String name, String password, int id) throws IOException {

        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL + "/doctor-system/doctor/record/"+id);

        // add request header
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = client.execute(request);

        return  response;
    }


}
