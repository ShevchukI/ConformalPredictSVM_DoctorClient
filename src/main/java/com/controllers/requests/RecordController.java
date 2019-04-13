package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Record;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 26.01.2019.
 */
public class RecordController extends MainController {

    public HttpResponse changeRecord(String[] authorization, Record record, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        String json = new Gson().toJson(record);
        System.out.println(json);

        String[] content = json.split(",");
        String recordDate = null;
        String dateToJson;
        for(int i = 0; i<content.length; i ++){
            if(content[i].indexOf("birthday")>=0){
                recordDate = content[i].substring(12, content[i].length()) +","+content[i+1].substring(0, 5);
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
                content[i] = content[i].substring(0, 12) + dateToJson;
//                content[i+1] = content[i+1].substring(content[i+1].length()-1);
                content[i+1] = "\"}";
                break;
            }
        }
        json = "";
        for (int i = 0; i<content.length; i ++){
            json = json + content[i];
            if(i != content.length-2){
                json = json + ",";
            }
        }
        System.out.println(json);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
//        recordDate = recordDate.substring(0, recordDate.length()-1);
//        System.out.println(recordDate);
//        Date date = null;
//        try {
//            date = simpleDateFormat.parse(recordDate);
//            System.out.println(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//        dateToJson = simpleDateFormat1.format(date);
//        System.out.println(recordDate);
//        System.out.println(dateToJson);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(getUrl()+"/record/" + id);
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

    public HttpResponse getRecordByPatientId(String[] authorization, int id) throws IOException {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((authorization[0] + ":" + authorization[1]).getBytes());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl()+"/record/" + id);
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
}
