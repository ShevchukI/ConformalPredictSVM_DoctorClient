package com.controllers;

import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Created by Admin on 07.01.2019.
 */
public class MainMenuController {

    private Placeholder placeholder = new Placeholder();

    @FXML
    private MenuBarController menuBarController;

    private Stage stage;

    @FXML
    public void initialize() {
        menuBarController.init(this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage(){
        return stage;
    }

    public void getPlaceholderAlert(ActionEvent event){
        placeholder.getAlert();
    }
}
