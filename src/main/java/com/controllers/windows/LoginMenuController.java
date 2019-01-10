package com.controllers.windows;

import com.controllers.requests.AuthorizationController;
import com.models.Doctor;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    AuthorizationController authorizationController = new AuthorizationController();

    private int statusCode;


    private Placeholder placeholder = new Placeholder();

    //private Stage stage;

    private WindowsController windowsController = new WindowsController();

    // private String getPlaceholderAlert = "PLACEHOLDER";

    @FXML
    private TextField textField_Login;

    @FXML
    private PasswordField passwordField_Password;

    @FXML
    private Button button_SignIn;

    @FXML
    private Button button_SignUp;


//    public void getPlaceholderAlert(ActionEvent event) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText(getPlaceholderAlert);
//        alert.setHeaderText(null);
//        alert.showAndWait();
//    }

//    public void getPlaceholderAlert() {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText(getPlaceholderAlert);
//        alert.setHeaderText(null);
//        alert.showAndWait();
//    }

//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }

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
            response = authorizationController.getDoctorAuth(textField_Login.getText(), passwordField_Password.getText());

            statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                System.out.println("Status code: " + statusCode);

                Doctor doctor = new Doctor().fromJson(response);

                //TODO

                windowsController.openWindowResizable("mainMenu.fxml", this.getStage(), mainMenuController, doctor, "Main menu", 600, 500);
            } else {
                alert.setHeaderText("Status code: " + statusCode);
                alert.setContentText("Login or password incorrect!");
                alert.showAndWait();
            }
        }


//        } else if (textField_Login.getText().equals(passwordField_Password.getText())) {
//
//            //TODO
//
//            statusCode = authorizationController.getDoctorAuth(textField_Login.getText(), passwordField_Password.getText());
//
//            if (statusCode == 200) {
//                System.out.println(statusCode);
//                windowsController.openWindowResizable("mainMenu.fxml", this.getStage(), mainMenuController, "Main menu", 600, 500);
//            } else if(){
//
//            }
//            // getPlaceholderAlert();
//
//            //getPlaceholderAlert.getAlert();
//
////            windowsController.openWindowResizable("mainMenu.fxml", this.getStage(), mainMenuController, "Main menu", 600, 500);
//
////            FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/mainMenu.fxml"));
////            Pane mainMenuPane = (Pane)mainMenuLoader.load();
////            Scene mainMenuScene = new Scene(mainMenuPane);
////            stage.setScene(mainMenuScene);
////            stage.setMinHeight(500);
////            stage.setMinWidth(600);
////            stage.setResizable(true);
////            stage.setTitle("Main menu");
////            MainMenuController mainMenuController = (MainMenuController)mainMenuLoader.getController();
////            mainMenuController.setStage(stage);
////            stage.show();
//
//        } else {
//            alert.setContentText("Login or password incorrect!");
//            alert.showAndWait();
//        }
    }

    public void signUp(ActionEvent event) throws IOException {

        //RegistrationMenuController registrationMenuController = new RegistrationMenuController();

        windowsController.openWindow("registrationMenu.fxml", this.getStage(), registrationMenuController, "Registration", 408, 400);

//        FXMLLoader registrationMenuLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/registrationMenu.fxml"));
//        Pane registrationMenuPane = (Pane) registrationMenuLoader.load();
//        Scene registrationMenuScene = new Scene(registrationMenuPane);
//        stage.setScene(registrationMenuScene);
//        stage.setResizable(false);
//        stage.setTitle("Registration");
//        RegistrationMenuController registrationMenuController = (RegistrationMenuController) registrationMenuLoader.getController();
//        registrationMenuController.setStage(stage);
//        stage.show();
    }


}
