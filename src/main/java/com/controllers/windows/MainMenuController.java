package com.controllers.windows;

import com.models.Doctor;
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

    @FXML
    private MenuBarController menuBarController;

    private Stage stage;

    @FXML
    private Label label_Name;

    @FXML
    public void initialize(Doctor doctor) {
        menuBarController.init(this);
        label_Name.setText(doctor.getName());
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
}
