package com.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Admin on 06.01.2019.
 */
public class LoginMenuController {

    private Stage stage;

    private String placeholder = "PLACEHOLDER";

    @FXML
    private TextField textField_Login;

    @FXML
    private PasswordField passwordField_Password;

    @FXML
    private Button button_SignIn;

    @FXML
    private Button button_SignUp;


    public void placeholder(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(placeholder);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void placeholder() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(placeholder);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void signIn(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        if(textField_Login.getText().equals("")){
            alert.setContentText("Login is empty!");
            alert.showAndWait();
        } else if(passwordField_Password.getText().equals("")){
            alert.setContentText("Password is empty!");
            alert.showAndWait();
        } else if (textField_Login.getText().equals(passwordField_Password.getText())) {

            //TODO
            placeholder();

        } else {
            alert.setContentText("Login or password incorrect!");
            alert.showAndWait();
        }
    }

    public void signUp(ActionEvent event) throws IOException {
        FXMLLoader registrationMenuLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/registrationMenu.fxml"));
        Pane registrationMenuPane = (Pane) registrationMenuLoader.load();
        Scene registrationMenuScene = new Scene(registrationMenuPane);
        stage.setScene(registrationMenuScene);
        stage.setResizable(false);
        stage.setTitle("Registration");
        RegistrationMenuController registrationMenuController = (RegistrationMenuController) registrationMenuLoader.getController();
        registrationMenuController.setStage(stage);
        stage.show();
    }


}
