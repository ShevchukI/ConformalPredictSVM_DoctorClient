package com.controllers.windows.patient;

import com.controllers.requests.IllnessController;
import com.controllers.requests.PageController;
import com.controllers.windows.diagnostic.DiagnosticMenuController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Dataset;
import com.models.Page;
import com.tools.Constant;
import com.tools.HazelCastMap;
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

/**
 * Created by Admin on 14.01.2019.
 */
public class CardPageMenuController extends MenuController {

    private WindowsController windowsController;
    private ObservableList<Dataset> datasets;


    private String oldDescription;
    private PageController pageController;
    private Page page;
    private boolean create;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private Label label_PatientName;
    @FXML
    private Label label_Doctor;
    @FXML
    private TextArea textArea_Symptoms;
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
    private Button button_Previous;
    @FXML
    private Button button_Next;
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
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        windowsController = new WindowsController();
        datasets = FXCollections.observableArrayList();
        pageController = new PageController();
        create = true;
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        label_Doctor.setText(HazelCastMap.getDoctorMap().get(1).getName() + " "
                + HazelCastMap.getDoctorMap().get(1).getSurname()
                + " / " + HazelCastMap.getDoctorMap().get(1).getSpecialization().getName());

        label_PatientName.setText(HazelCastMap.getPatientMap().get(1).getSurname() + " " + HazelCastMap.getPatientMap().get(1).getName());
        label_CurrentDate.setText(String.valueOf(date));

        page = new Page();
        page.setDate(date);

        HttpResponse response = IllnessController.getAllActiveDataSet();
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


        button_Change.setVisible(false);
        button_Back.setGraphic(new ImageView(Constant.getReturnIcon()));
    }

    public void initialize(Stage stage, Page page) throws IOException {
        menuBarController.init(this);
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        windowsController = new WindowsController();
        datasets = FXCollections.observableArrayList();
        pageController = new PageController();
        create = false;

        this.page = page;

        label_Doctor.setText(HazelCastMap.getDoctorMap().get(1).getName() + " "
                + HazelCastMap.getDoctorMap().get(1).getSurname()
                + " / " + HazelCastMap.getDoctorMap().get(1).getSpecialization().getName());

        label_PatientName.setText(HazelCastMap.getPatientMap().get(1).getName() + " " + HazelCastMap.getPatientMap().get(1).getSurname());
        label_CurrentDate.setText(String.valueOf(page.getDate()));

        textArea_Description.setText(this.page.getDescription());
        textArea_Symptoms.setEditable(false);
        textArea_Description.setEditable(false);
        oldDescription = textArea_Description.getText();

        if (this.page.getAnswer() != null) {
            String[] result = this.page.getAnswer().split(":");
//            if (result.length == 3) {
            if(result.length == 3){
                label_NameResult.setText(result[0]);
                label_Result.setText(result[1]);
                label_Confidence.setText(result[2]);
            } else if(result.length == 2){
                label_NameResult.setText(result[0]);
                label_Result.setText(result[1]);
            }

//            }
        }
        HttpResponse response = IllnessController.getAllActiveDataSet();
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

        if (this.page.getDoctor().getId() == HazelCastMap.getDoctorMap().get(1).getId()) {
            button_Change.setDisable(false);
        } else {
            button_Change.setDisable(true);
        }


        button_Save.setDisable(true);

        button_Back.setGraphic(new ImageView(Constant.getReturnIcon()));
    }


    public void savePage(ActionEvent event) throws IOException, ParseException {
        if (create) {
            page.setDescription(textArea_Description.getText());
            HttpResponse response = pageController.createPage(page, HazelCastMap.getPatientMap().get(1).getId());
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                getAlert(null, "Saved!", Alert.AlertType.INFORMATION);
                windowsController.openWindow(Constant.getCardMenuRoot(), getStage(),
                        new CardMenuController(), null, true, 600, 680);
            }
        } else {
            page.setDescription(textArea_Description.getText());
            HttpResponse response = pageController.changePage(page, page.getId());
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                getAlert(null, "Changed!", Alert.AlertType.INFORMATION);

                windowsController.openWindow(Constant.getCardMenuRoot(), getStage(),
                        new CardMenuController(), null, true, 600, 680);
            }
        }
    }


    public void backToCardMenu(ActionEvent event) throws IOException {
        if (create) {
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

    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindow(Constant.getMainMenuRoot(), getStage(),
                new MainMenuController(), null, true, 600, 680);

    }

    public void changePage(ActionEvent event) {
        button_Save.setDisable(false);
        textArea_Symptoms.setEditable(true);
        textArea_Description.setEditable(true);
        comboBox_Illness.setDisable(false);
        button_Diagnostic.setDisable(false);
    }

    public void diagnostic(ActionEvent event) throws IOException {
        if (comboBox_Illness.getSelectionModel().getSelectedItem() != null) {
            Dataset dataset = new Dataset(comboBox_Illness.getSelectionModel().getSelectedItem().getId(),
                    comboBox_Illness.getSelectionModel().getSelectedItem().getName(),
                    comboBox_Illness.getSelectionModel().getSelectedItem().getColumns());
            HazelCastMap.getDataSetMap().put(1, dataset);
            windowsController.openNewModalWindow(Constant.getDiagnosticMenuRoot(), getStage(),
                    new DiagnosticMenuController(), page, "", 600, 440);
        } else {
            getAlert(null, "Please, choice illness!", Alert.AlertType.INFORMATION);
        }
    }

    public void previousPage(ActionEvent event) {
    }

    public void nextPage(ActionEvent event) {
    }
}
