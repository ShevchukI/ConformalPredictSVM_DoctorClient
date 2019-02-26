package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Patient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.Base64;

/**
 * Created by Admin on 14.01.2019.
 */
public class PatientController extends MainController{

    public HttpResponse createPatient(String[] authorization, Patient patient) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        String json = new Gson().toJson(patient);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(getUrl()+"/patient");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        request.setEntity(new StringEntity(json));
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }

    public HttpResponse getAllPatient(String[] authorization) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl()+"/patient/all");
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

    public HttpResponse findPatient(String[] authorization, String search, int searchType) throws IOException {
        String[] parameter = getSearchParameter(search, searchType);
        String searchName = parameter[0];
        String searchSurname =  parameter[1];
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl()+"/patient/params?name=" + searchName + "&surname=" + searchSurname);
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

    public HttpResponse getPatientPage(String[] authorization, int page, int objectOnPage) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl()+"/patient/all/" + page + "/" + objectOnPage);
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

    public HttpResponse findPatientPage(String[] authorization, String search, int searchType, int page, int objectOnPage) throws IOException {
        String[] parameter = getSearchParameter(search, searchType);
        String searchName = parameter[0];
        String searchSurname =  parameter[1];
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl()+"/patient/params/" + page + "/" + objectOnPage + "?name=" + searchName + "&surname=" + searchSurname);
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

    public String[] getSearchParameter(String search, int searchType) {
        String delimiter = " ";
        String[] subStr = search.split(delimiter);
        String[] parameter = new String[2];
        //all - 0; name - 1; surname - 2;
        switch (searchType) {
            case 0:
                if (subStr.length == 1) {
                    parameter[0] = subStr[0];
                    parameter[1] = subStr[0];
                } else {
                    parameter[0] = subStr[0];
                    parameter[1] = subStr[1];
                }
                break;
            case 1:
                if (subStr.length == 1) {
                    parameter[0] = search;
                    parameter[1] = "";
                } else {
                    parameter[0] = subStr[0];
                    parameter[1] = "";
                }
                break;
            case 2:
                if (subStr.length == 1) {
                    parameter[0] = "";
                    parameter[1] = search;
                } else {
                    parameter[0] = "";
                    parameter[1] = subStr[1];
                }
                break;
        }
        return parameter;
    }
}
