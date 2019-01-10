package com.controllers.windows;

import com.controllers.requests.AuthorizationController;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Admin on 06.01.2019.
 */
public class RegistrationMenuController extends MenuController {

//    private Stage stage;

    @Autowired
    LoginMenuController loginMenuController;

    WindowsController windowsController = new WindowsController();

    Placeholder placeholder = new Placeholder();

    AuthorizationController authorizationController = new AuthorizationController();

    @FXML
    private TextField textField_Name;

    @FXML
    private TextField textField_Surname;

    @FXML
    private TextField textField_Login;

    @FXML
    private PasswordField passwordField_Password;

    @FXML
    private PasswordField passwordField_ConfirmPassword;

    @FXML
    private Tooltip tooltip_Name;

    @FXML
    private Tooltip tooltip_Surname;

    @FXML
    private Tooltip tooltip_Login;

    @FXML
    private Tooltip tooltip_Password;

    @FXML
    private Tooltip tooltip_ConfirmPassword;

    private Tooltip tooltipError_Name = new Tooltip();
    private Tooltip tooltipError_Surname = new Tooltip();
    private Tooltip tooltipError_Login = new Tooltip();
    private Tooltip tooltipError_Password = new Tooltip();
    private Tooltip tooltipError_ConfirmPassword = new Tooltip();

    private int statusCode;


    public void getPlaceholderAlert() {
        placeholder.getAlert();
    }

//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }

    public void register(ActionEvent event) throws IOException {

        if (textField_Name.getText().equals("")) {
            tooltipError_Name.setText("You name is empty!");
            textField_Name.setTooltip(tooltipError_Name);
            textField_Name.setStyle("-fx-border-color: red");
        } else {
            textField_Name.setTooltip(tooltip_Name);
            textField_Name.setStyle("-fx-border-color: inherit");
        }

        if (textField_Surname.getText().equals("")) {
            tooltipError_Surname.setText("You surname is empty!");
            textField_Surname.setTooltip(tooltipError_Surname);
            textField_Surname.setStyle("-fx-border-color: red");
        } else {
            textField_Surname.setTooltip(tooltip_Surname);
            textField_Surname.setStyle("-fx-border-color: inherit");
        }

        if (textField_Login.getText().equals("")) {
            tooltipError_Login.setText("You login is empty!");
            textField_Login.setTooltip(tooltipError_Login);
            textField_Login.setStyle("-fx-border-color: red");
        } else {
            textField_Login.setTooltip(tooltip_Login);
            textField_Login.setStyle("-fx-border-color: inherit");
        }

        if (passwordField_Password.getText().equals("")) {
            tooltipError_Password.setText("You password is empty!");
            passwordField_Password.setTooltip(tooltipError_Password);
            passwordField_Password.setStyle("-fx-border-color: red");
        } else {
            passwordField_Password.setTooltip(tooltip_Password);
            passwordField_Password.setStyle("-fx-border-color: inherit");
        }

        if (passwordField_ConfirmPassword.getText().equals("")) {
            tooltipError_ConfirmPassword.setText("You confirm password is empty");
            passwordField_ConfirmPassword.setTooltip(tooltipError_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: red");

        } else if (!passwordField_ConfirmPassword.getText().equals(passwordField_Password.getText())) {
            tooltipError_ConfirmPassword.setText("Your passwords do not match!");
            passwordField_ConfirmPassword.setTooltip(tooltipError_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: red");

        } else {
            passwordField_ConfirmPassword.setTooltip(tooltip_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: inherit");
        }

        if (textField_Name.getStyle().equals("-fx-border-color: inherit") &&
                textField_Surname.getStyle().equals("-fx-border-color: inherit") &&
                textField_Login.getStyle().equals("-fx-border-color: inherit") &&
                passwordField_Password.getStyle().equals("-fx-border-color: inherit") &&
                passwordField_ConfirmPassword.getStyle().equals("-fx-border-color: inherit")) {

            statusCode = authorizationController.postDoctorRegistration(textField_Name.getText(), textField_Surname.getText(),
                    textField_Login.getText(), passwordField_ConfirmPassword.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            if(statusCode == 200){
                alert.setContentText("Status code: " + statusCode);
                alert.showAndWait();
                windowsController.openWindow("loginMenu.fxml", this.getStage(), loginMenuController, "Login menu", 350, 190);
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Status code:" + statusCode);
                alert.setContentText("Already exist!");
                alert.showAndWait();
            }
        }

    }

    public void cancel(ActionEvent event) {
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert questionOfCancellation = new Alert(Alert.AlertType.WARNING, "Do you really want to leave?", ok, cancel);
        questionOfCancellation.setHeaderText(null);
        Optional<ButtonType> result = questionOfCancellation.showAndWait();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (result.orElse(cancel) == ok) {
            try {
                returnToLoginMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            alert.setContentText("You press Cancel!");
            alert.showAndWait();
        }
    }

    public void returnToLoginMenu() throws IOException {


        windowsController.openWindow("loginMenu.fxml", this.getStage(), loginMenuController, "Login menu", 350, 190);

//        FXMLLoader loginMenuLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/loginMenu.fxml"));
//        Pane loginMenuPane = null;
//        loginMenuPane = (Pane) loginMenuLoader.load();
//        Scene loginMenuScene = new Scene(loginMenuPane);
//        stage.setScene(loginMenuScene);
//        stage.setResizable(false);
//        stage.setTitle("DocClient");
//        LoginMenuController loginMenuController = (LoginMenuController) loginMenuLoader.getController();
//        loginMenuController.setStage(stage);
//        stage.show();

    }
}
