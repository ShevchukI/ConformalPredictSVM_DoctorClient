package com.controllers.windows;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.Doctor;
import com.models.Page;
import com.models.Patient;
import com.tools.Placeholder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Admin on 10.01.2019.
 */
public abstract class MenuController {

    @Autowired
    Doctor doctor;

    @Autowired
    Patient patient;

    @Autowired
    HazelcastInstance instance;

    @Autowired
    IMap<String, String> userMap;

    private Placeholder placeholder = new Placeholder();

    private Stage stage;

    private Stage newWindow;

    private TableView tableView;

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

    public void initialize(Patient patient, Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        setPatient(patient);
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void initialize(Patient patient, ArrayList<Page> pages, int row, Stage stage, HazelcastInstance hazelcastInstance, String action) {
        setPatient(patient);
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        setNewWindow(newWindow);

    }

    public void setNewWindow(Stage newWindow) {
        this.newWindow = newWindow;
    }

    public Stage getNewWindow() {
        return newWindow;
    }



    public TableView getTableView() {
        return tableView;
    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, MainMenuController controller) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        setNewWindow(newWindow);
    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow, ObservableList<Patient> patientObservableList,
                           TableView<Patient> tableView_PatientTable) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        setNewWindow(newWindow);
    }
}
