package com.controllers.windows.patient;

import com.controllers.requests.PatientController;
import com.controllers.requests.RecordController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Patient;
import com.models.Record;
import com.tools.Constant;
import com.tools.HazelCastMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Admin on 14.01.2019.
 */
public class AddPatientAndCardMenuController extends MenuController {

    private MainMenuController mainMenuController;
    private WindowsController windowsController;
    private PatientController patientController;
    private RecordController recordController;
    private TableView<Patient> tableView_PatientTable;

    @FXML
    private PatientMenuController patientMenuController;
    @FXML
    private RecordMenuController recordMenuController;
    @FXML
    private Button button_Save;
    @FXML
    private Button button_Cancel;


    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");


    public void initialize(Stage stage, Stage newWindow, TableView<Patient> tableView_PatientTable) throws IOException {
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        setNewWindow(newWindow);
        patientMenuController.init(this);
        recordMenuController.init(this);
        mainMenuController = new MainMenuController();
        windowsController = new WindowsController();
        patientController = new PatientController();
        recordController = new RecordController();
        this.tableView_PatientTable = tableView_PatientTable;
    }



    public void savePatient(ActionEvent event) throws IOException, ParseException {
        if (checkPatientFields() && checkCardFields()) {
            Patient patient = new Patient(patientMenuController.getTextField_Name().getText(),
                    patientMenuController.getTextField_Surname().getText(), patientMenuController.getTextField_Telephone().getText(),
                    patientMenuController.getTextField_Address().getText(), patientMenuController.getTextField_Email().getText());
            Record record = new Record(Double.parseDouble(recordMenuController.getTextField_Weight().getText()),
                    Double.parseDouble(recordMenuController.getTextField_Height().getText()),
                    recordMenuController.getChoiceBox_BloodGroup().getSelectionModel().getSelectedItem()
                            + recordMenuController.getChoiceBox_BloodType().getSelectionModel().getSelectedItem(),
                    formatter1.parse(recordMenuController.getDatePicker_Birthday().getValue().toString()));
            if (recordMenuController.getChoiceBox_Sex().getSelectionModel().getSelectedItem().equals("Male")) {
                record.setSex(true);
            } else {
                record.setSex(false);
            }
            HttpResponse response = patientController.createPatient(patient);
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                int id = new Patient().getIdFromJson(response);
                patient.setId(id);
                response = recordController.changeRecord(record, id);
                setStatusCode(response.getStatusLine().getStatusCode());
                if (checkStatusCode(getStatusCode())) {
                    getAlert(null, "Patient is registered!", Alert.AlertType.INFORMATION);
                    if (tableView_PatientTable.getItems().size() < Constant.getObjectOnPage()) {
                        tableView_PatientTable.getItems().add(patient);
                    }
                    tableView_PatientTable.refresh();
                    getNewWindow().close();
                }
            }
        }
    }

    public void cancel(ActionEvent event) {
        boolean result = questionOkCancel("Do you really want to leave?");
        if (result) {
            getNewWindow().close();
        }
    }

    public boolean checkPatientFields() {
        if (patientMenuController.getTextField_Name().getText().equals("")) {
            patientMenuController.getTooltipError_Name().setText("You name is empty!");
            patientMenuController.getTextField_Name().setTooltip(patientMenuController.getTooltipError_Name());
            patientMenuController.getTextField_Name().setStyle("-fx-border-color: red");
        } else {
            patientMenuController.getTextField_Name().setTooltip(patientMenuController.getTooltip_Name());
            patientMenuController.getTextField_Name().setStyle("-fx-border-color: inherit");
        }
        if (patientMenuController.getTextField_Surname().getText().equals("")) {
            patientMenuController.getTooltipError_Surname().setText("You surname is empty!");
            patientMenuController.getTextField_Surname().setTooltip(patientMenuController.getTooltipError_Surname());
            patientMenuController.getTextField_Surname().setStyle("-fx-border-color: red");
        } else {
            patientMenuController.getTextField_Surname().setTooltip(patientMenuController.getTooltip_Surname());
            patientMenuController.getTextField_Surname().setStyle("-fx-border-color: inherit");
        }
        if (patientMenuController.getTextField_Telephone().getText().equals("")) {
            patientMenuController.getTooltipError_Telephone().setText("You telephone is empty!");
            patientMenuController.getTextField_Telephone().setTooltip(patientMenuController.getTooltipError_Telephone());
            patientMenuController.getTextField_Telephone().setStyle("-fx-border-color: red");
        } else if (!patientMenuController.getTextField_Telephone().getText().matches(Constant.getPHONEREG())) {
            patientMenuController.getTooltipError_Telephone().setText("Your telephone must have the  format: +380XXXXXXXXX");
            patientMenuController.getTextField_Telephone().setTooltip(patientMenuController.getTooltipError_Telephone());
            patientMenuController.getTextField_Telephone().setStyle("-fx-border-color: red");
        } else {
            patientMenuController.getTextField_Telephone().setTooltip(patientMenuController.getTooltip_Telephone());
            patientMenuController.getTextField_Telephone().setStyle("-fx-border-color: inherit");
        }
        if (patientMenuController.getTextField_Address().getText().equals("")) {
            patientMenuController.getTooltipError_Address().setText("You address is empty!");
            patientMenuController.getTextField_Address().setTooltip(patientMenuController.getTooltipError_Address());
            patientMenuController.getTextField_Address().setStyle("-fx-border-color: red");
        } else {
            patientMenuController.getTextField_Address().setTooltip(patientMenuController.getTooltip_Address());
            patientMenuController.getTextField_Address().setStyle("-fx-border-color: inherit");
        }
        if (patientMenuController.getTextField_Email().getText().equals("")) {
            patientMenuController.getTooltipError_Email().setText("You email is empty!");
            patientMenuController.getTextField_Email().setTooltip(patientMenuController.getTooltipError_Email());
            patientMenuController.getTextField_Email().setStyle("-fx-border-color: red");
        } else if (!patientMenuController.getTextField_Email().getText().matches(Constant.getEMAILREG())) {
            patientMenuController.getTooltipError_Email().setText("Your email must have the  format: example@example.com");
            patientMenuController.getTextField_Email().setTooltip(patientMenuController.getTooltipError_Email());
            patientMenuController.getTextField_Email().setStyle("-fx-border-color: red");
        } else {
            patientMenuController.getTextField_Email().setTooltip(patientMenuController.getTooltip_Email());
            patientMenuController.getTextField_Email().setStyle("-fx-border-color: inherit");
        }
        if (patientMenuController.getTextField_Name().getStyle().equals("-fx-border-color: inherit")
                && patientMenuController.getTextField_Surname().getStyle().equals("-fx-border-color: inherit")
                && patientMenuController.getTextField_Telephone().getStyle().equals("-fx-border-color: inherit")
                && patientMenuController.getTextField_Address().getStyle().equals("-fx-border-color: inherit")
                && patientMenuController.getTextField_Email().getStyle().equals("-fx-border-color: inherit")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkCardFields() {
        if (recordMenuController.getDatePicker_Birthday().getValue() == null) {
            recordMenuController.getTooltipError_Birthday().setText("Patient birthday is empty!");
            recordMenuController.getDatePicker_Birthday().setTooltip(recordMenuController.getTooltipError_Birthday());
            recordMenuController.getDatePicker_Birthday().setStyle("-fx-border-color: red");
        } else {
            recordMenuController.getDatePicker_Birthday().setTooltip(patientMenuController.getTooltip_Name());
            recordMenuController.getDatePicker_Birthday().setStyle("-fx-border-color: inherit");
        }
        if (recordMenuController.getChoiceBox_Sex().getSelectionModel().getSelectedItem() == null) {
            recordMenuController.getTooltipError_Sex().setText("Patient sex is empty!");
            recordMenuController.getChoiceBox_Sex().setTooltip(recordMenuController.getTooltipError_Sex());
            recordMenuController.getChoiceBox_Sex().setStyle("-fx-border-color: red");
        } else {
            recordMenuController.getChoiceBox_Sex().setTooltip(recordMenuController.getTooltip_Sex());
            recordMenuController.getChoiceBox_Sex().setStyle("-fx-border-color: inherit");
        }
        if (recordMenuController.getChoiceBox_BloodGroup().getSelectionModel().getSelectedItem() == null) {
            recordMenuController.getTooltipError_BloodGroup().setText("Patient blood group is empty!");
            recordMenuController.getChoiceBox_BloodGroup().setTooltip(recordMenuController.getTooltipError_BloodGroup());
            recordMenuController.getChoiceBox_BloodGroup().setStyle("-fx-border-color: red");
        } else {
            recordMenuController.getChoiceBox_BloodGroup().setTooltip(recordMenuController.getTooltip_BloodGroup());
            recordMenuController.getChoiceBox_BloodGroup().setStyle("-fx-border-color: inherit");
        }
        if (recordMenuController.getChoiceBox_BloodType().getSelectionModel().getSelectedItem() == null) {
            recordMenuController.getTooltipError_BloodType().setText("Patient blood group type is empty!");
            recordMenuController.getChoiceBox_BloodType().setTooltip(recordMenuController.getTooltipError_BloodType());
            recordMenuController.getChoiceBox_BloodType().setStyle("-fx-border-color: red");
        } else {
            recordMenuController.getChoiceBox_BloodType().setTooltip(recordMenuController.getTooltip_BloodType());
            recordMenuController.getChoiceBox_BloodType().setStyle("-fx-border-color: inherit");
        }
        if (recordMenuController.getTextField_Weight().getText().equals("")) {
            recordMenuController.getTooltipError_Weight().setText("Patient weight is empty!");
            recordMenuController.getTextField_Weight().setTooltip(recordMenuController.getTooltipError_Weight());
            recordMenuController.getTextField_Weight().setStyle("-fx-border-color: red");
        } else if (!recordMenuController.getTextField_Weight().getText().matches(Constant.getWHEIGHT())) {
            recordMenuController.getTooltipError_Weight().setText("Weight must have the  format: xx,xx");
            recordMenuController.getTextField_Weight().setTooltip(recordMenuController.getTooltipError_Weight());
            recordMenuController.getTextField_Weight().setStyle("-fx-border-color: red");
        } else {
            recordMenuController.getTextField_Weight().setTooltip(recordMenuController.getTooltip_Weight());
            recordMenuController.getTextField_Weight().setStyle("-fx-border-color: inherit");
        }
        if (recordMenuController.getTextField_Height().getText().equals("")) {
            recordMenuController.getTooltipError_Height().setText("Patient height is empty!");
            recordMenuController.getTextField_Height().setTooltip(recordMenuController.getTooltipError_Height());
            recordMenuController.getTextField_Height().setStyle("-fx-border-color: red");
        } else if (!recordMenuController.getTextField_Height().getText().matches(Constant.getWHEIGHT())) {
            recordMenuController.getTooltipError_Height().setText("Height must have the  format: xx,xx");
            recordMenuController.getTextField_Height().setTooltip(recordMenuController.getTooltipError_Height());
            recordMenuController.getTextField_Height().setStyle("-fx-border-color: red");
        } else {
            recordMenuController.getTextField_Height().setTooltip(recordMenuController.getTooltip_Height());
            recordMenuController.getTextField_Height().setStyle("-fx-border-color: inherit");
        }
        if (recordMenuController.getDatePicker_Birthday().getStyle().equals("-fx-border-color: inherit")
                && recordMenuController.getChoiceBox_Sex().getStyle().equals("-fx-border-color: inherit")
                && recordMenuController.getChoiceBox_BloodGroup().getStyle().equals("-fx-border-color: inherit")
                && recordMenuController.getChoiceBox_BloodType().getStyle().equals("-fx-border-color: inherit")
                && recordMenuController.getTextField_Weight().getStyle().equals("-fx-border-color: inherit")
                && recordMenuController.getTextField_Height().getStyle().equals("-fx-border-color: inherit")) {
            return true;
        } else {
            return false;
        }
    }
}
