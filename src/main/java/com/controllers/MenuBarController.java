package com.controllers;

import com.tools.Placeholder;
import javafx.event.ActionEvent;

/**
 * Created by Admin on 07.01.2019.
 */
public class MenuBarController {

    private Placeholder placeholder = new Placeholder();

    private MainMenuController mainController;

    public void init(MainMenuController mainMenuController) {
        this.mainController = mainMenuController;
    }

    public void closeApplication(ActionEvent event) {
        mainController.getStage().close();
    }

    public void getPlaceholderAlert(ActionEvent event){
        placeholder.getAlert();
    }
}
