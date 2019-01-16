package com.controllers.windows;

import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 14.01.2019.
 */
public class CardMenuController extends MenuController{

    @Autowired
    MainMenuController mainMenuController;

    @FXML
    private MenuBarController menuBarController;

    private Placeholder placeholder = new Placeholder();

    private WindowsController windowsController = new WindowsController();

    @FXML
    public void initialize(Doctor doctor, Stage stage, HazelcastInstance hazelcastInstance){
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event ->{hazelcastInstance.getLifecycleService().shutdown();});
        setStage(stage);
        setInstance(hazelcastInstance);
        this.doctor = doctor;
        menuBarController.init(this);
    }

    public void getPlaceholderAlert(ActionEvent event){
        placeholder.getAlert();
    }

    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("mainMenu.fxml", getStage(), getInstance(), mainMenuController,"Main menu", 600, 640);

    }


}
