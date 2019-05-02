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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Admin on 26.01.2019.
 */
public class PageController extends MainController {

    public static HttpResponse getAllPageByPatientId(int id) {
        String url = getUrl() + "/page/" + id + "/all";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
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
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type founderListType = new TypeToken<ArrayList<Page>>() {
        }.getType();
        pages = gson.fromJson(json, founderListType);
        return pages;
    }

    public static HttpResponse changePage(Page page, int id) {
        Page pageWithoutDate = new Page();
        pageWithoutDate.setPageWithoutDate(page);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(page.getDate());
        String json = new Gson().toJson(pageWithoutDate);
        json = json.substring(0, json.length()-1) + ",\"date\":\"" + date + "\"}";
        String url = getUrl() + "/page/" + id;
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), null, null, request, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse createPage(Page page, int id) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(page.getDate());
        String json = new Gson().toJson(page);
        String[] content = json.split(",");
        content[content.length - 2] = "\"date\":\"" + date + "\"}";
        json = "";
        for (int i = 0; i < content.length - 1; i++) {
            json = json + content[i];
            if (i != content.length - 2){
                json = json + ",";
            }
        }
        String url = getUrl() + "/page/" + id;
        HttpPost request = new HttpPost(url);

        HttpResponse response = crudEntity(new StringEntity(json), request, null, null, null);

        return response;
    }

    public static HttpResponse deletePage(int id) throws IOException {
        String url = getUrl() + "/page/" + id;
        HttpDelete request = new HttpDelete(url);
        HttpResponse response = crudEntity(null, null, null, null, request);
        return response;
    }

    public static HttpResponse getPage(int id) throws IOException {
        String url = getUrl() + "/page/" + id;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

}
