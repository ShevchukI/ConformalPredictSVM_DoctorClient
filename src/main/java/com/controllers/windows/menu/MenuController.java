package com.controllers.windows.menu;

import com.models.Page;
import com.models.Patient;
import com.tools.HazelCastMap;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Admin on 10.01.2019.
 */
public abstract class MenuController {

    private Stage stage;
    private Stage newWindow;
    private int statusCode;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void initialize(Stage stage) throws IOException {
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
    }

    public void initialize(ArrayList<Page> pages, int row, Stage stage, String action) throws IOException {

    }

    public void initialize(Stage stage, Stage newWindow) throws IOException {

    }

    public void setNewWindow(Stage newWindow) {
        this.newWindow = newWindow;
    }

    public Stage getNewWindow() {
        return newWindow;
    }

    public void initialize(Stage stage, Stage newWindow,
                           TableView<Patient> tableView_PatientTable) throws IOException {

    }

    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {

    }

    public boolean checkStatusCode(int statusCode) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        switch (statusCode) {
            case 200:
                return true;
            case 201:
                return true;
            case 401:
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Unauthorized: login or password incorrect!");
                alert.setContentText("Error code: " + statusCode);
                alert.showAndWait();
                return false;
            case 504:
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Connection to the server is missing!");
                alert.setContentText("Error code: " + statusCode);
                alert.showAndWait();
                return false;
            default:
                return false;
        }
    }

    public void getAlert(String header, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public boolean questionOkCancel(String questionText){
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert questionOfCancellation = new Alert(Alert.AlertType.WARNING, questionText, ok, cancel);
        questionOfCancellation.setHeaderText(null);
        Optional<ButtonType> result = questionOfCancellation.showAndWait();
        if(result.orElse(cancel) == ok){
            return true;
        } else {
            return false;
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
