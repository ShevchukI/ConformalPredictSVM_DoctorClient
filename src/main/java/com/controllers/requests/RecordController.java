package com.controllers.requests;

import com.google.gson.Gson;
import com.models.Record;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 26.01.2019.
 */
public class RecordController extends MainController {

    public static HttpResponse changeRecord(Record record, int id){
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
}
