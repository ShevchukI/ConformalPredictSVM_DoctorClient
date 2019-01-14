package com.controllers.windows;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.Doctor;
import com.tools.Encryptor;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by Admin on 07.01.2019.
 */
public class MainMenuController extends MenuController {

    private Placeholder placeholder = new Placeholder();

   // private Doctor doctor = new Doctor();

//    @Autowired
//    HazelcastInstance hz;
    HazelcastInstance hz = Hazelcast.newHazelcastInstance();
//    @Autowired
//    IMap<String, String> logmap;
    IMap<String, String> logmap = hz.getMap("login");

    Encryptor encryptor = new Encryptor();

    @FXML
    private MenuBarController menuBarController;

    private Stage stage;

    @FXML
    private Label label_Name;

    @FXML
    private Label label_Specialization;

    @FXML
    public void initialize(Doctor doctor) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        IMap<String, String> logmap = hz.getMap("login");
        menuBarController.init(this);
        label_Name.setText(doctor.getName());
        label_Specialization.setText(doctor.getSpecialization().getName());
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

    public void getMap(ActionEvent event){

        System.out.println("login: "+logmap.get("login"));
        System.out.println("password: "+logmap.get("password"));
        System.out.println("key: "+logmap.get("key"));
        System.out.println("vector: "+logmap.get("vector"));

        System.out.println(encryptor.decrypt(logmap.get("key"), logmap.get("vector"), logmap.get("login")));
    }
}
