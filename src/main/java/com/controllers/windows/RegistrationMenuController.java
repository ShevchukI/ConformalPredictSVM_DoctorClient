package com.controllers.windows;

import com.controllers.requests.DoctorController;
import com.controllers.requests.SpecializationController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.models.Specialization;
import com.tools.Placeholder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
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

    private WindowsController windowsController = new WindowsController();

    private Placeholder placeholder = new Placeholder();

    private DoctorController doctorController = new DoctorController();

    private SpecializationController specializationController = new SpecializationController();

    private ObservableList<Specialization> specializations = FXCollections.observableArrayList();

    @FXML
    private TextField textField_Name;

    @FXML
    private TextField textField_Surname;

    @FXML
    private ComboBox<Specialization> comboBox_Specialization;

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
    private Tooltip tooltip_Specialization;

    @FXML
    private Tooltip tooltip_Login;

    @FXML
    private Tooltip tooltip_Password;

    @FXML
    private Tooltip tooltip_ConfirmPassword;

    private Tooltip tooltipError_Name = new Tooltip();
    private Tooltip tooltipError_Surname = new Tooltip();
    private Tooltip tooltipError_Specialization = new Tooltip();
    private Tooltip tooltipError_Login = new Tooltip();
    private Tooltip tooltipError_Password = new Tooltip();
    private Tooltip tooltipError_ConfirmPassword = new Tooltip();

    private int statusCode;

    @FXML
    public void initialize(Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event ->{hazelcastInstance.getLifecycleService().shutdown();});
        setStage(stage);
        setInstance(hazelcastInstance);

        specializations.add(new Specialization(-1, "None"));

        specializations.addAll(specializationController.getAllSpecialization());

        comboBox_Specialization.setItems(specializations);
        comboBox_Specialization.setCellFactory(new Callback<ListView<Specialization>, ListCell<Specialization>>() {
            @Override
            public ListCell<Specialization> call(ListView<Specialization> p) {
                ListCell cell = new ListCell<Specialization>() {
                    @Override
                    protected void updateItem(Specialization item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });
        comboBox_Specialization.setButtonCell(new ListCell<Specialization>() {
            @Override
            protected void updateItem(Specialization t, boolean bln) {
                super.updateItem(t, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(t.getName());
                }
            }
        });
        comboBox_Specialization.setVisibleRowCount(5);
        comboBox_Specialization.getSelectionModel().select(0);
    }

    public void getPlaceholderAlert() {
        placeholder.getAlert();
    }


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

        if (comboBox_Specialization.getSelectionModel().getSelectedItem().getId() == -1) {
            tooltipError_Specialization.setText("You specialization is empty!");
            comboBox_Specialization.setTooltip(tooltipError_Specialization);
            comboBox_Specialization.setStyle("-fx-border-color: red");
        } else {
            comboBox_Specialization.setTooltip(tooltip_Specialization);
            comboBox_Specialization.setStyle("-fx-border-color: inherit");
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

        if (textField_Name.getStyle().equals("-fx-border-color: inherit")
                && textField_Surname.getStyle().equals("-fx-border-color: inherit")
                && comboBox_Specialization.getStyle().equals("-fx-border-color: inherit")
                && textField_Login.getStyle().equals("-fx-border-color: inherit")
                && passwordField_Password.getStyle().equals("-fx-border-color: inherit")
                && passwordField_ConfirmPassword.getStyle().equals("-fx-border-color: inherit")) {

            Doctor doctor = new Doctor(textField_Name.getText(), textField_Surname.getText(),
                    textField_Login.getText(), passwordField_ConfirmPassword.getText());

            statusCode = doctorController.doctorRegistration(doctor, comboBox_Specialization.getSelectionModel().getSelectedItem().getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);

            if (statusCode == 201) {
                alert.setHeaderText("Status code: " + statusCode);
                alert.setContentText("Congratulations, you are registered!");
                alert.showAndWait();
                windowsController.openWindow("loginMenu.fxml", getStage(), getInstance(), loginMenuController, "Login menu", 350, 190);
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
        }
    }

    public void returnToLoginMenu() throws IOException {
        windowsController.openWindow("loginMenu.fxml", getStage(), getInstance(), loginMenuController, "Login menu", 350, 190);
    }
}
