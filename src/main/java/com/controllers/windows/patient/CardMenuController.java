package com.controllers.windows.patient;

import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Page;
import com.models.Patient;
import com.models.Record;
import com.tools.Constant;
import com.tools.GlobalMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Admin on 14.01.2019.
 */
public class CardMenuController extends MenuController {

    private AddPatientAndCardMenuController addPatientAndCardMenuController;
    private WindowsController windowsController;
    private ArrayList<Page> pages;
    private Patient patient;
    private Record record;
    private Page page;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private Label label_Name;
    @FXML
    private Label label_BirthDate;
    @FXML
    private Label label_BloodGroup;
    @FXML
    private Label label_Sex;
    @FXML
    private Label label_Weight;
    @FXML
    private Label label_Height;
    @FXML
    private Button button_New;
    @FXML
    private Button button_View;
    @FXML
    private Button button_Delete;
    @FXML
    private Button button_ChangePatientInfo;
    @FXML
    private Button button_Back;
    @FXML
    private TableColumn<Page, Number> tableColumn_Number;
    @FXML
    private TableView<Page> tableView_PageTable;
    @FXML
    private TableColumn tableColumn_Date;
    @FXML
    private TableColumn tableColumn_DoctorName;
    @FXML
    private TableColumn tableColumn_Specialization;
    @FXML
    private ObservableList<Page> pageObservableList;

    private SimpleDateFormat formatter;


    public void initialize(Stage stage) throws IOException {
        menuBarController.init(this);
        setStage(stage);
        addPatientAndCardMenuController = new AddPatientAndCardMenuController();
        windowsController = new WindowsController();
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        patient = GlobalMap.getPatientMap().get(1);
        label_Name.setText(patient.getName() + " " + patient.getSurname());
        page = new Page();

        HttpResponse response = Record.getRecordByPatientId(patient.getId());
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            record = new Record().fromJson(response);
            label_BirthDate.setText(formatter.format(record.getBirthday()));
            label_BloodGroup.setText(record.getBloodGroup());
            if (record.isSex()) {
                label_Sex.setText("Male");
            } else {
                label_Sex.setText("Female");
            }
            label_Weight.setText(String.valueOf(record.getWeight()));
            label_Height.setText(String.valueOf(record.getHeight()));
        }
        response = page.getAllPageByPatientId(patient.getId());
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            pages = page.getAllPage(response);
            pageObservableList = FXCollections.observableList(pages);
            tableColumn_Number.impl_setReorderable(false);
            tableColumn_Number.setSortable(false);
            tableColumn_Number.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(tableView_PageTable.getItems().indexOf(column.getValue()) + 1));

            tableColumn_Date.impl_setReorderable(false);
            tableColumn_Date.setCellValueFactory(new PropertyValueFactory<Page, String>("datePlusDay"));

            tableColumn_DoctorName.impl_setReorderable(false);
            tableColumn_DoctorName.setCellValueFactory(new PropertyValueFactory<Page, String>("doctorInfo"));

            tableColumn_Specialization.impl_setReorderable(false);
            tableColumn_Specialization.setCellValueFactory(new PropertyValueFactory<Page, String>("doctorSpecialization"));
            tableView_PageTable.setItems(pageObservableList);
        }
        button_View.disableProperty().bind(Bindings.isEmpty(tableView_PageTable.getSelectionModel().getSelectedItems()));
        button_Delete.disableProperty().bind(Bindings.isEmpty(tableView_PageTable.getSelectionModel().getSelectedItems()));
        tableView_PageTable.setRowFactory(tv -> {
            TableRow<Page> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        viewPage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        button_ChangePatientInfo.setGraphic(new ImageView(Constant.getEditIcon()));
        button_New.setGraphic(new ImageView(Constant.getAddIcon()));
        button_Delete.setGraphic(new ImageView(Constant.getDeleteIcon()));
        button_View.setGraphic(new ImageView(Constant.getInfoIcon()));
        button_Back.setGraphic(new ImageView(Constant.getReturnIcon()));
    }

    public void viewPage() throws IOException {
        windowsController.openWindowResizable(Constant.getCardPageMenuRoot(), getStage(),
                new CardPageMenuController(), tableView_PageTable.getSelectionModel().getSelectedItem(), "Page", 800, 800);
    }

    public void newPage(ActionEvent event) throws IOException {
        windowsController.openWindow(Constant.getCardPageMenuRoot(), getStage(),
                new CardPageMenuController(), "Page", true, 800, 800);
    }

    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindow(Constant.getMainMenuRoot(), getStage(),
                new MainMenuController(), "", true, 1100, 680);
    }

    public void deletePage(ActionEvent event) throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        if (pages.get(row).getDoctor().getId() == GlobalMap.getDoctorMap().get(1).getId()) {
            boolean result = questionOkCancel("Do you really want to delete this page?");
            if (result) {
                row = tableView_PageTable.getSelectionModel().getFocusedIndex();
                page = pageObservableList.get(row);
                int statusCode = page.deletePage();
                if (checkStatusCode(statusCode)) {
                    pageObservableList.remove(row);
                    tableView_PageTable.refresh();
                }
            }
        } else {
            getAlert(null, "You can`t change this page!", Alert.AlertType.ERROR);
        }
    }

    public void changePatientInfo(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow(Constant.getAddPatientAndRecordMenuRoot(), this.getStage(),
                addPatientAndCardMenuController, patient, record, null, 740, 540);
    }

}
