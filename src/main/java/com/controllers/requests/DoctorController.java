package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Doctor;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
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
 * Created by Admin on 10.01.2019.
 */
public class DoctorController {



    private final static String HOSTNAME = "localhost";
    private final static int PORT = 8888;
    private final static String SCHEME = "http";
    private final static String URL = "http://localhost:8888";

    public HttpResponse getDoctorAuth(String name, String password) throws IOException {

        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL + "/doctor_auth");

        // add request header
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = client.execute(request);

//        HttpHost targetHost = new HttpHost(HOSTNAME, PORT, SCHEME);
//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(name, password));
//
//        AuthCache authCache = new BasicAuthCache();
//        authCache.put(targetHost, new BasicScheme());
//
////      Add AuthCache to the execution context
//        final HttpClientContext context = HttpClientContext.create();
//        context.setCredentialsProvider(credsProvider);
//        context.setAuthCache(authCache);
//
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpResponse response = client.execute(
//                new HttpGet(URL + "/doctor_auth"), context);

//        statusCode = response.getStatusLine().getStatusCode();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//        String json = reader.readLine();
//
//
//        Doctor data = new Gson().fromJson(json, Doctor.class);
//
//        System.out.println(data.getName() + " " + data.getSurname());


        return response;
    }

    public int postDoctorRegistration(String name, String surname, int specialization, String login, String password) {
        int statusCode;

        Client client = Client.create();

        WebResource webResource = client
                .resource(URL + "/doctor_registration/"+specialization);


        Doctor doctor = new Doctor(name, surname, login, password);

        String json = new Gson().toJson(doctor);

//        String input = "{\"name\":\"" + name + "\"," +
//                "\"surname\":\"" + surname + "\"," +
//                "\"login\":\"" + login + "\"," +
//                "\"password\":\"" + password + "\"}";

        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, json);

        statusCode = response.getStatus();

        return statusCode;
    }


    public int doctorRegistration(Doctor doctor, int specialization) throws IOException {

        String json = new Gson().toJson(doctor);

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(URL + "/doctor_registration/"+specialization);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(json));

        HttpResponse response = client.execute(request);


        return response.getStatusLine().getStatusCode();
    }
}
