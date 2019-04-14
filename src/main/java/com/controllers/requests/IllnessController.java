package com.controllers.requests;


import com.google.gson.Gson;
import com.models.ParameterSingleObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Admin on 22.02.2019.
 */
public class IllnessController extends MainController {

    public static HttpResponse startSingleTest(int configurationId, ParameterSingleObject parameterSingleObject) {
        String json = new Gson().toJson(parameterSingleObject);
        String url = getUrl() + "/illness/result/" + configurationId + "/start";
        HttpPost request = new HttpPost(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), request, null, null, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost(getUrl() + "/illness/result/" + configurationId + "/start");
//        request.addHeader("Authorization", basicAuthPayload);
//        request.setHeader("Content-Type", "application/json");
//        request.setEntity(new StringEntity(json));
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        }
        return response;
    }

    public static HttpResponse resultSingleTest(int processId) throws IOException {
        String url = getUrl() + "/illness/result/" + processId + "/start";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl() + "/illness/result/" + processId + "/start");
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

    public static HttpResponse getAllActiveDataSet() throws IOException {
        String url = getUrl() + "/illness/datasets";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl() + "/illness/datasets");
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
}
