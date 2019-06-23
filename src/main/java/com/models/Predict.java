package com.models;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static com.tools.Constant.crudEntity;
import static com.tools.Constant.getUrl;

/**
 * Created by Admin on 22.02.2019.
 */
public class Predict {
    private int id;
    private int realClass;
    private double pPositive;
    private double pNegative;
    private int predictClass;
    private double confidence;
    private double credibility;
    private double alphaPositive;
    private double alphaNegative;
    private String visibleClass;
    private String visibleCredibility;
    private String visibleConfidence;
    private String visibleParameters;


    public Predict() {
    }

    public Predict(int id, int realClass,
                   double pPositive, double pNegative,
                   int predictClass, double confidence, double credibility,
                   double alphaPositive, double alphaNegative) {
        this.id = id;
        this.realClass = realClass;
        this.pPositive = pPositive;
        this.pNegative = pNegative;
        this.predictClass = predictClass;
        this.confidence = confidence;
        this.credibility = credibility;
        this.alphaPositive = alphaPositive;
        this.alphaNegative = alphaNegative;
    }

    public HttpResponse startSingleTest(int configurationId, ParameterSingleObject parameterSingleObject) {
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

    public HttpResponse resultSingleTest(int processId) throws IOException {
        String url = getUrl() + "/illness/result/" + processId + "/start";
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

    public int getRealClass() {
        return realClass;
    }

    public void setRealClass(int realClass) {
        this.realClass = realClass;
    }

    public double getpPositive() {
        return pPositive;
    }

    public void setpPositive(double pPositive) {
        this.pPositive = pPositive;
    }

    public double getpNegative() {
        return pNegative;
    }

    public void setpNegative(double pNegative) {
        this.pNegative = pNegative;
    }

    public int getPredictClass() {
        return predictClass;
    }

    public void setPredictClass(int predictClass) {
        this.predictClass = predictClass;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getCredibility() {
        return credibility;
    }

    public void setCredibility(double credibility) {
        this.credibility = credibility;
    }

    public String getVisibleConfidence() {
        return visibleConfidence;
    }

    public void setVisibleConfidence(String visibleConfidence) {
        this.visibleConfidence = visibleConfidence;
    }

    public double getAlphaPositive() {
        return alphaPositive;
    }

    public void setAlphaPositive(double alphaPositive) {
        this.alphaPositive = alphaPositive;
    }

    public double getAlphaNegative() {
        return alphaNegative;
    }

    public void setAlphaNegative(double alphaNegative) {
        this.alphaNegative = alphaNegative;
    }

    public String getVisibleClass() {
        if (getCredibility() == 1) {
            switch (getPredictClass()) {
                case 1:
                    return "Positive";
                case -1:
                    return "Negative";
                default:
                    return "Uncertain";
            }
        } else {
            return "Uncertain";
        }
    }

    public void setVisibleClass(String visibleClass) {
        this.visibleClass = visibleClass;
    }

    public String getVisibleCredibility() {
        return visibleCredibility;
    }

    public void setVisibleCredibility(String visibleCredibility) {
        this.visibleCredibility = visibleCredibility;
    }

    public String getVisibleParameters() {
        return visibleParameters;
    }

    public void setVisibleParameters(String visibleParameters) {
        this.visibleParameters = visibleParameters;
    }

    public Predict fromJson(HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return new Gson().fromJson(json, Predict.class);
    }


}
