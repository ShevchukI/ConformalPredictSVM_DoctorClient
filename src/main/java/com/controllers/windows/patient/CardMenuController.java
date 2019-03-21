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

    @Autowired
    MainMenuController mainMenuController;
    @Autowired
    HttpResponse response;


    private WindowsController windowsController = new WindowsController();
    private RecordController recordController = new RecordController();
    private PageController pageController = new PageController();
    private CardPageMenuController cardPageMenuController = new CardPageMenuController();
    private int statusCode;
    private ArrayList<Page> pages;

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
    private TableColumn<Page, Number> tableColumn_Number = new TableColumn<Page, Number>("#");
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
    @FXML
    private

    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");


    public void initialize(Stage stage) throws IOException {
        menuBarController.init(this);
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        label_Name.setText(Constant.getMapByName("patient").get("name") + " " + Constant.getMapByName("patient").get("surname"));
        response = recordController.getRecordByPatientId(Constant.getAuth(),
                Integer.parseInt(Constant.getMapByName("patient").get("id").toString()));
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            Record record = new Record().fromJson(response);
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
        response = pageController.getAllPageByPatientId(Constant.getAuth(),
                Integer.parseInt(Constant.getMapByName("patient").get("id").toString()));
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            pages = pageController.getAllPage(response);
            pageObservableList = FXCollections.observableList(pages);
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

        button_New.setGraphic(new ImageView("/img/icons/add.png"));
        button_Delete.setGraphic(new ImageView("/img/icons/delete.png"));
        button_View.setGraphic(new ImageView("/img/icons/info.png"));
        button_Back.setGraphic(new ImageView("/img/icons/return.png"));
    }

    public void viewPage(ActionEvent event) throws IOException {
        viewPage();
//        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
//        windowsController.openWindowResizable("patient/cardPageMenu", getStage(), cardPageMenuController,
//                pages, row, "view", "Page", 800, 640);
    }

    public void viewPage() throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        windowsController.openWindowResizable("patient/cardPageMenu", getStage(), cardPageMenuController,
                pages, row, "view", "Page", 800, 680);
    }

    public void newPage(ActionEvent event) throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        windowsController.openWindowResizable("patient/cardPageMenu", getStage(), cardPageMenuController,
                pages, row, "new", "Page", 800, 800);
    }


    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("menu/mainMenu", getStage(), mainMenuController,
                "Main menu", 600, 800);
    }

    public void deletePage(ActionEvent event) throws IOException {
        int row = tableView_PageTable.getSelectionModel().getFocusedIndex();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (pages.get(row).getDoctor().getId() == Integer.parseInt(Constant.getMapByName("user").get("id").toString())) {
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
                    response = pageController.deletePage(Constant.getAuth(), id);
                    statusCode = response.getStatusLine().getStatusCode();
                    if (checkStatusCode(statusCode)) {
                        alert.setContentText("Page deleted!");
                        pageObservableList.remove(row);
                        tableView_PageTable.refresh();
                    }
//                    else {
//                        alert.setContentText("Error! Status code: " + statusCode);
//                    }
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

    public void test(){
        for (Page page : pages) {
            String pattern = "MM-dd-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//            Date convertedCurrentDate = null;
//            Date convertedCurrentDate1 = null;
//            Date date = null;
//            try {
//                convertedCurrentDate = sdf.parse(page.getDate());
//                convertedCurrentDate1 = sdf1.parse(page.getDate());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            try {
//                date = sdf.parse(page.getDate());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            System.out.println(page.getDate() + " : " + convertedCurrentDate + " : " + convertedCurrentDate1 + " :: "+ date +":"+simpleDateFormat.format(date));
        }
    }
}
