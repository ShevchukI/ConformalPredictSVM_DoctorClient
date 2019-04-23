package com.controllers.requests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

/**
 * Created by Admin on 13.01.2019.
 */
public class SpecializationController extends MainController {

    public static HttpResponse getAllSpecialization(){
        String url = getUrl() + "/specializations";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }
}
