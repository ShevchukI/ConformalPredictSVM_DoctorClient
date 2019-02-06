package com.controllers.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.models.Specialization;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Admin on 13.01.2019.
 */
public class SpecializationController extends MainController{

    public ArrayList getAllSpecialization() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl()+"/specializations");
        HttpResponse response = null;
        try {
            response = client.execute(request);
        }catch (HttpHostConnectException e){
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        }

        ArrayList<Specialization> specializations;
        if (response.getStatusLine().getStatusCode() == 200){
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
