package com.controllers.windows.menu;

import com.controllers.requests.PatientController;
import com.controllers.windows.doctor.RegistrationMenuController;
import com.controllers.windows.patient.AddPatientAndCardMenuController;
import com.controllers.windows.patient.CardMenuController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Patient;
import com.models.PatientPage;
import com.tools.Encryptor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    private WindowsController windowsController = new WindowsController();
    private Encryptor encryptor = new Encryptor();
    private int statusCode;
    private Stage stage;
    private List<Patient> patientList;
    private MenuController menuController;
    private PatientPage patientPage;
    private int searchType;
    private MainMenuController mainMenuController;
    private int objectOnPage = 30;
    private int pageIndx;
    private boolean checkSearch = false;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private Pagination pagination_Patient;
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
        pageIndx = Integer.parseInt(getMap().get("pageIndex").toString());
        tableColumn_Number.setSortable(false);
        tableColumn_Number.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView_PatientTable.getItems().
                indexOf(column.getValue()) + 1) + (pageIndx - 1) * objectOnPage));
        tableColumn_Name.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        tableColumn_Name.setSortable(false);
        tableColumn_Surname.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
        tableColumn_Surname.setSortable(false);
        tableColumn_Address.setCellValueFactory(new PropertyValueFactory<Patient, String>("address"));
        tableColumn_Address.setSortable(false);
        tableColumn_Telephone.setCellValueFactory(new PropertyValueFactory<Patient, String>("telephone"));
        tableColumn_Telephone.setSortable(false);
        tableColumn_Email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));
        tableColumn_Email.setSortable(false);
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
        pagination_Patient.setPageFactory(this::createPage);
    }

    public void addPatient(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("patient/addPatientAndRecordMenu.fxml", getStage(), getInstance(),
                addPatientAndCardMenuController, patientObservableList, tableView_PatientTable,
                "Add new patient", 740, 500);
    }

    public void viewPatient(ActionEvent event) throws IOException {
        if (tableView_PatientTable.getSelectionModel().getSelectedItem() == null) {
            System.out.println("ERROR!");
        } else {
            Patient patient = tableView_PatientTable.getSelectionModel().getSelectedItem();
            getMap().put("pageIndex", String.valueOf(pageIndx));
            System.out.println(getMap().get("pageIndex"));
            windowsController.openWindowResizable("patient/cardMenu.fxml", getStage(), getInstance(), cardMenuController, patient,
                    "Card", 600, 640);
        }
    }

    public void viewPatient() throws IOException {
        if (tableView_PatientTable.getSelectionModel().getSelectedItem() == null) {
            System.out.println("ERROR!");
        } else {
            Patient patient = tableView_PatientTable.getSelectionModel().getSelectedItem();
            getMap().put("pageIndex", String.valueOf(pageIndx));
            windowsController.openWindowResizable("patient/cardMenu.fxml", getStage(), getInstance(), cardMenuController, patient,
                    "Card", 600, 640);
        }
    }

    public void search(ActionEvent event) throws IOException {
        if (textField_Search.getText().equals("")) {
            checkSearch = false;
            getMap().put("pageIndex", "1");
            pageIndx = Integer.parseInt(getMap().get("pageIndex").toString());
            getPage(pageIndx, objectOnPage);
        } else {
            if (radio_All.isSelected()) {
                searchType = 0;
                getMap().put("searchType", "0");
            } else if (radio_Name.isSelected()) {
                searchType = 1;
                getMap().put("searchType", "1");
            } else if (radio_Surname.isSelected()) {
                searchType = 2;
                getMap().put("searchType", "2");
            }
            checkSearch = true;
            getMap().put("pageIndex", "1");
            pageIndx = Integer.parseInt(getMap().get("pageIndex").toString());
            searchPage(pageIndx, objectOnPage, textField_Search.getText(), Integer.parseInt(getMap().get("searchType").toString()));
        }
    }


    private Node createPage(int pageIndex) {
        if (!checkSearch) {
            try {
                pageIndx = pageIndex + 1;
                getPage(pageIndx, objectOnPage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                pageIndx = pageIndex + 1;
                searchPage(pageIndx, objectOnPage, textField_Search.getText(), Integer.parseInt(getMap().get("searchType").toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tableView_PatientTable;
    }

    public void getPage(int pageIndx, int objectOnPage) throws IOException {
        response = patientController.getPatientPage(encryptor.decrypt(getMap().get("key").toString(),
                getMap().get("vector").toString(), getMap().get("login").toString()),
                encryptor.decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("password").toString()),
                pageIndx, objectOnPage);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            patientPage = new PatientPage().fromJson(response);
            patientObservableList = FXCollections.observableList(patientPage.getPatientEntities());
        }
        pagination_Patient.setPageCount(patientPage.getNumberOfPages());
        pagination_Patient.setCurrentPageIndex(pageIndx - 1);
        tableView_PatientTable.setItems(patientObservableList);
        label_Count.setText(String.valueOf(patientObservableList.size()));
    }

    public void searchPage(int pageIndx, int objectOnPage, String search, int searchType) throws IOException {
        response = patientController.findPatientPage(encryptor.decrypt(getMap().get("key").toString(),
                getMap().get("vector").toString(), getMap().get("login").toString()),
                encryptor.decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("password").toString()),
                search, searchType, pageIndx, objectOnPage);
        statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            patientPage = new PatientPage().fromJson(response);
            patientObservableList = FXCollections.observableList(patientPage.getPatientEntities());
            tableView_PatientTable.setItems(patientObservableList);
            pagination_Patient.setPageCount(patientPage.getNumberOfPages());
            pagination_Patient.setCurrentPageIndex(pageIndx - 1);
            label_Count.setText(String.valueOf(patientObservableList.size()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Oops! Status code: " + statusCode);
        }
    }
}
