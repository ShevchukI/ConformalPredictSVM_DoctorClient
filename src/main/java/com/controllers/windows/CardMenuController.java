package com.controllers.windows;

import com.controllers.requests.PageController;
import com.controllers.requests.RecordController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.models.Page;
import com.models.Patient;
import com.models.Record;
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
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Admin on 14.01.2019.
 */
public class CardMenuController extends MenuController {

    @Autowired
    MainMenuController mainMenuController;

    @Autowired
    HttpResponse response;

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
    private Label label_Count;

    @FXML
    private Button button_View;

    @FXML
    private Button button_Delete;

    @FXML
    private TableColumn<Page, Number> tableColumn_Number = new TableColumn<Page, Number>("#");

    @FXML
    private TableView tableView_PageTable;

    @FXML
    private TableColumn tableColumn_Theme;

    @FXML
    private TableColumn tableColumn_Date;

    @FXML
    private ObservableList<Page> pageObservableList;

    private Encryptor encryptor = new Encryptor();

    private Placeholder placeholder = new Placeholder();

    private WindowsController windowsController = new WindowsController();

    private RecordController recordController = new RecordController();

    private PageController pageController = new PageController();

    private CardPageMenuController cardPageMenuController = new CardPageMenuController();

    private int statusCode;

    private ArrayList<Page> pages;

    @FXML
    public void initialize(Doctor doctor, Stage stage, HazelcastInstance hazelcastInstance) {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        this.doctor = doctor;
        menuBarController.init(this);
    }

    public void initialize(Patient patient, Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        menuBarController.init(this);
        setPatient(patient);
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        label_Name.setText(getPatient().getSurname() + " " + getPatient().getName());
        response = recordController.getRecordByPatientId(encryptor.decrypt(getMap().get("key").toString(),
                getMap().get("vector").toString(), getMap().get("login").toString()),
                encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                        getMap().get("password").toString()),
                getPatient().getId());

        statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            Record record = new Record().fromJson(response);
            label_BirthDate.setText(record.getBirthday());
            label_BloodGroup.setText(record.getBloodGroup());
            if (record.isSex()) {
                label_Sex.setText("Male");
            } else {
                label_Sex.setText("Female");
            }
            label_Weight.setText(String.valueOf(record.getWeight()));
            label_Height.setText(String.valueOf(record.getHeight()));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Status code: " + statusCode);
            alert.setContentText("ERROR!");
            alert.showAndWait();
        }

        response = pageController.getAllPageByPatientId(encryptor.decrypt(getMap().get("key").toString(),
                getMap().get("vector").toString(), getMap().get("login").toString()),
                encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                        getMap().get("password").toString()),
                getPatient().getId());

        statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            pages = pageController.getAllPage(response);
            pageObservableList = FXCollections.observableList(pages);
            tableColumn_Number.setSortable(false);
            tableColumn_Number.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(tableView_PageTable.getItems().indexOf(column.getValue()) + 1));
            tableColumn_Theme.setCellValueFactory(new PropertyValueFactory<Page, String>("theme"));
            tableColumn_Date.setCellValueFactory(new PropertyValueFactory<Page, String>("date"));
            tableView_PageTable.setItems(pageObservableList);
            label_Count.setText(String.valueOf(pageObservableList.size()));
        }

        button_View.disableProperty().bind(Bindings.isEmpty(tableView_PageTable.getSelectionModel().getSelectedItems()));
        button_Delete.disableProperty().bind(Bindings.isEmpty(tableView_PageTable.getSelectionModel().getSelectedItems()));

        tableView_PageTable.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
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
    }

    public void viewPage(ActionEvent event) throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        windowsController.openWindowResizable("cardPageMenu.fxml", getStage(), getInstance(), cardPageMenuController,
                patient, pages, row, "view", "Page", 800, 640);
    }

    public void viewPage() throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        windowsController.openWindowResizable("cardPageMenu.fxml", getStage(), getInstance(), cardPageMenuController,
                patient, pages, row, "view", "Page", 800, 640);

    }

    public void newPage(ActionEvent event) throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        windowsController.openWindowResizable("cardPageMenu.fxml", getStage(), getInstance(), cardPageMenuController,
                patient, pages, row, "new", "Page", 800, 640);

    }

    public void getPlaceholderAlert(ActionEvent event) {
        placeholder.getAlert();
    }

    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("mainMenu.fxml", getStage(), getInstance(), mainMenuController, "Main menu", 600, 640);
    }


    public void deletePage(ActionEvent event) throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (pages.get(row).getDoctor().getId() == Integer.parseInt(getMap().get("id").toString())) {
            ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert questionOfCancellation = new Alert(Alert.AlertType.WARNING, "Do you really want to delete this page?", ok, cancel);
            questionOfCancellation.setHeaderText(null);
            Optional<ButtonType> result = questionOfCancellation.showAndWait();

            if (result.orElse(cancel) == ok) {
                try {
                    row = tableView_PageTable.getSelectionModel().getFocusedIndex();
                    int id = pageObservableList.get(row).getId();
                    response = pageController.deletePage(encryptor.decrypt(getMap().get("key").toString(),
                            getMap().get("vector").toString(), getMap().get("login").toString()),
                            encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                                    getMap().get("password").toString()), id);
                    statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        alert.setContentText("Page deleted!");
                        pageObservableList.remove(row);
                        tableView_PageTable.refresh();
                    } else {
                        alert.setContentText("Error! Status code: " + statusCode);
                    }
                    alert.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            alert.setContentText("You can`t change this page!");
            alert.showAndWait();
        }
    }
}
