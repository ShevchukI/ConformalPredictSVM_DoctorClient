package com.controllers.windows;

import com.controllers.requests.PatientController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.models.Patient;
import com.tools.Encryptor;
import com.tools.Placeholder;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
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
    AddPatientMenuController addPatientMenuController;

    @Autowired
    CardMenuController cardMenuController;

    @Autowired
    HttpResponse response;

    PatientController patientController = new PatientController();

    private Placeholder placeholder = new Placeholder();

    private WindowsController windowsController = new WindowsController();

//    @Autowired
//    Doctor doctor;

    private Encryptor encryptor = new Encryptor();

    private int statusCode;

    @FXML
    private MenuBarController menuBarController;

    private Stage stage;

    private List<Patient> patientList;

    @FXML
    private ObservableList<Patient> patientObservableList;

    @FXML
    private TableView tableView_PatientTable;

    @FXML
    private TableColumn tableColumn_Number;

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
    public void initialize(Doctor doctor) {
//        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
//        IMap<String, String> logmap = hz.getMap("login");

        this.doctor = doctor;
        menuBarController.init(this);
        label_Name.setText(doctor.getName());
        label_Specialization.setText(doctor.getSpecialization().getName());
    }

    public void initialize(Doctor doctor, Stage stage, HazelcastInstance hazelcastInstance) {
//        setDoctor(doctor);
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

//        try {
//            response = patientController.getAllPatient(encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()),
//                    encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()));
//            statusCode = response.getStatusLine().getStatusCode();
//            System.out.println(statusCode);
//            List<Patient> patients = new Patient().listFromJson(response);
//            ObservableList<Patient> list = FXCollections.observableArrayList(patients);
//            for (int i =0; i<list.size(); i++){
//                System.out.println(list.get(i).getId()+":"+list.get(i).getName()+":"+list.get(i).getSurname()+":"+list.get(i).getTelephone()+":"+list.get(i).getAddress()+":"+list.get(i).getEmail());
//            }
//        } catch (IOException e) {
//            System.out.println("ERROR!");
//        }

    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);

        menuBarController.init(this);
        label_Name.setText(getMap().get("name").toString());
        label_Specialization.setText(getMap().get("specName").toString());

        response = patientController.getAllPatient(encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()),
                encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()));

        patientList = new Patient().listFromJson(response);

        patientObservableList = FXCollections.observableList(patientList);

        tableColumn_Number.setCellFactory(col -> {
            TableCell<String, Integer> indexCell = new TableCell<>();
            ReadOnlyObjectProperty<TableRow> rowProperty = indexCell.tableRowProperty();
            ObjectBinding<String> rowBinding = Bindings.createObjectBinding(() -> {
                TableRow<String> row = rowProperty.get();
                if (row != null) {
                    int rowIndex = row.getIndex()+1;
                    if (rowIndex <=row.getTableView().getItems().size()) {
                        return Integer.toString(rowIndex);
                    }
                }
                return null;
            }, rowProperty);
            indexCell.textProperty().bind(rowBinding);
            return indexCell;
        });
        tableColumn_Name.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        tableColumn_Surname.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
        tableColumn_Address.setCellValueFactory(new PropertyValueFactory<Patient, String>("address"));
        tableColumn_Telephone.setCellValueFactory(new PropertyValueFactory<Patient, String>("telephone"));
        tableColumn_Email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));

        tableView_PatientTable.setItems(patientObservableList);

        label_Count.setText(String.valueOf(patientObservableList.size()));

    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }


//    public void setDoctor(Doctor doctor){
//        this.doctor = doctor;
//    }

    public Stage getStage() {
        return stage;
    }

    public void getPlaceholderAlert(ActionEvent event) {
        placeholder.getAlert();
    }

    public void addPatient(ActionEvent event) throws IOException {
//        System.out.println(doctor.getName());
//        System.out.println(doctor.getSurname());
//        System.out.println(doctor.getSpecialization().getName());
//        System.out.println(doctor.getLogin());
//        System.out.println(doctor.getPassword());

        windowsController.openWindow("addPatientMenu.fxml", getStage(), getInstance(), addPatientMenuController, "Add new patient", 408, 400);

    }

    public void viewPatient(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("cardMenu.fxml", getStage(), getInstance(), cardMenuController, "Card", 600, 640);
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


}
