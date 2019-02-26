package com.controllers.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.models.Page;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by Admin on 26.01.2019.
 */
public class PageController extends MainController{

    public HttpResponse getAllPageByPatientId(String[] authorization, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl()+"/page/" + id + "/all");
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

    public HttpResponse changePage(String[] authorization, Page page, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        String json = new Gson().toJson(page);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(getUrl()+"/page/"+id);
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

    public HttpResponse createPage(String[] authorization, Page page, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        String json = new Gson().toJson(page);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(getUrl()+"/page/"+id);
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

    public HttpResponse deletePage(String[] authorization, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(getUrl()+"/page/"+id);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }

    public HttpResponse getPage(String[] authorization, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl()+"/page/"+id);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", basicAuthPayload);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }
        return response;
    }
}
