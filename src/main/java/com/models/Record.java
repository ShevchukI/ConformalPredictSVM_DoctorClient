package com.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.tools.Constant.*;

/**
 * Created by Admin on 26.01.2019.
 */
public class Record {
    private double weight;
    private double height;
    private String bloodGroup;
    private boolean sex;
    private Date birthday;

    public Record() {
    }

    private HttpResponse changeRecord(Record record, int id){
        String json = prepareJson(record);
        String url = getUrl()+"/record/" + id;
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), null, null, request, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return response;
    }

    public int changeRecord(double weight, double height, String bloodGroup, Date birthday, String sex, Patient patient) {
        this.weight = weight;
        this.height = height;
        this.bloodGroup = bloodGroup;
        this.birthday = birthday;
        if (sex.equals("Male")) {
            this.sex = true;
        } else {
            this.sex = false;
        }
        HttpResponse response = changeRecord(this, patient.getId());
        int statusCode = response.getStatusLine().getStatusCode();
        if(checkStatusCode(statusCode)){
            return statusCode;
        } else {
            return 0;
        }
    }

    public static HttpResponse getRecordByPatientId(int id) throws IOException {
        String url = getUrl()+"/record/" + id;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    private static String prepareJson(Record record){
        String json = new Gson().toJson(record);
        String[] content = json.split(",");
        String recordDate = null;
        String dateToJson;
        for (int i = 0; i < content.length; i++) {
            if (content[i].indexOf("birthday") >= 0) {
                recordDate = content[i].substring(12, content[i].length()) + "," + content[i + 1].substring(0, 5);
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

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public boolean isSex() {
        return sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Record fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.fromJson(json, Record.class);
    }

}
