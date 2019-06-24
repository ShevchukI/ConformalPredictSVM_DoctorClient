package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Admin on 03.02.2019.
 */
public class PatientPage {
    private int numberOfPages;
    private List<Patient> patientEntities;
    public PatientPage() {
    }

    public static PatientPage fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, PatientPage.class);
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public List<Patient> getPatientEntities() {
        return patientEntities;
    }

}
