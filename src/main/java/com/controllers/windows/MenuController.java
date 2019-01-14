package com.controllers.windows;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.Doctor;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 10.01.2019.
 */
public abstract class MenuController {

    @Autowired
    Doctor doctor;

    @Autowired
    HazelcastInstance instance;

    @Autowired
    IMap<String, String> userMap;

    private Placeholder placeholder = new Placeholder();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void initialize(Doctor doctor) {
        setDoctor(doctor);
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void getPlaceholderAlert(ActionEvent event) {
        placeholder.getAlert();
    }

    public HazelcastInstance getInstance() {
        return instance;
    }

    public void setInstance(HazelcastInstance hazelcastInstance) {
        instance = hazelcastInstance;
    }

    public IMap getMap() {
        return userMap;
    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
    }

    public void initialize(Doctor doctor, Stage stage, HazelcastInstance hazelcastInstance) {
        setDoctor(doctor);
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
    }
}
