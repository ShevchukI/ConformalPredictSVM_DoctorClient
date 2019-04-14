package com.controllers.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.models.Page;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 26.01.2019.
 */
public class PageController extends MainController {

    public static HttpResponse getAllPageByPatientId(int id) {
        String url = getUrl() + "/page/" + id + "/all";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl()+"/page/" + id + "/all");
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

    public static ArrayList getAllPage(HttpResponse response) {
        ArrayList<Page> pages;
        StringBuilder stringBuilder = new StringBuilder();
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(response.getEntity().getContent());
            String line;
            while ((line = dataInputStream.readLine()) != null) {
                stringBuilder.append(line);
            }
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = stringBuilder.toString();
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type founderListType = new TypeToken<ArrayList<Page>>() {
        }.getType();
        pages = gson.fromJson(json, founderListType);
        return pages;
    }

    public static HttpResponse changePage(Page page, int id) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(page.getDate());
        String json = new Gson().toJson(page);
        String[] content = json.split("date\":");
//        content[content.length-1] = "\""+date+"\"}";
        json =content[0]+"date\":"+"\""+date+"\"}";
//        for(int i = 0; i<content.length; i++){
//            json = json + content[i];
//            if(i<content.length){
//                json = json + ":";
//            }
//        }
        String url = getUrl() + "/page/" + id;
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), null, null, request, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        String json = new Gson().toJson(page);
//        json = dataFormat(json);
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpPut request = new HttpPut(getUrl()+"/page/"+id);
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

    public static HttpResponse createPage(Page page, int id) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(page.getDate());
        String json = new Gson().toJson(page);
        String[] content = json.split(":");
        content[2] = "\""+date+"\"}";
        json = content[0] + ":" + content[1] +":" + content[2];
        String url = getUrl() + "/page/" + id;
        HttpPost request = new HttpPost(url);

        HttpResponse response = crudEntity(new StringEntity(json), request, null, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        String json = new Gson().toJson(page);
//        json = dataFormat(json);
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost(getUrl() + "/page/" + id);
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

    public static HttpResponse deletePage(int id) throws IOException {
        String url = getUrl() + "/page/" + id;
        HttpDelete request = new HttpDelete(url);
        HttpResponse response = crudEntity(null, null, null, null, request);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpDelete request = new HttpDelete(getUrl() + "/page/" + id);
//        request.setHeader("Content-Type", "application/json");
//        request.setHeader("Authorization", basicAuthPayload);
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
    }

    public static HttpResponse getPage(int id) throws IOException {
        String url = getUrl() + "/page/" + id;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null,null, request, null, null);
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl() + "/page/" + id);
//        request.setHeader("Content-Type", "application/json");
//        request.setHeader("Authorization", basicAuthPayload);
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
    }

    public String dataFormat(String json) {
        String[] content = json.split(",");
        String recordDate = null;
        String dateToJson;
        for (int i = 0; i < content.length; i++) {
            if (content[i].indexOf("date") >= 0) {
                recordDate = content[i].substring(8, content[i].length()) + "," + content[i + 1].substring(0, 5);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                System.out.println(recordDate);
                Date date = null;
                try {
                    date = simpleDateFormat.parse(recordDate);
                    System.out.println(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                dateToJson = simpleDateFormat1.format(date);
                System.out.println(recordDate);
                System.out.println(dateToJson);
                content[i] = content[i].substring(0, 8) + dateToJson;
                content[i + 1] = "\"}";
                break;
            }
        }
        json = "";
        for (int i = 0; i < content.length; i++) {
            json = json + content[i];
            if (i != content.length - 2) {
                json = json + ",";
            }
        }
        return json;
    }
}
