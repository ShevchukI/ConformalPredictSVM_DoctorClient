package com.controllers.windows.menu;

import com.models.Page;
import com.models.Patient;
import com.models.Record;
import com.tools.Constant;
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
        setStage(stage);
    }

    public void initialize(ArrayList<Page> pages, int row, Stage stage, String action) throws IOException {

    }

    public void initialize(Stage stage, Stage newWindow) throws IOException {

    }

    public void initialize(Stage stage, TableView<Page> pageTableView) throws IOException{

    }

    protected void setNewWindow(Stage newWindow) {
        this.newWindow = newWindow;
    }

    protected Stage getNewWindow() {
        return newWindow;
    }

    public void initialize(Stage stage, Stage newWindow,
                           TableView<Patient> tableView_PatientTable) throws IOException {

    }

    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {

    }

    public void initialize(Stage stage, Stage newWindow, Page page) throws IOException {

    }

    public void initialize(Stage stage, Page page) throws IOException {

    }

    public void initialize(Stage stage, Stage newWindow, Patient patient, Record record){

    }

    public boolean checkStatusCode(int statusCode) {
        switch (statusCode) {
            case 200:
                return true;
            case 201:
                return true;
            case 401:
                Constant.getAlert(null, "Unauthorized: login or password incorrect!", Alert.AlertType.ERROR);
                return false;
            case 423:
                Constant.getAlert(null, "Not allow!", Alert.AlertType.ERROR);
                return false;
            case 504:
                Constant.getAlert(null, "Connection to the server is missing!", Alert.AlertType.ERROR);
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
