package com.controllers.windows.patient;

import com.controllers.windows.diagnostic.DiagnosticMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Dataset;
import com.models.Page;
import com.tools.Constant;
import com.tools.GlobalMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

//import com.controllers.requests.PageController;

/**
 * Created by Admin on 14.01.2019.
 */
public class CardPageMenuController extends MenuController {

    private WindowsController windowsController;
    private ObservableList<Dataset> datasets;


    private String oldDescription;
    private Page page;
    private boolean create;
    private boolean change;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private Label label_PatientName;
    @FXML
    private Label label_Doctor;
    @FXML
    private TextArea textArea_Description;
    @FXML
    private Label label_CurrentDate;
    @FXML
    private Label label_NameResult;
    @FXML
    private Label label_Result;
    @FXML
    private Label label_Confidence;
    @FXML
    private Button button_Save;
    @FXML
    private Button button_Diagnostic;
    @FXML
    private Button button_Change;
    @FXML
    private Button button_Back;
    @FXML
    private ComboBox<Dataset> comboBox_Illness;

    public void initialize(Stage stage) throws IOException {
        menuBarController.init(this);
//        stage.setOnHidden(event -> {
//            HazelCastMap.getInstance().getLifecycleService().shutdown();
//        });
        setStage(stage);
        button_Save.setGraphic(new ImageView(Constant.getOkIcon()));

        windowsController = new WindowsController();
        datasets = FXCollections.observableArrayList();
        create = true;
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        label_Doctor.setText(GlobalMap.getDoctorMap().get(1).getName() + " "
                + GlobalMap.getDoctorMap().get(1).getSurname()
                + " / " + GlobalMap.getDoctorMap().get(1).getSpecialization().getName());
//        label_Doctor.setText(HazelCastMap.getDoctorMap().get(1).getName() + " "
//                + HazelCastMap.getDoctorMap().get(1).getSurname()
//                + " / " + HazelCastMap.getDoctorMap().get(1).getSpecialization().getName());

        label_PatientName.setText(GlobalMap.getPatientMap().get(1).getSurname() + " " + GlobalMap.getPatientMap().get(1).getName());
//        label_PatientName.setText(HazelCastMap.getPatientMap().get(1).getSurname() + " " + HazelCastMap.getPatientMap().get(1).getName());
        label_CurrentDate.setText(String.valueOf(date));

        page = new Page();
        page.setDate(date);

        HttpResponse response = Dataset.getAllActiveDataSet();
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            datasets.addAll(new Dataset().getListFromResponse(response));
        }
        comboBox_Illness.setItems(datasets);
        comboBox_Illness.setCellFactory(new Callback<ListView<Dataset>, ListCell<Dataset>>() {
            @Override
            public ListCell<Dataset> call(ListView<Dataset> p) {
                ListCell cell = new ListCell<Dataset>() {
                    @Override
                    protected void updateItem(Dataset item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });
        comboBox_Illness.setButtonCell(new ListCell<Dataset>() {
            @Override
            protected void updateItem(Dataset t, boolean bln) {
                super.updateItem(t, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(t.getName());
                }
            }
        });
        comboBox_Illness.setVisibleRowCount(5);

        change = true;

        button_Change.setVisible(false);
        button_Change.setGraphic(new ImageView(Constant.getEditIcon()));
        button_Back.setGraphic(new ImageView(Constant.getReturnIcon()));
    }

    public void initialize(Stage stage, Page page) throws IOException {
        menuBarController.init(this);
//        stage.setOnHidden(event -> {
//            HazelCastMap.getInstance().getLifecycleService().shutdown();
//        });
        setStage(stage);
        button_Save.setGraphic(new ImageView(Constant.getOkIcon()));

        windowsController = new WindowsController();
        datasets = FXCollections.observableArrayList();
        create = false;

        this.page = page;

        label_Doctor.setText(GlobalMap.getDoctorMap().get(1).getName() + " "
                + GlobalMap.getDoctorMap().get(1).getSurname()
                + " / " + GlobalMap.getDoctorMap().get(1).getSpecialization().getName());

//        label_Doctor.setText(HazelCastMap.getDoctorMap().get(1).getName() + " "
//                + HazelCastMap.getDoctorMap().get(1).getSurname()
//                + " / " + HazelCastMap.getDoctorMap().get(1).getSpecialization().getName());

        label_PatientName.setText(GlobalMap.getPatientMap().get(1).getName() + " " + GlobalMap.getPatientMap().get(1).getSurname());
//        label_PatientName.setText(HazelCastMap.getPatientMap().get(1).getName() + " " + HazelCastMap.getPatientMap().get(1).getSurname());
        label_CurrentDate.setText(String.valueOf(page.getDatePlusDay()));

        textArea_Description.setText(this.page.getDescription());
        textArea_Description.setEditable(false);
        oldDescription = textArea_Description.getText();

        if (this.page.getAnswer() != null) {
            String[] result = this.page.getAnswer().split(":");
//            if (result.length == 3) {
            if (result.length == 3) {
                label_NameResult.setText(result[0]);
                label_Result.setText(result[1]);
                label_Confidence.setText(result[2]);
            } else if (result.length == 2) {
                label_NameResult.setText(result[0]);
                label_Result.setText(result[1]);
            }

//            }
        }
        HttpResponse response = Dataset.getAllActiveDataSet();
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            datasets.addAll(new Dataset().getListFromResponse(response));
        }
        comboBox_Illness.setItems(datasets);
        comboBox_Illness.setCellFactory(new Callback<ListView<Dataset>, ListCell<Dataset>>() {
            @Override
            public ListCell<Dataset> call(ListView<Dataset> p) {
                ListCell cell = new ListCell<Dataset>() {
                    @Override
                    protected void updateItem(Dataset item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });
        comboBox_Illness.setButtonCell(new ListCell<Dataset>() {
            @Override
            protected void updateItem(Dataset t, boolean bln) {
                super.updateItem(t, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(t.getName());
                }
            }
        });
        comboBox_Illness.setVisibleRowCount(5);

        if (this.page.getDoctor().getId() == GlobalMap.getDoctorMap().get(1).getId()) {
//        if (this.page.getDoctor().getId() == HazelCastMap.getDoctorMap().get(1).getId()) {
            button_Change.setDisable(false);
        } else {
            button_Change.setDisable(true);
        }

        change = false;


        comboBox_Illness.setDisable(true);
        button_Diagnostic.setDisable(true);
        button_Save.setDisable(true);

        button_Change.setGraphic(new ImageView(Constant.getEditIcon()));
        button_Back.setGraphic(new ImageView(Constant.getReturnIcon()));
    }


    public void savePage(ActionEvent event) throws IOException, ParseException {
        page.setDescription(textArea_Description.getText());
        if (create) {
            page.createPage(GlobalMap.getPatientMap().get(1).getId());
//            page.createPage(HazelCastMap.getPatientMap().get(1).getId());
            change = false;
        } else {
            page.changePage();
        }
    }


    public void backToCardMenu(ActionEvent event) throws IOException {

        if (change) {
            boolean result = questionOkCancel("Do you really want to leave without save?");
            if (result) {
                windowsController.openWindow(Constant.getCardMenuRoot(), getStage(),
                        new CardMenuController(), null, true, 600, 680);
            }
        } else {
            windowsController.openWindow(Constant.getCardMenuRoot(), getStage(),
                    new CardMenuController(), null, true, 600, 680);
        }
    }

//    }

    public void changePage(ActionEvent event) {
        button_Save.setDisable(false);
        textArea_Description.setEditable(true);
        comboBox_Illness.setDisable(false);
        button_Diagnostic.setDisable(false);
        change = true;
    }

    public void diagnostic(ActionEvent event) throws IOException {
        if (comboBox_Illness.getSelectionModel().getSelectedItem() != null) {
            Dataset dataset = new Dataset(comboBox_Illness.getSelectionModel().getSelectedItem().getId(),
                    comboBox_Illness.getSelectionModel().getSelectedItem().getName(),
                    comboBox_Illness.getSelectionModel().getSelectedItem().getColumns());
            GlobalMap.getDataSetMap().put(1, dataset);
//            HazelCastMap.getDataSetMap().put(1, dataset);
            windowsController.openNewModalWindow(Constant.getDiagnosticMenuRoot(), getStage(),
                    new DiagnosticMenuController(), page, "", 600, 440);
        } else {
            getAlert(null, "Please, choice illness!", Alert.AlertType.INFORMATION);
        }
    }
}
