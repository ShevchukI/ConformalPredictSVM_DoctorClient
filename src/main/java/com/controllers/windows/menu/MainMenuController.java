package com.controllers.windows.menu;

import com.controllers.requests.PatientController;
import com.controllers.windows.doctor.RegistrationMenuController;
import com.controllers.windows.patient.AddPatientAndCardMenuController;
import com.controllers.windows.patient.CardMenuController;
import com.models.Patient;
import com.models.PatientPage;
import com.tools.Constant;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
//    @Autowired
//    AddPatientAndCardMenuController addPatientAndCardMenuController;
    @Autowired
CardMenuController cardMenuController;
    @Autowired
    HttpResponse response;

    private AddPatientAndCardMenuController addPatientAndCardMenuController;
    private ObservableList<Patient> patientObservableList;
    private PatientController patientController;
    private WindowsController windowsController;
    private int statusCode;
    private Stage stage;
    private List<Patient> patientList;
    private MenuController menuController;
    private PatientPage patientPage;
    private int searchType;
    private MainMenuController mainMenuController;
    private int objectOnPage;
    private int pageIndex;
    private boolean checkSearch;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private Pagination pagination_Patient;


    @FXML
    private TableView<Patient> tableView_PatientTable;
    @FXML
    private TableColumn<Patient, Number> tableColumn_Number;
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
    @FXML
    private Button button_Add;
    @FXML
    private Button button_Search;

    public void initialize(Stage stage) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        menuBarController.init(this);
        patientObservableList = FXCollections.observableArrayList();
        addPatientAndCardMenuController = new AddPatientAndCardMenuController();
        patientController = new PatientController();
        windowsController = new WindowsController();
        tableColumn_Number = new TableColumn<Patient, Number>("#");
        objectOnPage = 30;
        checkSearch = false;
        label_Name.setText(Constant.getMapByName(Constant.getUserMapName()).get("surname").toString() + " "
                + Constant.getMapByName(Constant.getUserMapName()).get("name").toString());
        label_Specialization.setText(Constant.getMapByName(Constant.getUserMapName()).get("specName").toString());
        this.pageIndex = Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("pageIndex").toString());
        tableColumn_Number.setSortable(false);
        tableColumn_Number.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView_PatientTable.getItems().
                indexOf(column.getValue()) + 1) + (pageIndex - 1) * objectOnPage));
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
        button_Add.setGraphic(new ImageView("/img/icons/add.png"));
        button_View.setGraphic(new ImageView("/img/icons/info.png"));
        button_Search.setGraphic(new ImageView("/img/icons/search.png"));
    }

    public void addPatient(ActionEvent event) throws IOException {
//        windowsController.openNewModalWindow("patient/addPatientAndRecordMenu", getStage(),
//                addPatientAndCardMenuController,
//                "Add new patient", 740, 540);
        windowsController.openNewModalWindow("patient/addPatientAndRecordMenu", this.getStage(),
                addPatientAndCardMenuController, patientObservableList, tableView_PatientTable,
                "Add new patient", 740, 540);
    }

    public void viewPatient(ActionEvent event) throws IOException {
        viewPatient();
    }

    public void viewPatient() throws IOException {
        if (tableView_PatientTable.getSelectionModel().getSelectedItem() == null) {
            System.out.println("ERROR!");
        } else {
            Patient patient = tableView_PatientTable.getSelectionModel().getSelectedItem();
            Constant.getMapByName(Constant.getMiscellaneousMapName()).put("pageIndex", String.valueOf(this.pageIndex));
            Constant.getMapByName(Constant.getPatientMapName()).put("name", patient.getName());
            Constant.getMapByName(Constant.getPatientMapName()).put("id", patient.getId());
            Constant.getMapByName(Constant.getPatientMapName()).put("surname", patient.getSurname());
            windowsController.openWindowResizable("patient/cardMenu", getStage(), cardMenuController,
                    "Card", 600, 680);
        }
    }

    public void search(ActionEvent event) throws IOException {
        if (textField_Search.getText().equals("")) {
            checkSearch = false;
            Constant.getMapByName(Constant.getMiscellaneousMapName()).put("pageIndex", "1");
            pageIndex = Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("pageIndex").toString());
            getPage(this.pageIndex, objectOnPage);
        } else {
            if (radio_All.isSelected()) {
                searchType = 0;
                Constant.getMapByName(Constant.getMiscellaneousMapName()).put("searchType", "0");
            } else if (radio_Name.isSelected()) {
                searchType = 1;
                Constant.getMapByName(Constant.getMiscellaneousMapName()).put("searchType", "1");
            } else if (radio_Surname.isSelected()) {
                searchType = 2;
                Constant.getMapByName(Constant.getMiscellaneousMapName()).put("searchType", "2");
            }
            checkSearch = true;
            Constant.getMapByName(Constant.getMiscellaneousMapName()).put("pageIndex", "1");
            pageIndex = Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("pageIndex").toString());
            searchPage(this.pageIndex, objectOnPage, textField_Search.getText(),
                    Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("searchType").toString()));
        }
    }


    private Node createPage(int pageIndex) {
        if (!checkSearch) {
            try {
                this.pageIndex = pageIndex + 1;
                getPage(this.pageIndex, objectOnPage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.pageIndex = pageIndex + 1;
                searchPage(this.pageIndex, objectOnPage, textField_Search.getText(), Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("searchType").toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tableView_PatientTable;
    }

    public void getPage(int pageIndex, int objectOnPage) throws IOException {
        response = patientController.getPatientPage(Constant.getAuth(),
                pageIndex, objectOnPage);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            patientPage = new PatientPage().fromJson(response);
            patientObservableList = FXCollections.observableList(patientPage.getPatientEntities());
        }
//        pagination_Patient.setPageCount(patientPage.getNumberOfPages());
//        pagination_Patient.setCurrentPageIndex(pageIndex - 1);
        tableView_PatientTable.setItems(patientObservableList);
        label_Count.setText(String.valueOf(patientObservableList.size()));
        if (patientObservableList.isEmpty()) {
            pagination_Patient.setPageCount(1);
            pagination_Patient.setCurrentPageIndex(1);
        } else {
            pagination_Patient.setPageCount(patientPage.getNumberOfPages());
            pagination_Patient.setCurrentPageIndex(pageIndex - 1);
        }
    }

    public void searchPage(int pageIndex, int objectOnPage, String search, int searchType) throws IOException {
        response = patientController.findPatientPage(Constant.getAuth(),
                search, searchType, pageIndex, objectOnPage);
        statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            patientPage = new PatientPage().fromJson(response);
            patientObservableList = FXCollections.observableList(patientPage.getPatientEntities());
            tableView_PatientTable.setItems(patientObservableList);
            if (patientObservableList.isEmpty()) {
                pagination_Patient.setPageCount(1);
                pagination_Patient.setCurrentPageIndex(1);
            } else {
                pagination_Patient.setPageCount(patientPage.getNumberOfPages());
                pagination_Patient.setCurrentPageIndex(pageIndex - 1);
            }
            label_Count.setText(String.valueOf(patientObservableList.size()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Oops! Status code: " + statusCode);
            alert.showAndWait();
        }
    }

}
