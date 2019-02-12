package com.controllers.windows.menu;

import com.controllers.requests.SpecializationController;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.Doctor;
import com.models.Page;
import com.models.Patient;
import com.models.Specialization;
import com.tools.Encryptor;
import com.tools.Placeholder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
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
    protected Patient patient;
    @Autowired
    protected HazelcastInstance instance;
    @Autowired
    protected IMap<String, Object> userMap;

    private Placeholder placeholder = new Placeholder();
    private Stage stage;
    private Stage newWindow;
    private SpecializationController specializationController = new SpecializationController();
    private ObservableList<Specialization> specializations = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
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

    }

    public void initialize(Patient patient, Stage stage, HazelcastInstance hazelcastInstance) throws IOException {

    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void initialize(Patient patient, ArrayList<Page> pages, int row, Stage stage, HazelcastInstance hazelcastInstance, String action) {

    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow) throws IOException {

    }

    public void setNewWindow(Stage newWindow) {
        this.newWindow = newWindow;
    }

    public Stage getNewWindow() {
        return newWindow;
    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow, ObservableList<Patient> patientObservableList,
                           TableView<Patient> tableView_PatientTable) throws IOException {

    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow, boolean change) throws IOException {

    }

    public boolean checkStatusCode(int statusCode) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        switch (statusCode) {
            case 200:
                return true;
            case 201:
                return true;
            case 401:
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Unauthorized: login or password incorrect!");
                alert.setContentText("Error code: " + statusCode);
                alert.showAndWait();
                return false;
            case 504:
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Connection to the server is missing!");
                alert.setContentText("Error code: " + statusCode);
                alert.showAndWait();
                return false;
            default:
                return false;
        }
    }

    public void fillMap(Doctor doctorFromJson, String login, String password) {
        Doctor doctor = doctorFromJson;
        String key;
        String vector;
        key = new Encryptor().genRandString();
        vector = new Encryptor().genRandString();
        getMap().put("key", key);
        getMap().put("vector", vector);
        getMap().put("login", new Encryptor().encrypt(key, vector, login));
        getMap().put("password", new Encryptor().encrypt(key, vector, password));
        getMap().put("id", doctor.getId());
        getMap().put("name", doctor.getName());
        getMap().put("surname", doctor.getSurname());
        if (doctor.getSpecialization() != null) {
            getMap().put("specId", doctor.getSpecialization().getId());
            getMap().put("specName", doctor.getSpecialization().getName());
        } else {
            getMap().put("specId", "-2");
            getMap().put("specName", "Empty");
        }
        getMap().put("pageIndex", "1");
    }
}
