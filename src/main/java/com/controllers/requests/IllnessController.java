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
        return response;
    }

    public static HttpResponse resultSingleTest(int processId) throws IOException {
        String url = getUrl() + "/illness/result/" + processId + "/start";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);

        return response;
    }

    public static HttpResponse getAllActiveDataSet() throws IOException {
        String url = getUrl() + "/illness/datasets";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }
}
