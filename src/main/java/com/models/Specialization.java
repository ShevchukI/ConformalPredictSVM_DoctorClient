package com.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.controllers.requests.MainController.crudEntity;
import static com.controllers.requests.MainController.getUrl;

/**
 * Created by Admin on 13.01.2019.
 */
public class Specialization implements Serializable{

    private int id;
    private String name;

    public Specialization() {

    }

    public Specialization(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static HttpResponse getAllSpecialization(){
        String url = getUrl() + "/specializations";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Specialization> getListFromResponse(HttpResponse response) throws IOException {
        ArrayList<Specialization> specializations;
        if (response.getStatusLine().getStatusCode() == 200) {
            StringBuilder stringBuilder = new StringBuilder();
            DataInputStream dataInputStream = new DataInputStream(response.getEntity().getContent());
            String line;
            while ((line = dataInputStream.readLine()) != null) {
                stringBuilder.append(line);
            }
            dataInputStream.close();
            String json = stringBuilder.toString();
            Gson gson = new Gson();
            Type founderListType = new TypeToken<ArrayList<Specialization>>() {
            }.getType();
            specializations = gson.fromJson(json, founderListType);
        } else {
            specializations = new ArrayList<Specialization>();
            specializations.add(new Specialization(-2, "Empty"));
        }
        return specializations;
    }
}
