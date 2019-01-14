package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Patient;
import org.apache.http.HttpResponse;
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
    private int statusCode;

    private final static String HOSTNAME = "localhost";
    private final static int PORT = 8888;
    private final static String SCHEME = "http";
    private final static String URL = "http://localhost:8888";

    public int createPatient(String name, String password, Patient patient) throws IOException {

        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());

        String json = new Gson().toJson(patient);

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(URL + "/create_patient");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        request.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(request);

        statusCode = response.getStatusLine().getStatusCode();

        return statusCode;
    }
}
