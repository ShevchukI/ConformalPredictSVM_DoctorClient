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
import javafx.beans.binding.BooleanBinding;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Admin on 14.01.2019.
 */
public class CardPageMenuController extends MenuController {

    private WindowsController windowsController;
    private ObservableList<Dataset> datasets;

    private static final String NEW = "new";
    private static final String VIEW = "view";

    private String action;
    private int row;
    private ArrayList<Page> pages;
    private String oldDescription;
    private String oldSymptoms;
    private HttpResponse response;
    private PageController pageController;
    private LocalDate localDate;
    private DateTimeFormatter formatter;
    private int pageId;
    private Page page;

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

    private SimpleDateFormat formatter1;
    private SimpleDateFormat formatter2;

    public void initialize(ArrayList<Page> pages, int row, Stage stage, String action) throws IOException {
        menuBarController.init(this);
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        windowsController = new WindowsController();
        datasets = FXCollections.observableArrayList();
        pageController = new PageController();
        localDate = LocalDate.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter1 = new SimpleDateFormat("dd-MM-yyyy");
        formatter2 = new SimpleDateFormat("yyyy-MM-dd");

        this.pages = pages;
        this.row = row;
        this.action = action;
        label_PatientName.setText(HazelCastMap.getPatientMap().get(1).getSurname() + " " + HazelCastMap.getPatientMap().get(1).getName());
        textArea_Description.setWrapText(true);
        if (action.equals(VIEW)) {
            oldSymptoms = textArea_Symptoms.getText();
            oldDescription = textArea_Description.getText();
            textArea_Symptoms.setText(pages.get(row).getTheme());
            textArea_Symptoms.setEditable(false);
            textArea_Description.setText(pages.get(row).getDescription());
            textArea_Description.setEditable(false);

            label_CurrentDate.setText(formatter2.format(pages.get(row).getDate()));
            if (pages.get(row).getAnswer() != null) {
                String[] result = pages.get(row).getAnswer().split(":");
                if (result.length == 2) {
                    label_NameResult.setText(result[0]);
                    label_Result.setText(result[1]);
                }
            }
            button_Save.setDisable(true);
            comboBox_Illness.setDisable(true);
            button_Diagnostic.setDisable(true);
            label_Doctor.setText(pages.get(row).getDoctor().getName() + " " + pages.get(row).getDoctor().getSurname() + " / " + pages.get(row).getDoctor().getSpecialization().getName());
            if (pages.get(row).getDoctor().getId() == HazelCastMap.getDoctorMap().get(1).getId()) {
                button_Change.setDisable(false);
            } else {
                button_Change.setDisable(true);
            }
        } else if (action.equals(NEW)) {
            label_CurrentDate.setText(localDate.format(formatter));
            Page page1 = new Page();
            try {
                page1.setDate(formatter2.parse(label_CurrentDate.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            response = pageController.createPage(page1, HazelCastMap.getPatientMap().get(1).getId());
            setStatusCode(response.getStatusLine().getStatusCode());
            pageId = Integer.parseInt(Constant.responseToString(response));
            if (checkStatusCode(getStatusCode())) {
                pages.add(page1);
                HazelCastMap.getMiscellaneousMap().put("pageId", pageId);
            }
            label_CurrentDate.setText(localDate.format(formatter));
            button_Save.setDisable(false);
            button_Previous.setDisable(true);
            button_Next.setDisable(true);
            button_Change.setDisable(true);
            label_Doctor.setText(HazelCastMap.getDoctorMap().get(1).getSurname() + " " +
                    HazelCastMap.getDoctorMap().get(1).getName() + " / " + HazelCastMap.getDoctorMap().get(1).getSpecialization().getName());
            BooleanBinding checkEmptyField = new BooleanBinding() {
                {
                    super.bind(textArea_Symptoms.textProperty(),
                            textArea_Description.textProperty());
                }

                @Override
                protected boolean computeValue() {
                    return (textArea_Symptoms.getText().isEmpty()
                            && textArea_Description.getText().isEmpty());
                }
            };
            button_Save.disableProperty().bind(checkEmptyField);
        }
        if (row == 0) {
            button_Previous.setDisable(true);
        }
        if (row == pages.size() - 1) {
            button_Next.setDisable(true);
        }
        response = IllnessController.getAllActiveDataSet();
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

        button_Back.setGraphic(new ImageView(Constant.getReturnIcon()));
    }

    public void previousPage(ActionEvent event) {
        row--;
        textArea_Symptoms.setText(pages.get(row).getTheme());
        textArea_Description.setText(pages.get(row).getDescription());
        label_CurrentDate.setText(formatter1.format(pages.get(row).getDate()));

        if (pages.get(row).getAnswer() != null && !pages.get(row).getAnswer().equals("")) {
            label_NameResult.setText(pages.get(row).getAnswer().substring(0, pages.get(row).getAnswer().length() - 9));
            label_Result.setText(pages.get(row).getAnswer().substring(pages.get(row).getAnswer().length() - 8, pages.get(row).getAnswer().length()));
        }
        if (action.equals(VIEW)) {
            textArea_Symptoms.setEditable(false);
            textArea_Description.setEditable(false);
            label_Doctor.setText(pages.get(row).getDoctor().getName() + " " + pages.get(row).getDoctor().getSurname() + " / " + pages.get(row).getDoctor().getSpecialization().getName());

        }
        if (row == 0) {
            button_Previous.setDisable(true);
        }
        if (row != pages.size() - 1) {
            button_Next.setDisable(false);
        }
        if (pages.get(row).getDoctor().getId() == HazelCastMap.getDoctorMap().get(1).getId()) {
            button_Change.setDisable(false);
        } else {
            button_Change.setDisable(true);
        }

    }

    public void nextPage(ActionEvent event) {
        if (action.equals(NEW)) {
            Date date = new Date();
            label_CurrentDate.setText(localDate.format(formatter));
            textArea_Symptoms.setText("");
            textArea_Description.setText("");
            button_Next.setDisable(true);
            if (pages.get(row).getAnswer() != null && !pages.get(row).getAnswer().equals("")) {
                label_NameResult.setText(pages.get(row).getAnswer().substring(0, pages.get(row).getAnswer().length() - 9));
                label_Result.setText(pages.get(row).getAnswer().substring(pages.get(row).getAnswer().length() - 8, pages.get(row).getAnswer().length()));
            } else {
                label_NameResult.setText("");
                label_Result.setText("");
            }
        } else {
            row++;

            textArea_Symptoms.setText(pages.get(row).getTheme());
            textArea_Description.setText(pages.get(row).getDescription());
            label_CurrentDate.setText(formatter1.format(pages.get(row).getDate()));

            if (pages.get(row).getAnswer() != null && !pages.get(row).getAnswer().equals("")) {
                label_NameResult.setText(pages.get(row).getAnswer().substring(0, pages.get(row).getAnswer().length() - 9));
                label_Result.setText(pages.get(row).getAnswer().substring(pages.get(row).getAnswer().length() - 8, pages.get(row).getAnswer().length()));
            } else {
                label_NameResult.setText("");
                label_Result.setText("");
            }

            if (action.equals(VIEW)) {
                textArea_Symptoms.setEditable(false);
                textArea_Description.setEditable(false);
                label_Doctor.setText(pages.get(row).getDoctor().getName() + " " + pages.get(row).getDoctor().getSurname() + " / " + pages.get(row).getDoctor().getSpecialization().getName());
                if (pages.get(row).getAnswer() != null && !pages.get(row).getAnswer().equals("")) {
                    label_NameResult.setText(pages.get(row).getAnswer().substring(0, pages.get(row).getAnswer().length() - 9));
                    label_Result.setText(pages.get(row).getAnswer().substring(pages.get(row).getAnswer().length() - 8, pages.get(row).getAnswer().length()));
                } else {
                    label_NameResult.setText("");
                    label_Result.setText("");
                }
            }
            if (row == pages.size() - 1) {
                button_Next.setDisable(true);
            }
            if (row != 0) {
                button_Previous.setDisable(false);
            }
            if (pages.get(row).getDoctor().getId() == HazelCastMap.getDoctorMap().get(1).getId()) {
                button_Change.setDisable(false);
            } else {
                button_Change.setDisable(true);
            }
        }
    }

    public void savePage(ActionEvent event) throws IOException, ParseException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (action.equals(VIEW)) {
            boolean result = questionOkCancel("Do you really want to save the changes?");
            if (result) {
                page = new Page(textArea_Symptoms.getText(), textArea_Description.getText(), pages.get(row).getParameters(), pages.get(row).getAnswer(), pages.get(row).getDate());
                pages.get(row).setTheme(textArea_Symptoms.getText());
                pages.get(row).setDescription(textArea_Description.getText());
                pages.get(row).setAnswer(label_NameResult.getText() + ":" + label_Result.getText());
                response = pageController.changePage(page, pages.get(row).getId());
                setStatusCode(response.getStatusLine().getStatusCode());
            }
        } else if (action.equals(NEW)) {
            page = new Page(textArea_Symptoms.getText(), textArea_Description.getText(),
                    "", label_NameResult.getText() + ":" + label_Result.getText(), formatter2.parse(label_CurrentDate.getText()));
            response = pageController.changePage(page, pageId);
            setStatusCode(response.getStatusLine().getStatusCode());
        }
        if (checkStatusCode(getStatusCode())) {
            if (action.equals(VIEW)) {
                pages.get(row).setPage(page);
                alert.setContentText("Changed!");
                alert.showAndWait();
            } else if (action.equals(NEW)) {
                pages.add(page);
                alert.setContentText("Saved!");
                alert.showAndWait();
                button_Next.setDisable(false);
                windowsController.openWindow(Constant.getCardMenuRoot(), getStage(),
                        new CardMenuController(), null, true, 600, 680);

            }
        }
    }

    public void backToCardMenu(ActionEvent event) throws IOException {
        if (action.equals("new")) {
            boolean result = questionOkCancel("Do you really want to leave without save?");
            if (result) {
                response = pageController.deletePage(pageId);
                setStatusCode(response.getStatusLine().getStatusCode());
                if (checkStatusCode(getStatusCode())) {
                    windowsController.openWindow(Constant.getCardMenuRoot(), getStage(),
                            new CardMenuController(), null, true, 600, 680);

                }
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

    public void changeText() {
        if (action.equals(VIEW)) {
            if (oldDescription.equals(textArea_Description.getText()) || oldSymptoms.equals(textArea_Symptoms.getText())) {
                button_Save.setDisable(true);
            }

        } else if (action.equals(NEW)) {
        }
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
            if(action.equals(VIEW)) {
                HazelCastMap.getMiscellaneousMap().put("pageId", pages.get(row).getId());
            }
            windowsController.openNewModalWindow(Constant.getDiagnosticMenuRoot(), getStage(),
                    new DiagnosticMenuController(), "Main menu", false, 600, 440);
        } else {
            getAlert(null, "Please, choice illness!", Alert.AlertType.INFORMATION);
        }
    }
}
