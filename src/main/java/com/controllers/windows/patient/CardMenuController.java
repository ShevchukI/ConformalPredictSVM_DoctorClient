package com.controllers.windows.patient;

import com.controllers.requests.PageController;
import com.controllers.requests.RecordController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Page;
import com.models.Patient;
import com.models.Record;
import com.tools.Constant;
import com.tools.HazelCastMap;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Admin on 14.01.2019.
 */
public class CardMenuController extends MenuController {

//    @Autowired
//    MainMenuController mainMenuController;
    @Autowired
    HttpResponse response;


    private WindowsController windowsController;
    private RecordController recordController;
    private PageController pageController;
//    private CardPageMenuController cardPageMenuController;
//    private int statusCode;
    private ArrayList<Page> pages;
    private Patient patient;
    private Record record;

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
    private Button button_New;
    @FXML
    private Button button_View;
    @FXML
    private Button button_Delete;
    @FXML
    private Button button_Back;
    @FXML
    private TableColumn<Page, Number> tableColumn_Number;
    @FXML
    private TableView tableView_PageTable;
    @FXML
    private TableColumn tableColumn_Theme;
    @FXML
    private TableColumn tableColumn_Date;
    @FXML
    private TableColumn tableColumn_DoctorName;
    @FXML
    private TableColumn tableColumn_Specialization;
    @FXML
    private ObservableList<Page> pageObservableList;

    private SimpleDateFormat formatter1;


    public void initialize(Stage stage) throws IOException {
        menuBarController.init(this);
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        windowsController = new WindowsController();
        recordController = new RecordController();
        pageController = new PageController();
        formatter1 = new SimpleDateFormat("dd-MM-yyyy");

//        cardPageMenuController = new CardPageMenuController();
        patient = HazelCastMap.getPatientMap().get(1);
        label_Name.setText(patient.getSurname() + " " + patient.getName());


        response = recordController.getRecordByPatientId(patient.getId());
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            record = new Record().fromJson(response);
            label_BirthDate.setText(formatter1.format(record.getBirthday()));
            label_BloodGroup.setText(record.getBloodGroup());
            if (record.isSex()) {
                label_Sex.setText("Male");
            } else {
                label_Sex.setText("Female");
            }
            label_Weight.setText(String.valueOf(record.getWeight()));
            label_Height.setText(String.valueOf(record.getHeight()));
        }
//        else {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText("Status code: " + statusCode);
//            alert.setContentText("ERROR!");
//            alert.showAndWait();
//        }
        response = pageController.getAllPageByPatientId(patient.getId());
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            pages = pageController.getAllPage(response);
            pageObservableList = FXCollections.observableList(pages);
            tableColumn_Number = new TableColumn<Page, Number>("#");
            tableColumn_Number.setSortable(false);
            tableColumn_Number.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(tableView_PageTable.getItems().indexOf(column.getValue()) + 1));
            tableColumn_Theme.setCellValueFactory(new PropertyValueFactory<Page, String>("theme"));
            tableColumn_Date.setCellValueFactory(new PropertyValueFactory<Page, String>("dateFormatted"));
            tableColumn_DoctorName.setCellValueFactory(new PropertyValueFactory<Page, String>("doctorInfo"));
            tableColumn_Specialization.setCellValueFactory(new PropertyValueFactory<Page, String>("doctorSpecialization"));

//            tableColumn_Date.setCellValueFactory(new PropertyValueFactory<Page, Date>("date"));
//            tableColumn_Date.setCellFactory(column -> {
//                TableCell<Page, Date> cell = new TableCell<Page, Date>() {
//                    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//
//                    @Override
//                    protected void updateItem(Date item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if(empty) {
//                            setText(null);
//                        }
//                        else {
//                            System.out.println(format.format(item));
//                            setText("data");
////                            setText(format.format(item));
//                        }
//                    }
//                };
//
//                return cell;
//            });
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

        button_New.setGraphic(new ImageView(Constant.getAddIcon()));
        button_Delete.setGraphic(new ImageView(Constant.getDeleteIcon()));
        button_View.setGraphic(new ImageView(Constant.getInfoIcon()));
        button_Back.setGraphic(new ImageView(Constant.getReturnIcon()));
    }

    public void viewPage(ActionEvent event) throws IOException {
        viewPage();
//        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
//        windowsController.openWindowResizable("patient/cardPageMenu", getStage(), cardPageMenuController,
//                pages, row, "view", "Page", 800, 640);
    }

    public void viewPage() throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        windowsController.openWindowResizable(Constant.getCardPageMenuRoot(), getStage(),
                new CardPageMenuController(), pages, row, "view", "Page", 800, 800);
    }

    public void newPage(ActionEvent event) throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        windowsController.openWindowResizable(Constant.getCardPageMenuRoot(), getStage(),
                new CardPageMenuController(), pages, row, "new", "Page", 800, 800);
    }


    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable(Constant.getMainMenuRoot(), getStage(),
               new MainMenuController(), "Main menu", 600, 800);
    }

    public void deletePage(ActionEvent event) throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
//        if (pages.get(row).getDoctor().getId() == Integer.parseInt(HazelCastMap.getMapByName("user").get("id").toString())) {
        if (pages.get(row).getDoctor().getId() == HazelCastMap.getDoctorMap().get(1).getId()) {
//            ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
//            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
//            Alert questionOfCancellation = new Alert(Alert.AlertType.WARNING, "Do you really want to delete this page?", ok, cancel);
//            questionOfCancellation.setHeaderText(null);
//            Optional<ButtonType> result = questionOfCancellation.showAndWait();
            boolean result = questionOkCancel("Do you really want to delete this page?");
            if (result) {
                try {
                    row = tableView_PageTable.getSelectionModel().getFocusedIndex();
                    int id = pageObservableList.get(row).getId();
                    response = pageController.deletePage(id);
                    setStatusCode(response.getStatusLine().getStatusCode());
                    if (checkStatusCode(getStatusCode())) {
                        getAlert(null, "Page deleted!", Alert.AlertType.INFORMATION);
                        pageObservableList.remove(row);
                        tableView_PageTable.refresh();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            getAlert(null, "You can`t change this page!", Alert.AlertType.ERROR);
        }
    }

}
