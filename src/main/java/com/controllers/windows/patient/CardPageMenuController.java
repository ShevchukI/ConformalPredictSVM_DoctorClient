package com.controllers.windows.patient;

import com.controllers.requests.PageController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Page;
import com.models.Patient;
import com.tools.Encryptor;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Admin on 14.01.2019.
 */
public class CardPageMenuController extends MenuController {

    @Autowired
    CardMenuController cardMenuController;

    private WindowsController windowsController = new WindowsController();
    private MainMenuController mainMenuController = new MainMenuController();
    private DiagnosticMenuController diagnosticMenuController = new DiagnosticMenuController();
    private String action;
    private int row;
    private ArrayList<Page> pages;
    private String oldDescription;
    private String oldTheme;
    private HttpResponse response;
    private PageController pageController = new PageController();
    private Encryptor encryptor = new Encryptor();
    private LocalDate localDate = LocalDate.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private int statusCode;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private Label label_PatientName;
    @FXML
    private Label label_DoctorName;
    @FXML
    private TextField textField_Theme;
    @FXML
    private TextArea textArea_Description;
    @FXML
    private Label label_CurrentDate;
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
    private ComboBox comboBox_Combo;

    public void initialize(Patient patient, ArrayList<Page> pages, int row, Stage stage, HazelcastInstance hazelcastInstance, String action) {
        menuBarController.init(this);
        setPatient(patient);
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        this.pages = pages;
        this.row = row;
        this.action = action;
        label_PatientName.setText(getPatient().getSurname() + " " + getPatient().getName());
        textArea_Description.setWrapText(true);
        if (action.equals("view")) {
            oldTheme = textField_Theme.getText();
            oldDescription = textArea_Description.getText();
            textField_Theme.setText(pages.get(row).getTheme());
            textField_Theme.setEditable(false);
            textArea_Description.setText(pages.get(row).getDescription());
            textArea_Description.setEditable(false);
            label_CurrentDate.setText(pages.get(row).getDate());
            label_Result.setText(pages.get(row).getAnswer());
            button_Save.setDisable(true);
            comboBox_Combo.setDisable(true);
            button_Diagnostic.setDisable(true);
            label_DoctorName.setText(pages.get(row).getDoctor().getSurname() + " " + pages.get(row).getDoctor().getName());
            if (pages.get(row).getDoctor().getId() == Integer.parseInt(getMap().get("id").toString())) {
                button_Change.setDisable(false);
            } else {
                button_Change.setDisable(true);
            }
        } else if (action.equals("new")) {
            label_CurrentDate.setText(localDate.format(formatter));
            button_Previous.setDisable(true);
            button_Next.setDisable(true);
            button_Change.setDisable(true);
            label_DoctorName.setText(getMap().get("surname").toString() + " " + getMap().get("name").toString());
            BooleanBinding checkEmptyField = new BooleanBinding() {
                {
                    super.bind(textField_Theme.textProperty(),
                            textArea_Description.textProperty());
                }

                @Override
                protected boolean computeValue() {
                    return (textField_Theme.getText().isEmpty()
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
    }

    public void previousPage(ActionEvent event) {
        row--;
        oldDescription = textArea_Description.getText();
        changeText();
        textField_Theme.setText(pages.get(row).getTheme());
        textArea_Description.setText(pages.get(row).getDescription());
        label_CurrentDate.setText(pages.get(row).getDate());
        label_Result.setText(pages.get(row).getAnswer());
        if (action.equals("view")) {
            textField_Theme.setEditable(false);
            textArea_Description.setEditable(false);
        }
        if (row == 0) {
            button_Previous.setDisable(true);
        }
        if (row != pages.size() - 1) {
            button_Next.setDisable(false);
        }
        if (pages.get(row).getDoctor().getId() == Integer.parseInt(getMap().get("id").toString())) {
            button_Change.setDisable(false);
        } else {
            button_Change.setDisable(true);
        }
    }

    public void nextPage(ActionEvent event) {
        if (action.equals("new")) {
            Date date = new Date();
            label_CurrentDate.setText(localDate.format(formatter));
            textField_Theme.setText("");
            textArea_Description.setText("");
            button_Next.setDisable(true);
        } else {
            row++;
            oldDescription = textArea_Description.getText();
            changeText();
            textField_Theme.setText(pages.get(row).getTheme());
            textArea_Description.setText(pages.get(row).getDescription());
            label_CurrentDate.setText(pages.get(row).getDate());
            label_Result.setText(pages.get(row).getAnswer());
            if (action.equals("view")) {
                textField_Theme.setEditable(false);
                textArea_Description.setEditable(false);
            }
            if (row == pages.size() - 1) {
                button_Next.setDisable(true);
            }
            if (row != 0) {
                button_Previous.setDisable(false);
            }
            if (pages.get(row).getDoctor().getId() == Integer.parseInt(getMap().get("id").toString())) {
                button_Change.setDisable(false);
            } else {
                button_Change.setDisable(true);
            }
        }
    }

    public void savePage(ActionEvent event) throws IOException {
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert questionOfCancellation = new Alert(Alert.AlertType.WARNING, "Do you really want to save the changes?", ok, cancel);
        questionOfCancellation.setHeaderText(null);
        Optional<ButtonType> result = questionOfCancellation.showAndWait();
        Page page = null;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (result.orElse(cancel) == ok) {
            if (action.equals("view")) {
                page = new Page(textField_Theme.getText(), textArea_Description.getText(), pages.get(row).getParameters(), pages.get(row).getAnswer(), pages.get(row).getDate());
                response = pageController.changePage(encryptor.decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("login").toString()),
                        encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                                getMap().get("password").toString()), page, pages.get(row).getId());
                statusCode = response.getStatusLine().getStatusCode();
            } else if (action.equals("new")) {
                page = new Page(textField_Theme.getText(), textArea_Description.getText(), "placeholderParam", label_Result.getText(), label_CurrentDate.getText());
                page.setDate(label_CurrentDate.getText());
                response = pageController.createPage(encryptor.decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("login").toString()),
                        encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                                getMap().get("password").toString()), page, getPatient().getId());
                statusCode = response.getStatusLine().getStatusCode();
            }
            if (checkStatusCode(statusCode)) {
                pages.get(row).setPage(page);
                alert.setContentText("Changed!");
                alert.showAndWait();
            } else if (checkStatusCode(statusCode)) {
                alert.setContentText("Saved!");
                alert.showAndWait();
                button_Next.setDisable(false);
            }
//            else {
//                alert.setHeaderText("Status code:" + statusCode);
//                alert.setContentText("You can not save this entry!");
//                alert.showAndWait();
//                textArea_Description.setText(pages.get(row).getDescription());
//            }
        }
    }

    public void backToCardMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("patient/cardMenu.fxml", getStage(), getInstance(), cardMenuController, getPatient(), "Card", 600, 640);
    }

    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("menu/mainMenu.fxml", getStage(), getInstance(), mainMenuController, "Main menu", 600, 640);
    }

    public void changeText() {
        if (action.equals("view")) {
            if (oldDescription.equals(textArea_Description.getText()) || oldTheme.equals(textField_Theme.getText())) {
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
        textField_Theme.setEditable(true);
        textArea_Description.setEditable(true);
    }

    public void diagnostic(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("patient/diagnosticMenu.fxml", getStage(), getInstance(), diagnosticMenuController,
                "Main menu", 600, 400);

    }
}
