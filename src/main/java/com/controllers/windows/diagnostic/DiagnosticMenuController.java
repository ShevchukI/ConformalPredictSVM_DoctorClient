package com.controllers.windows.diagnostic;

import com.controllers.requests.IllnessController;
import com.controllers.windows.menu.MenuController;
import com.models.Page;
import com.models.ParameterSingleObject;
import com.models.Predict;
import com.tools.Constant;
import com.tools.HazelCastMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 06.02.2019.
 */
public class DiagnosticMenuController extends MenuController {

    private int configurationId;
    private String[] columns;
    private IllnessController illnessController;
    private List<Predict> predictList;
    private ObservableList<Predict> predicts;
    private Predict predict;
    private boolean quick;
    private Page page;

    //    @FXML
//    private CheckBox checkBox_Significance;
//    @FXML
//    private Slider slider_Significance;
//    @FXML
//    private TextField textField_Significance;
    @FXML
    private ScrollPane scrollPane_Data;
    @FXML
    private GridPane gridPane_Data;
    @FXML
    private TableView<Predict> tableView_Result;
    @FXML
    private TableColumn tableColumn_Class;
    @FXML
    private TableColumn tableColumn_Credibility;
    @FXML
    private StackPane stackPane_Table;
    @FXML
    private StackPane stackPane_Progress;
    @FXML
    private Button button_Run;
    @FXML
    private Button button_Save;
    @FXML
    private Button button_Cancel;


    public void initialize(Stage stage, Stage newWindow) throws IOException {
        newWindow.setOnHidden(event -> {
            HazelCastMap.getDataSetMap().clear();
//            HazelCastMap.getMiscellaneousMap().remove("pageId");
        });
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        illnessController = new IllnessController();
        predictList = new ArrayList<>();
        setStage(stage);
        setNewWindow(newWindow);
        getNewWindow().setTitle("Doctor System");
//        this.quick = quick;
        quick = true;
        button_Save.setText("Ok");
//        if (quick) {
//            button_Save.setText("Ok");
//        } else {
//            button_Save.setText("Save");
//        }
        stackPane_Table.setVisible(true);
        stackPane_Progress.setVisible(false);
        button_Save.setDisable(true);
        tableColumn_Class.setSortable(false);
        tableColumn_Class.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleClass"));
        tableColumn_Credibility.setSortable(false);
        tableColumn_Credibility.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleConfidence"));
        NumberFormat formatter = new DecimalFormat("#0.00");
//        slider_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
//        textField_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
//        slider_Significance.valueProperty().addListener(new ChangeListener<Number>() {
//            public void changed(ObservableValue<? extends Number> ov,
//                                Number old_val, Number new_val) {
//                textField_Significance.setText(String.valueOf(formatter.format(Double.parseDouble(String.valueOf(new_val))).replace(",", ".")));
//            }
//        });
//        textField_Significance.setText(String.valueOf(formatter.format(Double.parseDouble(String.valueOf(slider_Significance.getValue()))).replace(",", ".")));
        scrollPane_Data.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_Data.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        configurationId = HazelCastMap.getDataSetMap().get(1).getId();

        createFields(HazelCastMap.getDataSetMap().get(1).getColumns());
        button_Run.setGraphic(new ImageView(Constant.getRunIcon()));
        button_Cancel.setGraphic(new ImageView(Constant.getCancelIcon()));
        button_Save.setGraphic(new ImageView(Constant.getOkIcon()));
    }


    public void initialize(Stage stage, Stage newWindow, Page page) throws IOException {
        newWindow.setOnHidden(event -> {
            HazelCastMap.getDataSetMap().clear();
            HazelCastMap.getMiscellaneousMap().remove("pageId");
        });
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        this.page = page;
        illnessController = new IllnessController();
        predictList = new ArrayList<>();
        setStage(stage);
        setNewWindow(newWindow);
        quick = false;
        button_Save.setText("Save");
        stackPane_Table.setVisible(true);
        stackPane_Progress.setVisible(false);
        button_Save.setDisable(true);
        tableColumn_Class.setSortable(false);
        tableColumn_Class.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleClass"));
        tableColumn_Credibility.setSortable(false);
        tableColumn_Credibility.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleConfidence"));
        NumberFormat formatter = new DecimalFormat("#0.00");
//        slider_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
//        textField_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
//        slider_Significance.valueProperty().addListener(new ChangeListener<Number>() {
//            public void changed(ObservableValue<? extends Number> ov,
//                                Number old_val, Number new_val) {
//                textField_Significance.setText(String.valueOf(formatter.format(Double.parseDouble(String.valueOf(new_val))).replace(",", ".")));
//            }
//        });
//        textField_Significance.setText(String.valueOf(formatter.format(Double.parseDouble(String.valueOf(slider_Significance.getValue()))).replace(",", ".")));
        scrollPane_Data.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_Data.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        configurationId = HazelCastMap.getDataSetMap().get(1).getId();
        createFields(HazelCastMap.getDataSetMap().get(1).getColumns());
        button_Run.setGraphic(new ImageView(Constant.getRunIcon()));
        button_Cancel.setGraphic(new ImageView(Constant.getCancelIcon()));
        button_Save.setGraphic(new ImageView(Constant.getOkIcon()));
    }

    public void runDiagnostic(ActionEvent event) throws IOException {
        String matches = "[0-9]{1,3}";


        ParameterSingleObject parameterSingleObject = new ParameterSingleObject("", 0);
        parameterSingleObject.setParams("");
        for (int i = 2; i < columns.length; i++) {
            TextField textField = (TextField) gridPane_Data.lookup("#parameter" + i);
            if (!textField.getText().matches(matches)) {
                textField.setStyle(Constant.getBorderColorRed());
            } else {
                textField.setStyle(Constant.getBorderColorInherit());
            }
            parameterSingleObject.setParams(parameterSingleObject.getParams() + textField.getText());
//            if (!textField.getText().equals("")) {
//                parameterSingleObject.setParams(parameterSingleObject.getParams() + textField.getText());
//            } else {
//                parameterSingleObject.setParams(parameterSingleObject.getParams() + 0);
//            }
            if (i != columns.length - 1) {
                parameterSingleObject.setParams(parameterSingleObject.getParams() + ",");
            }
            if (textField.getStyle().equals(Constant.getBorderColorRed())) {
                return;
            }
        }
        tableView_Result.setOpacity(0);
        stackPane_Progress.setVisible(true);
        button_Run.setDisable(true);
//        if (checkBox_Significance.isSelected()) {
//            parameterSingleObject.setSignificance((100 - Double.parseDouble(textField_Significance.getText())) / 100);
//        } else {
//            parameterSingleObject.setSignificance(null);
//        }
        parameterSingleObject.setSignificance(0.2);
        HttpResponse response = illnessController.startSingleTest(configurationId, parameterSingleObject);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            int processId = Integer.parseInt(Constant.responseToString(response));
            Thread calculation = new Thread(new Runnable() {
                @Override
                public void run() {
                    predict = new Predict();
                    predict.setPredictClass(0);
                    while (predict.getPredictClass() == 0) {
                        try {
                            HttpResponse response = illnessController.resultSingleTest(processId);
                            setStatusCode(response.getStatusLine().getStatusCode());
                            if (getStatusCode() == 200) {
                                predict = new Predict().fromJson(response);
                                System.out.println(predict.getRealClass() + " : " + predict.getPredictClass() + " : " + predict.getConfidence() + " Sig: " + parameterSingleObject.getSignificance());
                                if (predict.getPredictClass() != 0) {
                                    if (predict.getRealClass() == predict.getPredictClass()) {
                                        NumberFormat formatter = new DecimalFormat("#00.00");
                                        predict.setVisibleConfidence(String.valueOf(formatter.format(predict.getConfidence() * 100)) + "%");

//                                        switch (predict.getPredictClass()) {
//                                            case 1:
//                                                predict.setVisibleClass("Positive");
//                                                predict.setVisibleConfidence(String.valueOf(formatter.format(predict.getConfidence() * 100)) + "%");
//                                                break;
//                                            case -1:
//                                                predict.setVisibleClass("Negative");
//                                                predict.setVisibleConfidence(String.valueOf(formatter.format(predict.getConfidence() * 100)) + "%");
//                                                break;
//                                            default:
//                                                predict.setVisibleClass("Uncertain");
//                                                predict.setVisibleConfidence("");
//                                                break;
//                                        }
                                    } else {
//                                        predict.setVisibleClass("Uncertain");
                                        predict.setVisibleConfidence("");
                                    }
//                                    if (predict.getRealClass() == predict.getPredictClass() || predict.getRealClass() == 0) {
////                                        switch (predict.getPredictClass()) {
////                                            case 1:
////                                                predict.setVisibleClass("Positive");
////                                                break;
////                                            case -1:
////                                                predict.setVisibleClass("Negative");
////                                                break;
////                                            default:
////                                                break;
////                                        }
//                                        NumberFormat formatter = new DecimalFormat("#00.00");
////                                        predict.setVisibleConfidence(String.valueOf(predict.getConfidence() * 100) + "%");
//                                        predict.setVisibleConfidence(String.valueOf(formatter.format(predict.getConfidence() * 100)) + "%");
//                                    } else {
//                                        if (predict.getVisibleClass().equals("Uncertain")) {
////                                        predict.setVisibleClass("Uncertain");
//                                            predict.setVisibleConfidence("");
//                                        }
//                                    }
                                    predictList.clear();
                                    predictList.add(predict);
                                    predicts = FXCollections.observableArrayList(predictList);
                                    tableView_Result.setItems(predicts);
//                                    stackPane_Table.setVisible(true);
                                    stackPane_Progress.setVisible(false);
                                    tableView_Result.setOpacity(100);

                                }
                                Thread.sleep(1000 * 1);
                            } else {
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    button_Run.setDisable(false);
                    button_Save.setDisable(false);
                }
            });
            calculation.start();
        }
    }


    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }


//    public void setSignificanceValue(ActionEvent event) {
//        setSignificanceValue();
//    }

//    public void setSignificanceValue() {
//        slider_Significance.setValue(Double.parseDouble(textField_Significance.getText()));
//    }

    private void createFields(String columns) throws IOException {
        this.columns = columns.split(",");
        for (int i = 2; i < this.columns.length; i++) {
//            Tooltip tooltip = new Tooltip(this.columns[i]);
            Label label = new Label(this.columns[i]);
            label.setId("column" + i);
            label.setMinWidth(Region.USE_COMPUTED_SIZE);
//            label.setTooltip(tooltip);
            TextField textField = new TextField();
            textField.setId("parameter" + i);
            textField.setMinWidth(100.0);
            textField.setMaxWidth(100.0);
            gridPane_Data.add(label, 0, i);
            gridPane_Data.setHalignment(label, HPos.LEFT);
            gridPane_Data.add(textField, 1, i);
            gridPane_Data.setHalignment(textField, HPos.RIGHT);
            gridPane_Data.setMargin(label, new Insets(14, 10, 10, 14));
            gridPane_Data.setMargin(textField, new Insets(14, 14, 10, 0));
        }
    }

    public void save(ActionEvent event) throws IOException {
        if (quick) {
            getNewWindow().close();
        } else {
//            HttpResponse response = PageController.getPage(HazelCastMap.getMiscellaneousMap().get("pageId"));
//            setStatusCode(response.getStatusLine().getStatusCode());
//            if (checkStatusCode(getStatusCode())) {
//                Page page = new Page().fromResponse(response);
//                page.setParameters("");
            page.setParameters("");
            for (int i = 2; i < this.columns.length; i++) {
                Label label = (Label) gridPane_Data.lookup("#column" + i);
                TextField textField = (TextField) gridPane_Data.lookup("#parameter" + i);
                page.setParameters(page.getParameters() + label.getText() + ":" + textField.getText());
                if (i < columns.length - 1) {
                    page.setParameters(page.getParameters() + ";");
                }
            }
            page.setAnswer(HazelCastMap.getDataSetMap().get(1).getName() + ":" + predict.getVisibleClass() + ":" + predict.getVisibleConfidence());
//            HttpResponse response = PageController.changePage(page, page.getId());
//            setStatusCode(response.getStatusLine().getStatusCode());
//            if (checkStatusCode(getStatusCode())) {
            Label nameResult = (Label) getStage().getScene().lookup("#label_NameResult");
            Label result = (Label) getStage().getScene().lookup("#label_Result");
            Label confidence = (Label) getStage().getScene().lookup("#label_Confidence");
            nameResult.setText(HazelCastMap.getDataSetMap().get(1).getName());
            result.setText(predict.getVisibleClass());
            confidence.setText(predict.getVisibleConfidence());
            getNewWindow().close();
//            }
//            }
        }
    }
}

