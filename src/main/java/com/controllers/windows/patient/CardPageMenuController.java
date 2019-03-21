package com.controllers.windows.patient;

import com.controllers.requests.IllnessController;
import com.controllers.requests.PageController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Dataset;
import com.models.Page;
import com.tools.Constant;
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
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    CardMenuController cardMenuController;

    private WindowsController windowsController = new WindowsController();
    private MainMenuController mainMenuController = new MainMenuController();
    private DiagnosticMenuController diagnosticMenuController = new DiagnosticMenuController();
    private IllnessController illnessController = new IllnessController();
    private ObservableList<Dataset> datasets = FXCollections.observableArrayList();

    private String action;
    private int row;
    private ArrayList<Page> pages;
    private String oldDescription;
    private String oldTheme;
    private HttpResponse response;
    private PageController pageController = new PageController();
    private LocalDate localDate = LocalDate.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private int statusCode;
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


    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");


    public void initialize(ArrayList<Page> pages, int row, Stage stage, String action) throws IOException {
        menuBarController.init(this);
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        this.pages = pages;
        this.row = row;
        this.action = action;
        label_PatientName.setText(Constant.getMapByName("patient").get("name") + " " + Constant.getMapByName("patient").get("surname"));
        textArea_Description.setWrapText(true);
        if (action.equals("view")) {
            oldTheme = textArea_Symptoms.getText();
            oldDescription = textArea_Description.getText();
            textArea_Symptoms.setText(pages.get(row).getTheme());
            textArea_Symptoms.setEditable(false);
            textArea_Description.setText(pages.get(row).getDescription());
            textArea_Description.setEditable(false);

            label_CurrentDate.setText(formatter1.format(pages.get(row).getDate()));
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
            if (pages.get(row).getDoctor().getId() == Integer.parseInt(Constant.getMapByName("user").get("id").toString())) {
                button_Change.setDisable(false);
            } else {
                button_Change.setDisable(true);
            }
        } else if (action.equals("new")) {
            label_CurrentDate.setText(localDate.format(formatter));
            Page page1 = new Page();
            try {
                page1.setDate(formatter2.parse(label_CurrentDate.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            response = pageController.createPage(Constant.getAuth(), page1,
                    Integer.parseInt(Constant.getMapByName("patient").get("id").toString()));
            statusCode = response.getStatusLine().getStatusCode();
            pageId = Integer.parseInt(Constant.responseToString(response));
            if (checkStatusCode(statusCode)) {
                Constant.getMapByName("misc").put("pageId", pageId);
            }
            label_CurrentDate.setText(localDate.format(formatter));
            button_Previous.setDisable(true);
            button_Next.setDisable(true);
            button_Change.setDisable(true);
            label_Doctor.setText(Constant.getMapByName("user").get("name").toString() + " " +
                    Constant.getMapByName("user").get("surname").toString() + " / " + Constant.getMapByName("user").get("specName").toString());
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
        response = illnessController.getAllActiveDataSet(Constant.getAuth());
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
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
        button_Back.setGraphic(new ImageView("/img/icons/return.png"));
    }

    public void previousPage(ActionEvent event) {
        row--;
        oldDescription = textArea_Description.getText();
        changeText();
        textArea_Symptoms.setText(pages.get(row).getTheme());
        textArea_Description.setText(pages.get(row).getDescription());
        label_CurrentDate.setText(formatter1.format(pages.get(row).getDate()));
//        label_CurrentDate.setText(pages.get(row).getDate());
        label_Result.setText(pages.get(row).getAnswer());
        if (action.equals("view")) {
            textArea_Symptoms.setEditable(false);
            textArea_Description.setEditable(false);
            label_Doctor.setText(pages.get(row).getDoctor().getName() + " " +pages.get(row).getDoctor().getSurname() + " / " + pages.get(row).getDoctor().getSpecialization().getName());

        }
        if (row == 0) {
            button_Previous.setDisable(true);
        }
        if (row != pages.size() - 1) {
            button_Next.setDisable(false);
        }
        if (pages.get(row).getDoctor().getId() == Integer.parseInt(Constant.getMapByName("user").get("id").toString())) {
            button_Change.setDisable(false);
        } else {
            button_Change.setDisable(true);
        }

    }

    public void nextPage(ActionEvent event) {
        if (action.equals("new")) {
            Date date = new Date();
            label_CurrentDate.setText(localDate.format(formatter));
            textArea_Symptoms.setText("");
            textArea_Description.setText("");
            button_Next.setDisable(true);

        } else {
            row++;
            oldDescription = textArea_Description.getText();
            changeText();
            textArea_Symptoms.setText(pages.get(row).getTheme());
            textArea_Description.setText(pages.get(row).getDescription());
            label_CurrentDate.setText(formatter1.format(pages.get(row).getDate()));
//            label_CurrentDate.setText(pages.get(row).getDate());
            label_Result.setText(pages.get(row).getAnswer());
            if (action.equals("view")) {
                textArea_Symptoms.setEditable(false);
                textArea_Description.setEditable(false);
                label_Doctor.setText(pages.get(row).getDoctor().getName() + " " + pages.get(row).getDoctor().getSurname() + " / " + pages.get(row).getDoctor().getSpecialization().getName());

            }
            if (row == pages.size() - 1) {
                button_Next.setDisable(true);
            }
            if (row != 0) {
                button_Previous.setDisable(false);
            }
            if (pages.get(row).getDoctor().getId() == Integer.parseInt(Constant.getMapByName("user").get("id").toString())) {
                button_Change.setDisable(false);
            } else {
                button_Change.setDisable(true);
            }
        }
    }

    public void savePage(ActionEvent event) throws IOException, ParseException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (action.equals("view")) {
            boolean result = questionOkCancel("Do you really want to save the changes?");
            if (result) {
                page = new Page(textArea_Symptoms.getText(), textArea_Description.getText(), pages.get(row).getParameters(), pages.get(row).getAnswer(), pages.get(row).getDate());
                response = pageController.changePage(Constant.getAuth(), page, pages.get(row).getId());
                statusCode = response.getStatusLine().getStatusCode();
            }
        } else if (action.equals("new")) {
            page = new Page(textArea_Symptoms.getText(), textArea_Description.getText(),
                    "placeholderParam", label_Result.getText(), formatter2.parse(label_CurrentDate.getText()));
            response = pageController.changePage(Constant.getAuth(), page, pageId);
            statusCode = response.getStatusLine().getStatusCode();
        }
        if (checkStatusCode(statusCode)) {
            if (action.equals("view")) {
                pages.get(row).setPage(page);
                alert.setContentText("Changed!");
                alert.showAndWait();
            } else if (action.equals("new")) {
                alert.setContentText("Saved!");
                alert.showAndWait();
                button_Next.setDisable(false);
                windowsController.openWindowResizable("patient/cardMenu", getStage(),
                        cardMenuController, "Card", 600, 680);
            }
        }
    }

    public void backToCardMenu(ActionEvent event) throws IOException {
        if (action.equals("new")) {
            boolean result = questionOkCancel("Do you really want to leave without save?");
            if (result) {
                response = pageController.deletePage(Constant.getAuth(), pageId);
                statusCode = response.getStatusLine().getStatusCode();
                if (checkStatusCode(statusCode)) {
                    windowsController.openWindowResizable("patient/cardMenu", getStage(),
                            cardMenuController, "Card", 600, 680);
                }
            }
        } else {
            windowsController.openWindowResizable("patient/cardMenu", getStage(),
                    cardMenuController, "Card", 600, 680);
        }
    }

    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("menu/mainMenu", getStage(),
                mainMenuController, "Main menu", 600, 680);
    }

    public void changeText() {
        if (action.equals("view")) {
            if (oldDescription.equals(textArea_Description.getText()) || oldTheme.equals(textArea_Symptoms.getText())) {
                button_Save.setDisable(true);
            }
//            else if (!oldDescription.equals(textArea_Description.getText()) || !oldTheme.equals(textField_Theme.getText())) {
//                button_Save.setDisable(false);
//            }
        } else if (action.equals("new")) {
//            button_Save.setDisable(false);
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
//        try {
//
//            Constant.getMapByName("dataset").put("name", comboBox_Illness.getSelectionModel().getSelectedItem().getName());
//        }catch (NullPointerException e){
//            getAlert(null, "Choice illness!", Alert.AlertType.WARNING);
//        }
//        Constant.getMapByName("misc").put("pageId", pages.get(row).getId());
        if (comboBox_Illness.getSelectionModel().getSelectedItem() != null) {
            Constant.getMapByName("dataset").put("id", comboBox_Illness.getSelectionModel().getSelectedItem().getId());
            Constant.getMapByName("dataset").put("columns", comboBox_Illness.getSelectionModel().getSelectedItem().getColumns());
            Constant.getMapByName("misc").put("pageId", pages.get(row).getId());
            windowsController.openNewModalWindow("patient/diagnosticMenu", getStage(),
                    diagnosticMenuController, "Main menu", 600, 440);
        } else {
            getAlert(null, "Please, choice illness!", Alert.AlertType.INFORMATION);
        }
    }
}
