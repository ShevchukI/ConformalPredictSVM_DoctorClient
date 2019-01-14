package com.controllers.requests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.models.Specialization;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Admin on 13.01.2019.
 */
public class SpecializationController {

    private final static String HOSTNAME = "localhost";
    private final static int PORT = 8888;
    private final static String SCHEME = "http";
    private final static String URL = "http://localhost:8888";

    public ArrayList getAllSpecialization() throws IOException {

        HttpHost targetHost = new HttpHost(HOSTNAME, PORT, SCHEME);

        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());

//      Add AuthCache to the execution context
        final HttpClientContext context = HttpClientContext.create();
        context.setAuthCache(authCache);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(
                new HttpGet(URL + "/specializations"), context);


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
