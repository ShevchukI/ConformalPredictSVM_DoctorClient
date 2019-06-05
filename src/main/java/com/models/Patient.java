package com.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.tools.Constant.*;

/**
 * Created by Admin on 14.01.2019.
 */
public class Patient implements Serializable {

    private int id;
    private String name;
    private String surname;
    private String telephone;
    private String address;
    private String email;

    public Patient(String name, String surname, String telephone, String address, String email) {
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
    }

    public Patient() {
    }

    public int addNew(String name, String surname, String telephone, String address, String email) {
        int statusCode = 0;
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        try {
            HttpResponse response = createPatient(this);
            statusCode = response.getStatusLine().getStatusCode();
            if (checkStatusCode(statusCode)) {
                this.id = getIdFromJson(response);
                return statusCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statusCode;
    }

    private HttpResponse createPatient(Patient patient) {
        String json = new Gson().toJson(patient);
        String url = getUrl() + "/patient";
        HttpPost request = new HttpPost(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), request, null, null, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public int changePatient(String name, String surname, String telephone, String address, String email) {
        int statusCode = 0;
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        HttpResponse response = changePatient(this);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
//                this.id = getIdFromJson(response);
            return statusCode;
        }
        return statusCode;
    }

    private HttpResponse changePatient(Patient patient) {
        String json = new Gson().toJson(patient);
        String url = getUrl() + "/patient/" + patient.getId();
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        try {
            response = crudEntity(new StringEntity(json), null, null, request, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static HttpResponse getPatientPage(int pageIndex) throws IOException {
        String url = getUrl() + "/patient/all/" + pageIndex + "/" + Constant.getObjectOnPage();
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    public static HttpResponse findPatientPage(String search, int searchType, int page) throws IOException {
        String[] parameter = getSearchParameter(search, searchType);
        String searchName = parameter[0];
        String searchSurname = parameter[1];
        String url = getUrl() + "/patient/params/" + page + "/" + Constant.getObjectOnPage() + "?name=" + searchName + "&surname=" + searchSurname;
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
        return response;
    }

    private static String[] getSearchParameter(String search, int searchType) {
        String delimiter = " ";
        String[] subStr = search.split(delimiter);
        String[] parameter = new String[2];
        //all - 0; name - 1; surname - 2;
        switch (searchType) {
            case 0:
                if (subStr.length == 1) {
                    parameter[0] = subStr[0];
                    parameter[1] = subStr[0];
                } else {
                    parameter[0] = subStr[0];
                    parameter[1] = subStr[1];
                }
                break;
            case 1:
                if (subStr.length == 1) {
                    parameter[0] = search;
                    parameter[1] = "";
                } else {
                    parameter[0] = subStr[0];
                    parameter[1] = "";
                }
                break;
            case 2:
                if (subStr.length == 1) {
                    parameter[0] = "";
                    parameter[1] = search;
                } else {
                    parameter[0] = "";
                    parameter[1] = subStr[1];
                }
                break;
        }
        return parameter;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Patient> listFromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        Type patientListType = new TypeToken<ArrayList<Patient>>() {
        }.getType();
        Gson gson = new Gson();
        List<Patient> list = gson.fromJson(json, patientListType);
        return list;
    }

    public int getIdFromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, Integer.class);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
