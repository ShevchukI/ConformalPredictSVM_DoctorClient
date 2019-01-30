package com.controllers.windows;

import com.controllers.requests.PatientController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.models.Patient;
import com.tools.Encryptor;
import com.tools.Placeholder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 07.01.2019.
 */
public class MainMenuController extends MenuController {

    @Autowired
    RegistrationMenuController registrationMenuController;

    @Autowired
    AddPatientAndCardMenuController addPatientAndCardMenuController;

    @Autowired
    CardMenuController cardMenuController;

    @Autowired
    HttpResponse response;

    PatientController patientController = new PatientController();

    private Placeholder placeholder = new Placeholder();

    private WindowsController windowsController = new WindowsController();

    private Encryptor encryptor = new Encryptor();

    private int statusCode;

    @FXML
    private MenuBarController menuBarController;

    private Stage stage;

    private List<Patient> patientList;

    private MenuController menuController;

    @FXML
    private ObservableList<Patient> patientObservableList;

    @FXML
    private TableView<Patient> tableView_PatientTable;

    @FXML
    private TableColumn<Patient, Number> tableColumn_Number = new TableColumn<Patient, Number>("#");

    @FXML
    private TableColumn tableColumn_Name;

    @FXML
    private TableColumn tableColumn_Surname;

    @FXML
    private TableColumn tableColumn_Address;

    @FXML
    private TableColumn tableColumn_Telephone;

    @FXML
    private TableColumn tableColumn_Email;

    @FXML
    private Label label_Name;

    @FXML
    private Label label_Specialization;

    @FXML
    private Label label_Count;

    @FXML
    private Button button_View;

    @FXML
    private RadioButton radio_Name;

    @FXML
    private RadioButton radio_Surname;

    @FXML
    private RadioButton radio_All;

    @FXML
    private TextField textField_Search;

    private int searchType;

    private MainMenuController mainMenuController;

    @FXML
    public void initialize(Doctor doctor) {
        this.doctor = doctor;
        menuBarController.init(this);
        label_Name.setText(doctor.getName());
        label_Specialization.setText(doctor.getSpecialization().getName());
    }

    public void initialize(Doctor doctor, Stage stage, HazelcastInstance hazelcastInstance) {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);

        this.doctor = doctor;
        menuBarController.init(this);
        label_Name.setText(doctor.getName());
        if (doctor.getSpecialization() == null) {
            label_Specialization.setText("Empty!");
        } else {
            label_Specialization.setText(doctor.getSpecialization().getName());
        }
    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);

        menuBarController.init(this);
        label_Name.setText(getMap().get("surname").toString() + " " + getMap().get("name").toString());
        label_Specialization.setText(getMap().get("specName").toString());

//        response = patientController.getAllPatient(encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()),
//                encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()));

        response = patientController.findPatient(encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()),
                encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()),
                textField_Search.getText(), 0);

        patientList = new Patient().listFromJson(response);

        patientObservableList = FXCollections.observableList(patientList);

        tableColumn_Number.setSortable(false);
        tableColumn_Number.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(tableView_PatientTable.getItems().indexOf(column.getValue()) + 1));

        tableColumn_Name.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        tableColumn_Surname.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
        tableColumn_Address.setCellValueFactory(new PropertyValueFactory<Patient, String>("address"));
        tableColumn_Telephone.setCellValueFactory(new PropertyValueFactory<Patient, String>("telephone"));
        tableColumn_Email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));

        tableView_PatientTable.setItems(patientObservableList);

        label_Count.setText(String.valueOf(patientObservableList.size()));

        button_View.disableProperty().bind(Bindings.isEmpty(tableView_PatientTable.getSelectionModel().getSelectedItems()));

        tableView_PatientTable.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        viewPatient();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void getPlaceholderAlert(ActionEvent event) {
        placeholder.getAlert();
    }

    public void addPatient(ActionEvent event) throws IOException {

        windowsController.openNewModalWindow("addPatientAndRecordMenu.fxml", getStage(), getInstance(),
                addPatientAndCardMenuController, patientObservableList, tableView_PatientTable, "Add new patient", 740, 500);
    }

    public void viewPatient(ActionEvent event) throws IOException {
        if (tableView_PatientTable.getSelectionModel().getSelectedItem() == null) {
            System.out.println("ERROR!");
        } else {
            Patient patient = tableView_PatientTable.getSelectionModel().getSelectedItem();
            windowsController.openWindowResizable("cardMenu.fxml", getStage(), getInstance(), cardMenuController, patient, "Card", 600, 640);
        }
    }

    public void viewPatient() throws IOException {
        if (tableView_PatientTable.getSelectionModel().getSelectedItem() == null) {
            System.out.println("ERROR!");
        } else {
            Patient patient = tableView_PatientTable.getSelectionModel().getSelectedItem();
            windowsController.openWindowResizable("cardMenu.fxml", getStage(), getInstance(), cardMenuController, patient, "Card", 600, 640);
        }
    }

    public void getMap(ActionEvent event) {


        System.out.println(getMap().get("key"));
        System.out.println(getMap().get("vector"));
        System.out.println(getMap().get("login") + " :: " + encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()));
        System.out.println(getMap().get("password") + " :: " + encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()));
        System.out.println(getMap().size());
//        System.out.println("key: "+logmap.get("key"));
//        System.out.println("vector: "+logmap.get("vector"));
//
//        System.out.println(encryptor.decrypt(logmap.get("key"), logmap.get("vector"), logmap.get("login")));
    }


    public void search(ActionEvent event) throws IOException {
        if (radio_All.isSelected()) {
            searchType = 0;
        } else if (radio_Name.isSelected()) {
            searchType = 1;
        } else if (radio_Surname.isSelected()) {
            searchType = 2;
        }
        response = patientController.findPatient(encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()),
                encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()),
                textField_Search.getText(), searchType);
        statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            patientList = new Patient().listFromJson(response);
            patientObservableList = FXCollections.observableList(patientList);
            tableView_PatientTable.setItems(patientObservableList);
            label_Count.setText(String.valueOf(patientObservableList.size()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Oops! Status code: " + statusCode);
        }
    }

    public Label getLabel_Name() {
        return label_Name;
    }
}
