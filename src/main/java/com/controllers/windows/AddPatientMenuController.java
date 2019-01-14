package com.controllers.windows;

import com.controllers.requests.PatientController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.models.Patient;
import com.tools.Encryptor;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Admin on 14.01.2019.
 */
public class AddPatientMenuController extends MenuController {

    private final static String PHONEREG = "[+][3][8][0][0-9]{9}";
    private final static String EMAILREG = "[a-zA-Z0-9]+[@][a-z]+[.][a-z]{1,3}";

    @Autowired
    MainMenuController mainMenuController;

    private WindowsController windowsController = new WindowsController();

    private PatientController patientControleller = new PatientController();

    private Encryptor encryptor = new Encryptor();

    @Autowired
    Doctor doctor;

    private Placeholder placeholder = new Placeholder();

    @FXML
    private TextField textField_Name;

    @FXML
    private TextField textField_Surname;

    @FXML
    private TextField textField_Telephone;

    @FXML
    private TextField textField_Address;

    @FXML
    private TextField textField_Email;


    @FXML
    private Tooltip tooltip_Name;

    @FXML
    private Tooltip tooltip_Surname;

    @FXML
    private Tooltip tooltip_Telephone;

    @FXML
    private Tooltip tooltip_Address;

    @FXML
    private Tooltip tooltip_Email;


    private Tooltip tooltipError_Name = new Tooltip();
    private Tooltip tooltipError_Surname = new Tooltip();
    private Tooltip tooltipError_Telephone = new Tooltip();
    private Tooltip tooltipError_Address = new Tooltip();
    private Tooltip tooltipError_Email = new Tooltip();

    private int statusCode;

    public void initialize(Doctor doctor) {
        this.doctor = doctor;
    }

    public void initialize(Doctor doctor, Stage stage, HazelcastInstance hazelcastInstance) {
        this.doctor = doctor;
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
    }

    public void savePatient(ActionEvent event) throws IOException {

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

        if (textField_Telephone.getText().equals("")) {
            tooltipError_Telephone.setText("You telephone is empty!");
            textField_Telephone.setTooltip(tooltipError_Telephone);
            textField_Telephone.setStyle("-fx-border-color: red");
        } else if (!textField_Telephone.getText().matches(PHONEREG)) {
            tooltipError_Telephone.setText("Your telephone must have the  format: +380XXXXXXXXX");
            textField_Telephone.setTooltip(tooltipError_Telephone);
            textField_Telephone.setStyle("-fx-border-color: red");
        } else {
            textField_Telephone.setTooltip(tooltip_Telephone);
            textField_Telephone.setStyle("-fx-border-color: inherit");
        }

        if (textField_Address.getText().equals("")) {
            tooltipError_Address.setText("You address is empty!");
            textField_Address.setTooltip(tooltipError_Address);
            textField_Address.setStyle("-fx-border-color: red");
        } else {
            textField_Address.setTooltip(tooltip_Address);
            textField_Address.setStyle("-fx-border-color: inherit");
        }

        if (textField_Email.getText().equals("")) {
            tooltipError_Email.setText("You email is empty!");
            textField_Email.setTooltip(tooltipError_Email);
            textField_Email.setStyle("-fx-border-color: red");
        } else if (!textField_Email.getText().matches(EMAILREG)) {
            tooltipError_Email.setText("Your email must have the  format: example@example.com");
            textField_Email.setTooltip(tooltipError_Email);
            textField_Email.setStyle("-fx-border-color: red");
        } else {
            textField_Email.setTooltip(tooltip_Email);
            textField_Email.setStyle("-fx-border-color: inherit");
        }

        if (textField_Name.getStyle().equals("-fx-border-color: inherit")
                && textField_Surname.getStyle().equals("-fx-border-color: inherit")
                && textField_Telephone.getStyle().equals("-fx-border-color: inherit")
                && textField_Address.getStyle().equals("-fx-border-color: inherit")
                && textField_Email.getStyle().equals("-fx-border-color: inherit")) {

            Patient patient = new Patient(textField_Name.getText(), textField_Surname.getText(), textField_Telephone.getText(),
                    textField_Address.getText(), textField_Email.getText());

            statusCode = patientControleller.createPatient(encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()),
                    encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()), patient);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);

            if (statusCode == 201) {
                alert.setHeaderText("Status code: " + statusCode);
                alert.setContentText("Congratulations, patient is registered!");
                alert.showAndWait();
                windowsController.openWindowResizable("mainMenu.fxml", getStage(), getInstance(), mainMenuController, doctor, "Main menu", 600, 640);
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Status code:" + statusCode);
                alert.setContentText("Error!");
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
                returnToMainMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void returnToMainMenu() throws IOException {

        windowsController.openWindowResizable("mainMenu.fxml", getStage(), getInstance(), mainMenuController, doctor, "Main menu", 600, 640);

    }
}
