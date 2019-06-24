package com.controllers.windows.menu;

import com.controllers.windows.patient.AddPatientAndCardMenuController;
import com.controllers.windows.patient.CardMenuController;
import com.models.Doctor;
import com.models.Patient;
import com.models.PatientPage;
import com.tools.Constant;
import com.tools.GlobalMap;
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

import java.io.IOException;

/**
 * Created by Admin on 07.01.2019.
 */
public class MainMenuController extends MenuController {


    private AddPatientAndCardMenuController addPatientAndCardMenuController;
    private ObservableList<Patient> patientObservableList;
    private WindowsController windowsController;
    private PatientPage patientPage;
//    private int searchType;
    private int pageIndex;
    private boolean checkSearch;
    private Doctor doctor;

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
//        stage.setOnHidden(event -> {
//            HazelCastMap.getInstance().getLifecycleService().shutdown();
//        });
        setStage(stage);
        menuBarController.init(this);
        doctor = GlobalMap.getDoctorMap().get(1);
//        doctor = HazelCastMap.getDoctorMap().get(1);
        patientObservableList = FXCollections.observableArrayList();
        addPatientAndCardMenuController = new AddPatientAndCardMenuController();
        windowsController = new WindowsController();
        label_Name.setText(doctor.getSurname() + " " + doctor.getName());
        label_Specialization.setText(doctor.getSpecialization().getName());
        checkSearch = false;
        pageIndex = Integer.parseInt(GlobalMap.getMiscMap().get(Constant.getPageIndex()));
//        pageIndex = HazelCastMap.getMiscellaneousMap().get("pageIndex");

        tableColumn_Number.impl_setReorderable(false);
        tableColumn_Number.setSortable(false);
        tableColumn_Number.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView_PatientTable.getItems().
                indexOf(column.getValue()) + 1) + (pageIndex - 1) * Constant.getObjectOnPage()));
        tableColumn_Name.impl_setReorderable(false);
        tableColumn_Name.setSortable(false);
        tableColumn_Name.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        tableColumn_Surname.impl_setReorderable(false);
        tableColumn_Surname.setSortable(false);
        tableColumn_Surname.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
        tableColumn_Address.impl_setReorderable(false);
        tableColumn_Address.setSortable(false);
        tableColumn_Address.setCellValueFactory(new PropertyValueFactory<Patient, String>("address"));
        tableColumn_Telephone.impl_setReorderable(false);
        tableColumn_Telephone.setSortable(false);
        tableColumn_Telephone.setCellValueFactory(new PropertyValueFactory<Patient, String>("telephone"));
        tableColumn_Email.impl_setReorderable(false);
        tableColumn_Email.setSortable(false);
        tableColumn_Email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));
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
        button_Add.setGraphic(new ImageView(Constant.getAddIcon()));
        button_View.setGraphic(new ImageView(Constant.getInfoIcon()));
        button_Search.setGraphic(new ImageView(Constant.getSearchIcon()));
    }

    public void addPatient(ActionEvent event) throws IOException {

        windowsController.openNewModalWindow(Constant.getAddPatientAndRecordMenuRoot(), this.getStage(),
                addPatientAndCardMenuController, tableView_PatientTable,"Add new patient", 740, 540);
    }

    public void viewPatient(ActionEvent event) throws IOException {
        viewPatient();
    }

    public void viewPatient() throws IOException {
        if (tableView_PatientTable.getSelectionModel().getSelectedItem() == null) {
            getAlert(null, "Please, choose patient!", Alert.AlertType.ERROR);
        } else {
            Patient patient = tableView_PatientTable.getSelectionModel().getSelectedItem();
            GlobalMap.getMiscMap().put("pageIndex", String.valueOf(pageIndex));
//            HazelCastMap.getMiscellaneousMap().put("pageIndex", pageIndex);

            GlobalMap.getPatientMap().put(1, patient);
//            HazelCastMap.getPatientMap().put(1, patient);

            windowsController.openWindow(Constant.getCardMenuRoot(), getStage(),
                    new CardMenuController(), null, true, 600, 680);
        }
    }

    public void search(ActionEvent event) throws IOException {
        if (textField_Search.getText().equals("")) {
            checkSearch = false;
            GlobalMap.getMiscMap().put("pageIndex", "1");
//            HazelCastMap.getMiscellaneousMap().put("pageIndex", 1);
            pageIndex = Integer.parseInt(GlobalMap.getMiscMap().get(Constant.getPageIndex()));
//            pageIndex = HazelCastMap.getMiscellaneousMap().get("pageIndex");
            getPage(pageIndex);
        } else {
            if (radio_All.isSelected()) {
//                searchType = 0;
                GlobalMap.getMiscMap().put("searchType", "0");
//                HazelCastMap.getMiscellaneousMap().put("searchType", 0);
            } else if (radio_Name.isSelected()) {
//                searchType = 1;
                GlobalMap.getMiscMap().put(Constant.getSearchType(), "1");
//                HazelCastMap.getMiscellaneousMap().put("searchType", 1);
            } else if (radio_Surname.isSelected()) {
//                searchType = 2;
                GlobalMap.getMiscMap().put(Constant.getSearchType(), "2");
//                HazelCastMap.getMiscellaneousMap().put("searchType", 2);
            }
            checkSearch = true;
            GlobalMap.getMiscMap().put(Constant.getPageIndex(), "1");
//            HazelCastMap.getMiscellaneousMap().put("pageIndex", 1);
            pageIndex = Integer.parseInt(GlobalMap.getMiscMap().get(Constant.getPageIndex()));
//            pageIndex = HazelCastMap.getMiscellaneousMap().get("pageIndex");
            searchPage(pageIndex, textField_Search.getText(),
                    Integer.parseInt(GlobalMap.getMiscMap().get(Constant.getSearchType())));
//                    HazelCastMap.getMiscellaneousMap().get("searchType"));
        }
    }

    private Node createPage(int pageIndex) {
        if (!checkSearch) {
            try {
                this.pageIndex = pageIndex + 1;
                getPage(this.pageIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.pageIndex = pageIndex + 1;
                searchPage(this.pageIndex, textField_Search.getText(),
                        Integer.parseInt(GlobalMap.getMiscMap().get(Constant.getSearchType())));
//                        HazelCastMap.getMiscellaneousMap().get("searchType"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tableView_PatientTable;
    }

    public void getPage(int pageIndex) throws IOException {
        HttpResponse response = Patient.getPatientPage(pageIndex);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            patientPage = PatientPage.fromJson(response);
            patientObservableList = FXCollections.observableList(patientPage.getPatientEntities());
        }

        tableView_PatientTable.setItems(patientObservableList);
        paginationSetPage();
    }

    public void searchPage(int pageIndex, String search, int searchType) throws IOException {
        HttpResponse response = Patient.findPatientPage(search, searchType, pageIndex);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (getStatusCode() == 200) {
            patientPage = new PatientPage().fromJson(response);
            patientObservableList = FXCollections.observableList(patientPage.getPatientEntities());
            tableView_PatientTable.setItems(patientObservableList);
            paginationSetPage();
        } else {
            getAlert(null, "Oops! Status code: " + getStatusCode(), Alert.AlertType.ERROR);
        }
    }

    private void paginationSetPage(){
        if (patientObservableList.isEmpty()) {
            pagination_Patient.setPageCount(1);
            pagination_Patient.setCurrentPageIndex(1);
        } else {
            pagination_Patient.setPageCount(patientPage.getNumberOfPages());
            pagination_Patient.setCurrentPageIndex(pageIndex - 1);
        }
    }

}
