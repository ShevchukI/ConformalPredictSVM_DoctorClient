package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Doctor;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * Created by Admin on 10.01.2019.
 */
public class AuthorizationController {

    private int statusCode;

    private final static String HOSTNAME = "localhost";
    private final static int PORT = 8888;
    private final static String SCHEME = "http";
    private final static String URL = "http://localhost:8888";

    public HttpResponse getDoctorAuth(String name, String password) throws IOException {

        HttpHost targetHost = new HttpHost(HOSTNAME, PORT, SCHEME);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(name, password));

        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());

//      Add AuthCache to the execution context
        final HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(
                new HttpGet(URL + "/doctor_auth"), context);

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

    public int postDoctorRegistration(String name, String surname, String login, String password) {
        Client client = Client.create();

        WebResource webResource = client
                .resource(URL + "/doctor_registration");


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

}
