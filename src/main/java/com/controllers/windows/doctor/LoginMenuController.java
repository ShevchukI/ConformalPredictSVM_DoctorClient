package com.controllers.windows.doctor;

import com.controllers.requests.DoctorController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Doctor;
import com.tools.Constant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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


    private DoctorController doctorController = new DoctorController();
    private int statusCode;
    private WindowsController windowsController = new WindowsController();

    @FXML
    private TextField textField_Login;
    @FXML
    private PasswordField passwordField_Password;
    @FXML
    private Button button_SignIn;
    @FXML
    private Button button_SignUp;

    public void initialize(Stage stage) {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        Constant.clearInstance();
//        button_SignIn.setGraphic(new ImageView("/img/icons/SISmall.png"));
//        button_SignUp.setGraphic(new ImageView("/img/icons/SUSmall.png"));
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
            String[] authorization = new String[2];
            authorization[0] = textField_Login.getText();
            authorization[1] = passwordField_Password.getText();
            response = doctorController.getDoctorAuth(authorization);
            statusCode = response.getStatusLine().getStatusCode();
            if(checkStatusCode(statusCode)){
                Constant.fillMap(new Doctor().fromJson(response), authorization);
                windowsController.openWindowResizable("menu/mainMenu", getStage(),  mainMenuController,
                        "Main menu", 700, 680);
            }
        }
    }

    public void signUp(ActionEvent event) throws IOException {
        windowsController.openWindow("doctor/registrationMenu", getStage(), registrationMenuController,
                "Registration", 408, 520);
    }
}
