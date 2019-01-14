package com.controllers.windows;

import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.tools.Encryptor;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 07.01.2019.
 */
public class MainMenuController extends MenuController {

    @Autowired
    RegistrationMenuController registrationMenuController;

    @Autowired
    AddPatientMenuController addPatientMenuController;

    private Placeholder placeholder = new Placeholder();

    private WindowsController windowsController = new WindowsController();

    @Autowired
    Doctor doctor;

    private Encryptor encryptor = new Encryptor();

   // private Doctor doctor = new Doctor();

//    @Autowired
//    HazelcastInstance hz;
//    HazelcastInstance hz = Hazelcast.newHazelcastInstance();
////    @Autowired
////    IMap<String, String> logmap;
//    IMap<String, String> logmap = hz.getMap("login");
//
//    Encryptor encryptor = new Encryptor();

    @FXML
    private MenuBarController menuBarController;

    private Stage stage;

    @FXML
    private Label label_Name;

    @FXML
    private Label label_Specialization;

    @FXML
    public void initialize(Doctor doctor) {
//        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
//        IMap<String, String> logmap = hz.getMap("login");

        this.doctor = doctor;
        menuBarController.init(this);
        label_Name.setText(doctor.getName());
        label_Specialization.setText(doctor.getSpecialization().getName());
    }

    public void initialize(Doctor doctor, Stage stage,HazelcastInstance hazelcastInstance){
//        setDoctor(doctor);
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event ->{hazelcastInstance.getLifecycleService().shutdown();});
        setStage(stage);
        setInstance(hazelcastInstance);

        this.doctor = doctor;
        menuBarController.init(this);
        label_Name.setText(doctor.getName());
        if(doctor.getSpecialization() == null){
            label_Specialization.setText("Empty!");
        } else{
            label_Specialization.setText(doctor.getSpecialization().getName());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


//    public void setDoctor(Doctor doctor){
//        this.doctor = doctor;
//    }

    public Stage getStage(){
        return stage;
    }

    public void getPlaceholderAlert(ActionEvent event){
        placeholder.getAlert();
    }

    public void addPatient() throws IOException {
//        System.out.println(doctor.getName());
//        System.out.println(doctor.getSurname());
//        System.out.println(doctor.getSpecialization().getName());
//        System.out.println(doctor.getLogin());
//        System.out.println(doctor.getPassword());

        windowsController.openWindow("addPatientMenu.fxml", getStage(), getInstance(), addPatientMenuController, doctor, "Add new patient", 408, 400);

    }

    public void getMap(ActionEvent event){


        System.out.println(getMap().get("key"));
        System.out.println(getMap().get("vector"));
        System.out.println(getMap().get("login") + " :: " + encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()));
        System.out.println(getMap().get("password")+ " :: " + encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()));
        System.out.println(getMap().size());
//        System.out.println("key: "+logmap.get("key"));
//        System.out.println("vector: "+logmap.get("vector"));
//
//        System.out.println(encryptor.decrypt(logmap.get("key"), logmap.get("vector"), logmap.get("login")));
    }
}
