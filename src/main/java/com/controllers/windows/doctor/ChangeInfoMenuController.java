package com.controllers.windows.doctor;

import com.controllers.requests.DoctorController;
import com.controllers.requests.SpecializationController;
import com.controllers.windows.menu.MenuController;
import com.models.Doctor;
import com.models.Specialization;
import com.tools.Constant;
import com.tools.Encryptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


/**
 * Created by Admin on 29.01.2019.
 */
public class ChangeInfoMenuController extends MenuController {

    @Autowired
    HttpResponse response;

    private SpecializationController specializationController = new SpecializationController();
    private ObservableList<Specialization> specializations = FXCollections.observableArrayList();
    private Tooltip tooltipError_CurrentPassword = new Tooltip();
    private Tooltip tooltipError_NewPassword = new Tooltip();
    private Tooltip tooltipError_ConfirmPassword = new Tooltip();
    private Tooltip tooltipError_Name = new Tooltip();
    private Tooltip tooltipError_Surname = new Tooltip();
    private DoctorController doctorController = new DoctorController();
    private int statusCode;

    @FXML
    private PasswordField passwordField_CurrentPassword;
    @FXML
    private PasswordField passwordField_NewPassword;
    @FXML
    private PasswordField passwordField_ConfirmPassword;
    @FXML
    private TextField textField_Name;
    @FXML
    private TextField textField_Surname;
    @FXML
    private ComboBox<Specialization> comboBox_Specialization;
    @FXML
    private Tooltip tooltip_CurrentPassword;
    @FXML
    private Tooltip tooltip_NewPassword;
    @FXML
    private Tooltip tooltip_ConfirmPassword;
    @FXML
    private Tooltip tooltip_Name;
    @FXML
    private Tooltip tooltip_Surname;
    @FXML
    private Button button_Ok;
    @FXML
    private Button button_Cancel;

    @FXML
    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        setNewWindow(newWindow);
        button_Ok.setGraphic(new ImageView("/img/icons/ok.png"));
        button_Cancel.setGraphic(new ImageView("/img/icons/cancel.png"));
        if (change) {
            specializations.add(new Specialization(-1, "None"));
            response = specializationController.getAllSpecialization();
            statusCode = response.getStatusLine().getStatusCode();
            if(checkStatusCode(statusCode)) {
                specializations.addAll(new Specialization().getListFromResponse(response));
            }
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
            comboBox_Specialization.getSelectionModel().select(Integer.parseInt(Constant.getMapByName("user").get("specId").toString()));
            textField_Name.setText(Constant.getMapByName("user").get("name").toString());
            textField_Surname.setText(Constant.getMapByName("user").get("surname").toString());

        }
    }

    public void savePassword(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (checkPasswords()) {
            if (passwordField_CurrentPassword.getText().equals(Constant.getAuth()[0])) {
                if (passwordField_NewPassword.getText().equals(passwordField_ConfirmPassword.getText())) {
                    response = doctorController.changePassword(Constant.getAuth(), passwordField_ConfirmPassword.getText());
                    statusCode = response.getStatusLine().getStatusCode();
                    if (checkStatusCode(statusCode)) {
                        Constant.getMapByName("user").put("password", new Encryptor().encrypt(Constant.getMapByName("key").get("key").toString(),
                                Constant.getMapByName("key").get("vector").toString(),
                                passwordField_ConfirmPassword.getText().toString()));
                        alert.setContentText("Password changed!");
                        alert.showAndWait();
                        getNewWindow().close();
                    }
                }
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Error! Current password incorrect, please try again.");
                alert.showAndWait();
            }
        }
    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }

    public boolean checkPasswords() {
        if (passwordField_CurrentPassword.getText().equals("")) {
            tooltipError_CurrentPassword.setText("You name is empty!");
            passwordField_CurrentPassword.setTooltip(tooltipError_CurrentPassword);
            passwordField_CurrentPassword.setStyle("-fx-border-color: red");
        } else {
            passwordField_CurrentPassword.setTooltip(tooltip_CurrentPassword);
            passwordField_CurrentPassword.setStyle("-fx-border-color: inherit");
        }
        if (passwordField_NewPassword.getText().equals("")) {
            tooltipError_NewPassword.setText("You surname is empty!");
            passwordField_NewPassword.setTooltip(tooltipError_NewPassword);
            passwordField_NewPassword.setStyle("-fx-border-color: red");
        } else {
            passwordField_NewPassword.setTooltip(tooltip_NewPassword);
            passwordField_NewPassword.setStyle("-fx-border-color: inherit");
        }
        if (passwordField_ConfirmPassword.getText().equals("")) {
            tooltipError_ConfirmPassword.setText("You login is empty!");
            passwordField_ConfirmPassword.setTooltip(tooltipError_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: red");
        } else {
            passwordField_ConfirmPassword.setTooltip(tooltip_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: inherit");
        }
        if (passwordField_CurrentPassword.getStyle().equals("-fx-border-color: inherit")
                && passwordField_NewPassword.getStyle().equals("-fx-border-color: inherit")
                && passwordField_ConfirmPassword.getStyle().equals("-fx-border-color: inherit")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkNames() {
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
        if (textField_Name.getStyle().equals("-fx-border-color: inherit")
                && textField_Surname.getStyle().equals("-fx-border-color: inherit")) {
            return true;
        } else {
            return false;
        }
    }

    public void saveName(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (checkNames()) {
            Doctor doctor = new Doctor(textField_Name.getText(), textField_Surname.getText());
            response = doctorController.changeName(Constant.getAuth(), doctor,
                    comboBox_Specialization.getSelectionModel().getSelectedItem().getId());
            statusCode = response.getStatusLine().getStatusCode();
            if (checkStatusCode(statusCode)) {
                alert.setContentText("Information changed!");
                alert.showAndWait();
                Constant.getMapByName("user").put("name", doctor.getName());
                Constant.getMapByName("user").put("surname", doctor.getSurname());
                Constant.getMapByName("user").put("specId", comboBox_Specialization.getSelectionModel().getSelectedItem().getId());
                Constant.getMapByName("user").put("specName", comboBox_Specialization.getSelectionModel().getSelectedItem().getName());
                Label label_Name = (Label)getStage().getScene().lookup("#label_Name");
                label_Name.setText(Constant.getMapByName("user").get("name").toString() + " "
                        + Constant.getMapByName("user").get("surname").toString());
                Label label_Specialization = (Label)getStage().getScene().lookup("#label_Specialization");
                label_Specialization.setText(Constant.getMapByName("user").get("specName").toString());
                getNewWindow().close();
            }
        }
    }
}
