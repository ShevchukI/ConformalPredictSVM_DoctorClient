package com.controllers.windows.doctor;

import com.controllers.requests.DoctorController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.tools.Encryptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 06.01.2019.
 */
public class LoginMenuController extends MenuController {

    @Autowired
    RegistrationMenuController registrationMenuController;
    @Autowired
    MainMenuController mainMenuController;
    @Autowired
    HttpResponse response;

    private String key;
    private String vector;
    private DoctorController doctorController = new DoctorController();
    private Encryptor encryptor = new Encryptor();
    private int statusCode;
    private WindowsController windowsController = new WindowsController();

    @FXML
    private TextField textField_Login;
    @FXML
    private PasswordField passwordField_Password;

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance) {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        getMap().clear();
    }

    public void signIn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        if (textField_Login.getText().equals("")) {
            alert.setContentText("Login is empty!");
            alert.showAndWait();
        } else if (passwordField_Password.getText().equals("")) {
            alert.setContentText("Password is empty!");
            alert.showAndWait();
        } else {
            response = doctorController.getDoctorAuth(textField_Login.getText(), passwordField_Password.getText());
            statusCode = response.getStatusLine().getStatusCode();
            if(checkStatusCode(statusCode)){
                fillMap(new Doctor().fromJson(response), textField_Login.getText(), passwordField_Password.getText());
                windowsController.openWindowResizable("menu/mainMenu.fxml", getStage(), getInstance(), mainMenuController,
                        "Main menu", 600, 640);
            }
        }
    }

    public void signUp(ActionEvent event) throws IOException {
        windowsController.openWindow("doctor/registrationMenu.fxml", getStage(), getInstance(), registrationMenuController,
                "Registration", 408, 460);
    }
}
