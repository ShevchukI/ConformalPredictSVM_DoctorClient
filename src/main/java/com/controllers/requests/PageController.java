package com.controllers.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.models.Page;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by Admin on 26.01.2019.
 */
public class PageController {

    private final static String URL = "http://localhost:8888";


    public HttpResponse getAllPageByPatientId(String name, String password, int id) throws IOException {

        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL + "/doctor-system/doctor/page/" + id + "/all");

        // add request header
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = client.execute(request);

        return response;
    }

    public ArrayList getAllPage(HttpResponse response) throws IOException {
        ArrayList<Page> pages;

        StringBuilder stringBuilder = new StringBuilder();
        DataInputStream dataInputStream = new DataInputStream(response.getEntity().getContent());
        String line;
        while ((line = dataInputStream.readLine()) != null) {
            stringBuilder.append(line);
        }
        dataInputStream.close();
        String json = stringBuilder.toString();

        Gson gson = new Gson();

        Type founderListType = new TypeToken<ArrayList<Page>>() {
        }.getType();
        pages = gson.fromJson(json, founderListType);

        return pages;
    }

    public HttpResponse changePage(String name, String password, Page page, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        String json = new Gson().toJson(page);
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPut request = new HttpPut(URL + "/doctor-system/doctor/page/"+id);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        request.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(request);
        return response;
    }

    public HttpResponse createPage(String name, String password, Page page, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        String json = new Gson().toJson(page);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(URL + "/doctor-system/doctor/page/"+id);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        request.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(request);
        return response;
    }

    public HttpResponse deletePage(String name, String password, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(URL + "/doctor-system/doctor/page/"+id);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        HttpResponse response = client.execute(request);
        return response;
    }
}
