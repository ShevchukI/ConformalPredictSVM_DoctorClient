package com.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.tools.Constant.crudEntity;
import static com.tools.Constant.getUrl;

/**
 * Created by Admin on 03.02.2019.
 */
public class Dataset implements Serializable{
    private int id;
    private String name;
    private String description;
    private String columns;


    public Dataset() {
    }
    public Dataset(int id, String name, String columns) {
        this.id = id;
        this.name = name;
        this.columns = columns;
    }

    public Dataset(int id, String name, String description, String columns) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.columns = columns;
    }

    public Dataset(String name, String description, String columns) {
        this.name = name;
        this.description = description;
        this.columns = columns;
    }

    public Dataset(int id, String columns) {
        this.id = id;
        this.columns = columns;
    }

    public static HttpResponse getAllActiveDataSet() throws IOException {
        String url = getUrl() + "/illness/datasets";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public Dataset fromResponse(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, Dataset.class);
    }

    public ArrayList<Dataset> getListFromResponse(HttpResponse response) throws IOException {
        ArrayList<Dataset> datasets;
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
            Type founderListType = new TypeToken<ArrayList<Dataset>>() {
            }.getType();
            datasets = gson.fromJson(json, founderListType);
            return datasets;
        } else {
            return null;
        }
    }

}
