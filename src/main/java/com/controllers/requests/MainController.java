package com.controllers.requests;

/**
 * Created by Admin on 05.02.2019.
 */
public abstract class MainController {
    private final static String LOCALHOST_URL = "http://localhost:8888";
    private final static String DOCTOR_URL = "/doctor-system/doctor";
    private final static String URL = LOCALHOST_URL + DOCTOR_URL;

    protected String getUrl(){
        return URL;
    }

}
