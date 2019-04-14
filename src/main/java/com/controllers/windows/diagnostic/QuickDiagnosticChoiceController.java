package com.controllers.windows.diagnostic;

import com.controllers.requests.IllnessController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Dataset;
import com.tools.Constant;
import com.tools.HazelCastMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class QuickDiagnosticChoiceController extends MenuController {
    @Autowired
    HttpResponse response;

//    private DiagnosticMenuController diagnosticMenuController;
//    private IllnessController illnessController;
//    private int statusCode;
    private ObservableList<Dataset> datasets;
    private WindowsController windowsController;

    @FXML
    private ComboBox<Dataset> comboBox_Illness;
    @FXML
    private Button button_Ok;
    @FXML
    private Button button_Cancel;

    public void initialize(Stage stage, Stage newWindow) throws IOException {
        setStage(stage);
        setNewWindow(newWindow);
//        diagnosticMenuController = new DiagnosticMenuController();
//        illnessController = new IllnessController();
        datasets = FXCollections.observableArrayList();
        windowsController = new WindowsController();
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
    }

    public void cancel(ActionEvent event){
        getNewWindow().close();
    }

    public void ok(ActionEvent event) throws IOException {
        if (comboBox_Illness.getSelectionModel().getSelectedItem() != null) {
//            HazelCastMap.getMapByName(HazelCastMap.getDatasetMapName()).put("id", comboBox_Illness.getSelectionModel().getSelectedItem().getId());
//            HazelCastMap.getMapByName(HazelCastMap.getDatasetMapName()).put("columns", comboBox_Illness.getSelectionModel().getSelectedItem().getColumns());
            Dataset dataset = new Dataset(comboBox_Illness.getSelectionModel().getSelectedItem().getId(), comboBox_Illness.getSelectionModel().getSelectedItem().getColumns());
            HazelCastMap.getDataSetMap().put(1, dataset);
            windowsController.openNewModalWindow(Constant.getDiagnosticMenuRoot(), getStage(),
                    new DiagnosticMenuController(), "Main menu", true, 600, 440);
            getNewWindow().close();
        } else {
            getAlert(null, "Please, choice illness!", Alert.AlertType.INFORMATION);
        }
    }
}
