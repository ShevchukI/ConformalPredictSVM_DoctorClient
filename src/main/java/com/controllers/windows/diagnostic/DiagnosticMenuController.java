package com.controllers.windows.diagnostic;

//import com.controllers.requests.IllnessController;

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


public class DiagnosticMenuController extends MenuController {

    private int configurationId;
    private String[] columns;
    //    private IllnessController illnessController;
    private List<Predict> predictList;
    private ObservableList<Predict> predicts;
    private Predict predict;
    private boolean quick;
    private Page page;

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
        });
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
//        illnessController = new IllnessController();
        predictList = new ArrayList<>();
        setStage(stage);
        setNewWindow(newWindow);
        getNewWindow().setTitle("Doctor System");
        quick = true;
        button_Save.setText("Ok");
        stackPane_Table.setVisible(true);
        stackPane_Progress.setVisible(false);
        button_Save.setDisable(true);
        tableColumn_Class.setSortable(false);
        tableColumn_Class.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleClass"));
        tableColumn_Credibility.setSortable(false);
        tableColumn_Credibility.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleConfidence"));
        scrollPane_Data.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_Data.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        configurationId = HazelCastMap.getDataSetMap().get(1).getId();

        createFields(HazelCastMap.getDataSetMap().get(1).getColumns());

        predict = new Predict();

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
//        illnessController = new IllnessController();
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
        scrollPane_Data.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_Data.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        configurationId = HazelCastMap.getDataSetMap().get(1).getId();
        createFields(HazelCastMap.getDataSetMap().get(1).getColumns());
        predict = new Predict();
        button_Run.setGraphic(new ImageView(Constant.getRunIcon()));
        button_Cancel.setGraphic(new ImageView(Constant.getCancelIcon()));
        button_Save.setGraphic(new ImageView(Constant.getOkIcon()));
    }

    public void runDiagnostic(ActionEvent event) throws IOException {
        String matches = "[0-9]{1,3}";

        for (int i = 2; i < columns.length; i++) {
            TextField textField = (TextField) gridPane_Data.lookup("#parameter" + i);
            textField.setStyle(Constant.getBorderColorInherit());
        }

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
        parameterSingleObject.setSignificance(null);
        HttpResponse response = predict.startSingleTest(configurationId, parameterSingleObject);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            int processId = Integer.parseInt(Constant.responseToString(response));
            Thread calculation = new Thread(new Runnable() {
                @Override
                public void run() {
//                    predict = new Predict();
                    predict.setPredictClass(0);
                    while (predict.getPredictClass() == 0) {
                        try {
                            HttpResponse response = predict.resultSingleTest(processId);
                            setStatusCode(response.getStatusLine().getStatusCode());
                            if (getStatusCode() == 200) {
                                predict = new Predict().fromJson(response);
//                                System.out.println(predict.getRealClass() + " : " + predict.getPredictClass() + " : " + predict.getConfidence() + " Sig: " + parameterSingleObject.getSignificance());
                                if (parameterSingleObject.getSignificance() == null) {
                                    if (predict.getPredictClass() != 0) {
//                                        if (predict.getRealClass() == predict.getPredictClass()) {
                                        NumberFormat formatter = new DecimalFormat("#00.00");
                                        if(predict.getCredibility()<1){
                                            predict.setVisibleConfidence("");
                                        }else {
                                            predict.setVisibleConfidence(String.valueOf(formatter.format(predict.getConfidence() * 100)) + "%");
//                                        } else {
//                                            predict.setVisibleConfidence("");
//                                        }
                                        }
                                        predictList.clear();
                                        predictList.add(predict);
                                        predicts = FXCollections.observableArrayList(predictList);
                                        tableView_Result.setItems(predicts);
                                        stackPane_Progress.setVisible(false);
                                        tableView_Result.setOpacity(100);

                                    }
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

    private void createFields(String columns) throws IOException {
        this.columns = columns.split(",");
        for (int i = 2; i < this.columns.length; i++) {
            Label label = new Label(this.columns[i]);
            label.setId("column" + i);
            label.setMinWidth(Region.USE_COMPUTED_SIZE);
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
            Label nameResult = (Label) getStage().getScene().lookup("#label_NameResult");
            Label result = (Label) getStage().getScene().lookup("#label_Result");
            Label confidence = (Label) getStage().getScene().lookup("#label_Confidence");
            nameResult.setText(HazelCastMap.getDataSetMap().get(1).getName());
            result.setText(predict.getVisibleClass());
            confidence.setText(predict.getVisibleConfidence());
            getNewWindow().close();
        }
    }
}