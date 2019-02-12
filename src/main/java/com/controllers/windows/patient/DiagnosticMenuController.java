package com.controllers.windows.patient;

import com.controllers.windows.menu.MenuController;
import com.hazelcast.core.HazelcastInstance;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Admin on 06.02.2019.
 */
public class DiagnosticMenuController extends MenuController {

    @FXML
    private CheckBox checkBox_Significance;
    @FXML
    private Slider slider_Significance;
    @FXML
    private Spinner<Double> spinner_Significance;
    @FXML
    private ScrollPane scrollPane_Data;
    @FXML
    private GridPane gridPane_Data;

    private int row = 7;

    @FXML
    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        setNewWindow(newWindow);

        slider_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
        spinner_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());

        slider_Significance.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                spinner_Significance.getValueFactory().setValue(Double.parseDouble(String.valueOf(new_val)));
            }
        });

        SpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(40.00, 100.00, 80.00, 0.01);
        spinner_Significance.setValueFactory(spinnerValueFactory);
        spinner_Significance.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                slider_Significance.setValue(Double.parseDouble(String.valueOf(spinnerValueFactory.getValue())));
            }
        });

        scrollPane_Data.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_Data.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        getPlaceholderFill(row);
    }


    public void getPlaceholderFill(int row) {
//        anchorPane = new AnchorPane();
//        anchorPane.setId("test");
//        GridPane gridPane = new GridPane();
        for (int i = 0; i < row; i++) {
//            HBox hBox = new HBox();
            Label label = new Label("label treeeeeeeeeeee " + i);
            TextField textField = new TextField("234" + i);
            textField.setId("qwerty" + i);
            textField.setMaxWidth(100.0);
            gridPane_Data.add(label, 0, i);
            gridPane_Data.setHalignment(label, HPos.LEFT);
            gridPane_Data.add(textField, 1, i);
            gridPane_Data.setHalignment(textField, HPos.RIGHT);

            gridPane_Data.setMargin(label, new Insets(14, 5, 10, 14));
            gridPane_Data.setMargin(textField, new Insets(14, 14, 10, 5));
        }
//        anchorPane.setTopAnchor(gridPane, 0.0);
//        anchorPane.setLeftAnchor(gridPane, 14.0);
//        anchorPane.setRightAnchor(gridPane, 0.0);
//        anchorPane.setBottomAnchor(gridPane, 0.0);
//        anchorPane.getChildren().add(gridPane);
//        scrollPane_Data.setContent(anchorPane);
    }

    public void runDiagnostic(ActionEvent event) {
        if(checkBox_Significance.isSelected()) {
            for (int i = 0; i < row; i++) {
                TextField textField = (TextField) gridPane_Data.lookup("#qwerty" + i);
                if(Double.parseDouble(textField.getText()) >= spinner_Significance.getValue()) {
                    System.out.println(textField.getText());
                }
            }
        } else {
            for (int i = 0; i < row; i++) {
                TextField textField = (TextField) gridPane_Data.lookup("#qwerty" + i);
                System.out.println(textField.getText());
            }
        }
    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }
}

