package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Patient;
import com.tools.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Admin on 14.01.2019.
 */
public class PatientController extends MainController {

    public static HttpResponse createPatient(Patient patient) {
        String json = new Gson().toJson(patient);
        String url = getUrl() + "/patient";
        HttpPost request = new HttpPost(getUrl() + "/patient");
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), request, null, null, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        String json = new Gson().toJson(patient);
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost(getUrl()+"/patient");
//        request.setHeader("Content-Type", "application/json");
//        request.setHeader("Authorization", basicAuthPayload);
//        request.setEntity(new StringEntity(json));
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
    }

    public static HttpResponse getAllPatient() {
        String url = getUrl() + "/patient/all";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl() + "/patient/all");
//        request.addHeader("Authorization", basicAuthPayload);
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
    }

    public static HttpResponse findPatient(String search, int searchType) throws IOException {
        String[] parameter = getSearchParameter(search, searchType);
        String searchName = parameter[0];
        String searchSurname = parameter[1];
        String url = getUrl() + "/patient/params?name=" + searchName + "&surname=" + searchSurname;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl() + "/patient/params?name=" + searchName + "&surname=" + searchSurname);
//        request.addHeader("Authorization", basicAuthPayload);
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
    }

    public static HttpResponse getPatientPage(int page) throws IOException {
        String url = getUrl() + "/patient/all/" + page + "/" + Constant.getObjectOnPage();
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl() + "/patient/all/" + page + "/" + objectOnPage);
//        request.addHeader("Authorization", basicAuthPayload);
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
    }

    public static HttpResponse findPatientPage(String search, int searchType, int page) throws IOException {
        String[] parameter = getSearchParameter(search, searchType);
        String searchName = parameter[0];
        String searchSurname = parameter[1];
        String url = getUrl() + "/patient/params/" + page + "/" + Constant.getObjectOnPage() + "?name=" + searchName + "&surname=" + searchSurname;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl() + "/patient/params/" + page + "/" + objectOnPage + "?name=" + searchName + "&surname=" + searchSurname);
//        request.addHeader("Authorization", basicAuthPayload);
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
    }

    public static String[] getSearchParameter(String search, int searchType) {
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
