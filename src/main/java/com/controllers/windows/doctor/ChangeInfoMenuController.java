package com.controllers.windows.doctor;

import com.controllers.requests.DoctorController;
import com.controllers.requests.SpecializationController;
import com.controllers.windows.menu.MenuController;
import com.models.Doctor;
import com.models.Specialization;
import com.tools.Constant;
import com.tools.HazelCastMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpResponse;

import java.io.IOException;


/**
 * Created by Admin on 29.01.2019.
 */
public class ChangeInfoMenuController extends MenuController {

    private SpecializationController specializationController;
    private ObservableList<Specialization> specializations;
    private Tooltip tooltipError_CurrentPassword;
    private Tooltip tooltipError_NewPassword;
    private Tooltip tooltipError_ConfirmPassword;
    private Tooltip tooltipError_Name;
    private Tooltip tooltipError_Surname;

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
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        setNewWindow(newWindow);
        specializationController = new SpecializationController();
        specializations = FXCollections.observableArrayList();
        tooltipError_CurrentPassword = new Tooltip();
        tooltipError_NewPassword = new Tooltip();
        tooltipError_ConfirmPassword = new Tooltip();
        tooltipError_Name = new Tooltip();
        tooltipError_Surname = new Tooltip();
        button_Ok.setGraphic(new ImageView(Constant.getOkIcon()));
        button_Cancel.setGraphic(new ImageView(Constant.getCancelIcon()));
        if (change) {
            specializations.add(new Specialization(-1, "None"));
            HttpResponse response = specializationController.getAllSpecialization();
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
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
            comboBox_Specialization.getSelectionModel().select(HazelCastMap.getDoctorMap().get(1).getSpecialization().getId());
            textField_Name.setText(HazelCastMap.getDoctorMap().get(1).getName());
            textField_Surname.setText(HazelCastMap.getDoctorMap().get(1).getSurname());

        }
    }

    public void savePassword(ActionEvent event) throws IOException {

        if (checkPasswords()) {
            if (passwordField_CurrentPassword.getText().equals(Constant.getAuth()[1])) {
                if (passwordField_NewPassword.getText().equals(passwordField_ConfirmPassword.getText())) {
                    HttpResponse response = DoctorController.changePassword(passwordField_ConfirmPassword.getText());
                    setStatusCode(response.getStatusLine().getStatusCode());
                    if (checkStatusCode(getStatusCode())) {
                        HazelCastMap.changePassword(passwordField_ConfirmPassword.getText().toString());
                        getAlert(null, "Password changed!", Alert.AlertType.INFORMATION);
                        getNewWindow().close();
                    }
                }
            } else {
                getAlert(null, "Error! Current password incorrect, please try again.", Alert.AlertType.ERROR);
            }
        }
    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }

    public boolean checkPasswords() {
        if (passwordField_CurrentPassword.getText().equals("")) {
            tooltipError_CurrentPassword.setText("Current password is empty!");
            passwordField_CurrentPassword.setTooltip(tooltipError_CurrentPassword);
            passwordField_CurrentPassword.setStyle(Constant.getBorderColorRed());
        } else {
            passwordField_CurrentPassword.setTooltip(tooltip_CurrentPassword);
            passwordField_CurrentPassword.setStyle(Constant.getBorderColorInherit());
        }
        if (passwordField_NewPassword.getText().equals("")) {
            tooltipError_NewPassword.setText("New password is empty!");
            passwordField_NewPassword.setTooltip(tooltipError_NewPassword);
            passwordField_NewPassword.setStyle(Constant.getBorderColorRed());
        } else {
            passwordField_NewPassword.setTooltip(tooltip_NewPassword);
            passwordField_NewPassword.setStyle(Constant.getBorderColorInherit());
        }
        if (passwordField_ConfirmPassword.getText().equals("")) {
            tooltipError_ConfirmPassword.setText("Confirm password is empty!");
            passwordField_ConfirmPassword.setTooltip(tooltipError_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle(Constant.getBorderColorRed());
        } else {
            passwordField_ConfirmPassword.setTooltip(tooltip_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle(Constant.getBorderColorInherit());
        }
        if (passwordField_CurrentPassword.getStyle().equals(Constant.getBorderColorInherit())
                && passwordField_NewPassword.getStyle().equals(Constant.getBorderColorInherit())
                && passwordField_ConfirmPassword.getStyle().equals(Constant.getBorderColorInherit())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkNames() {
        if (textField_Name.getText().equals("")) {
            tooltipError_Name.setText("You name is empty!");
            textField_Name.setTooltip(tooltipError_Name);
            textField_Name.setStyle(Constant.getBorderColorRed());
        } else {
            textField_Name.setTooltip(tooltip_Name);
            textField_Name.setStyle(Constant.getBorderColorInherit());
        }
        if (textField_Surname.getText().equals("")) {
            tooltipError_Surname.setText("You surname is empty!");
            textField_Surname.setTooltip(tooltipError_Surname);
            textField_Surname.setStyle(Constant.getBorderColorRed());
        } else {
            textField_Surname.setTooltip(tooltip_Surname);
            textField_Surname.setStyle(Constant.getBorderColorInherit());
        }
        if (textField_Name.getStyle().equals(Constant.getBorderColorInherit())
                && textField_Surname.getStyle().equals(Constant.getBorderColorInherit())) {
            return true;
        } else {
            return false;
        }
    }

    public void saveName(ActionEvent event) throws IOException {
        if (checkNames()) {
            Doctor doctor = new Doctor(textField_Name.getText(), textField_Surname.getText());
            HttpResponse response = DoctorController.changeName(doctor, comboBox_Specialization.getSelectionModel().getSelectedItem().getId());
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                doctor.setSpecialization(comboBox_Specialization.getSelectionModel().getSelectedItem());
                HazelCastMap.changeUserInformation(doctor);
                getAlert(null, "Information changed!", Alert.AlertType.INFORMATION);
                Label label_Name = (Label) getStage().getScene().lookup("#label_Name");
                label_Name.setText(doctor.getSurname() + " " + doctor.getName());
                Label label_Specialization = (Label) getStage().getScene().lookup("#label_Specialization");
                label_Specialization.setText(doctor.getSpecialization().getName());
                getNewWindow().close();
            }
        }
    }
}
