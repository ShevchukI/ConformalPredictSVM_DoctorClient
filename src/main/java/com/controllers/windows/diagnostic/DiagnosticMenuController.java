package com.controllers.windows.diagnostic;

import com.controllers.requests.IllnessController;
import com.controllers.requests.PageController;
import com.controllers.windows.menu.MenuController;
import com.models.Dataset;
import com.models.Page;
import com.models.ParameterSingleObject;
import com.models.Predict;
import com.tools.Constant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 06.02.2019.
 */
public class DiagnosticMenuController extends MenuController {

    @Autowired
    HttpResponse response;

    private Dataset dataset;
    //    private ConfigurationEntity configurationEntity;
    private int statusCode;
    private int datasetId;
    private int configurationId;
    private String[] columns;
    //    private DataSetController dataSetController = new DataSetController();
//    private ConfigurationController configurationController = new ConfigurationController();
    private IllnessController illnessController;
    private List<Predict> predictList;
    private ObservableList<Predict> predicts;
    private PageController pageController;
    private Predict predict;
    private boolean quick;

    @FXML
    private CheckBox checkBox_Significance;
    @FXML
    private Slider slider_Significance;
    @FXML
    private TextField textField_Significance;
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

    @FXML
    public void initialize(Stage stage, Stage newWindow, boolean quick) throws IOException {
        newWindow.setOnHidden(event -> {
            Constant.getMapByName(Constant.getDatasetMapName()).remove("id");
            Constant.getMapByName(Constant.getDatasetMapName()).remove("columns");
            Constant.getMapByName(Constant.getDatasetMapName()).remove("name");
            Constant.getMapByName(Constant.getMiscellaneousMapName()).remove("pageId");
        });
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        illnessController = new IllnessController();
        predictList = new ArrayList<>();
        pageController = new PageController();
        setStage(stage);
        setNewWindow(newWindow);
        this.quick = quick;
        if(quick){
            button_Save.setText("Ok");
        } else {
            button_Save.setText("Save");
        }
        stackPane_Table.setVisible(true);
        stackPane_Progress.setVisible(false);
        button_Save.setDisable(true);
        tableColumn_Class.setSortable(false);
        tableColumn_Class.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleClass"));
        tableColumn_Credibility.setSortable(false);
        tableColumn_Credibility.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleConfidence"));
        NumberFormat formatter = new DecimalFormat("#0.00");
        slider_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
        textField_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
        slider_Significance.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                textField_Significance.setText(String.valueOf(formatter.format(Double.parseDouble(String.valueOf(new_val))).replace(",", ".")));
            }
        });
        textField_Significance.setText(String.valueOf(formatter.format(Double.parseDouble(String.valueOf(slider_Significance.getValue()))).replace(",", ".")));
        scrollPane_Data.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_Data.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        configurationId = Integer.parseInt(Constant.getMapByName(Constant.getDatasetMapName()).get("id").toString());
        createFields(Constant.getMapByName(Constant.getDatasetMapName()).get("columns").toString());
        button_Run.setGraphic(new ImageView("/img/icons/run.png"));
        button_Cancel.setGraphic(new ImageView("/img/icons/cancel.png"));
        button_Save.setGraphic(new ImageView("/img/icons/ok.png"));
    }


    public void runDiagnostic(ActionEvent event) throws IOException {
//        stackPane_Table.setVisible(false);
//        stackPane_Progress.setVisible(true);
        tableView_Result.setOpacity(0);
        stackPane_Progress.setVisible(true);
        button_Run.setDisable(true);
        ParameterSingleObject parameterSingleObject = new ParameterSingleObject("", 0);
        for (int i = 2; i < columns.length; i++) {
            TextField textField = (TextField) gridPane_Data.lookup("#parameter" + i);
            if (!textField.getText().equals("")) {
                parameterSingleObject.setParams(parameterSingleObject.getParams() + textField.getText());
            } else {
                parameterSingleObject.setParams(parameterSingleObject.getParams() + 0);
            }
            if (i != columns.length - 1) {
                parameterSingleObject.setParams(parameterSingleObject.getParams() + ",");
            }

        }
        if (checkBox_Significance.isSelected()) {
            parameterSingleObject.setSignificance((100 - Double.parseDouble(textField_Significance.getText())) / 100);
        } else {
            parameterSingleObject.setSignificance(null);
        }
        System.out.println(parameterSingleObject.toString());
        response = illnessController.startSingleTest(Constant.getAuth(), configurationId, parameterSingleObject);
        statusCode = response.getStatusLine().getStatusCode();
        System.out.println("First request: " + statusCode);
        if (checkStatusCode(statusCode)) {
            int processId = Integer.parseInt(Constant.responseToString(response));
            Thread calculation = new Thread(new Runnable() {
                @Override
                public void run() {
//                    button_Run.setDisable(true);
                    predict = new Predict();
                    predict.setPredictClass(0);
//                    double progress = 0;
                    while (predict.getPredictClass() == 0) {
                        try {
                            response = illnessController.resultSingleTest(Constant.getAuth(), processId);
                            statusCode = response.getStatusLine().getStatusCode();
                            System.out.println("Second request: " + statusCode);
                            if (statusCode == 200) {
                                predict = new Predict().fromJson(response);
                                System.out.println(predict.getRealClass() + " : " + predict.getPredictClass() + " : " + predict.getCredibility());
                                if (predict.getPredictClass() != 0) {
                                    if (predict.getRealClass() == predict.getPredictClass() || predict.getRealClass() == 0) {
                                        switch (predict.getPredictClass()) {
                                            case 1:
                                                predict.setVisibleClass("Positive");
                                                break;
                                            case -1:
                                                predict.setVisibleClass("Negative");
                                                break;
                                            default:
                                                break;
                                        }
//                                        predict.setVisibleClass(String.valueOf(predict.getPredictClass()));
//                                        predict.setVisibleCredibility(String.valueOf(predict.getCredibility() * 100) + "%");
                                        predict.setVisibleConfidence(String.valueOf(predict.getConfidence() * 100) + "%");
                                    } else {
                                        predict.setVisibleClass("Uncertain");
                                        predict.setVisibleConfidence("");
                                    }
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
//                        button_Run.setDisable(false);
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


    public void setSignificanceValue(ActionEvent event) {
        setSignificanceValue();
    }

    public void setSignificanceValue() {
        slider_Significance.setValue(Double.parseDouble(textField_Significance.getText()));
    }

    private void createFields(String columns) throws IOException {
        this.columns = columns.split(",");
        for (int i = 2; i < this.columns.length; i++) {
            Tooltip tooltip = new Tooltip(this.columns[i]);
            Label label = new Label(this.columns[i]);
            label.setId("column" + i);
            label.setMinWidth(Region.USE_COMPUTED_SIZE);
            label.setTooltip(tooltip);
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
        if (!quick) {
            response = pageController.getPage(Constant.getAuth(),
                    Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("pageId").toString()));
            statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode + "_1");
            if (checkStatusCode(statusCode)) {
                Page page = new Page().fromResponse(response);
                page.setParameters("");
                for (int i = 2; i < this.columns.length; i++) {
                    Label label = (Label) gridPane_Data.lookup("#column" + i);
                    TextField textField = (TextField) gridPane_Data.lookup("#parameter" + i);
                    page.setParameters(page.getParameters() + label.getText() + ":" + textField.getText());
                    if (i < columns.length - 1) {
                        page.setParameters(page.getParameters() + ",");
                    }
                }
                page.setAnswer(Constant.getMapByName(Constant.getDatasetMapName()).get("name").toString() + ":" + predict.getVisibleClass());
                response = pageController.changePage(Constant.getAuth(), page, page.getId());
                statusCode = response.getStatusLine().getStatusCode();
                System.out.println(statusCode + "_2");
                if (checkStatusCode(statusCode)) {
                    Label label = (Label) getStage().getScene().lookup("#label_NameResult");
                    Label label1 = (Label) getStage().getScene().lookup("#label_Result");
                    label.setText(Constant.getMapByName(Constant.getDatasetMapName()).get("name").toString());
                    label1.setText(predict.getVisibleClass());
                }
            }
        } else {
            getNewWindow().close();
        }
    }
}

