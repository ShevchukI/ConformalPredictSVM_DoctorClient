package com.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Admin on 03.02.2019.
 */
public class Dataset {
    private int id;
    private String name;
    private String description;
    private String columns;


    public Dataset() {
    }
    public Dataset(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
