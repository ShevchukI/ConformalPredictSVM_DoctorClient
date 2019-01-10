package com.controllers.windows;

import com.models.Doctor;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Admin on 10.01.2019.
 */
public abstract class MenuController {

    @Autowired
    Doctor doctor;

    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Stage getStage(){
        return  stage;
    }

    public void initialize(Doctor doctor){
        setDoctor(doctor);
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
