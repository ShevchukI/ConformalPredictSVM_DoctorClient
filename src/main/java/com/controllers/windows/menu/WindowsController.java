package com.controllers.windows.menu;

import com.controllers.windows.doctor.LoginMenuController;
import com.models.Page;
import com.models.Patient;
import com.tools.Constant;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Admin on 10.01.2019.
 */
public class WindowsController {

    private Dimension sSize;
    private Rectangle2D screenBounds;

    public WindowsController(){
        sSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenBounds = Screen.getPrimary().getVisualBounds();
    }

    public void start(Stage stage) throws IOException {
//        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        Constant.createInstanceAndMap();
        FXMLLoader loginMenuLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/doctor/loginMenu.fxml"));
        Pane loginMenuPane = (Pane) loginMenuLoader.load();
        Scene loginMenuScene = new Scene(loginMenuPane);
        stage.setScene(loginMenuScene);
        stage.setResizable(false);
        stage.setTitle("Login menu");
        stage.getIcons().add(new Image("img/icons/icon.png"));
        LoginMenuController loginMenuController = (LoginMenuController) loginMenuLoader.getController();
        loginMenuController.initialize(stage);
        stage.show();
    }

//    public void openWindowResizable(String rootName, Stage stage, MenuController controller,
//                                    Patient patient, String title, int minWidth, int minHeight) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName + ".fxml"));
//        Pane pane = (Pane) loader.load();
//        Scene scene = new Scene(pane);
//        stage.setScene(scene);
//        stage.setMinWidth(minWidth);
//        stage.setMinHeight(minHeight);
//        stage.setMaxWidth(sSize.getWidth());
//        stage.setMaxHeight(sSize.getHeight());
//        stage.setResizable(true);
//        stage.setTitle(title);
//        controller = (MenuController) loader.getController();
//        controller.initialize(stage);
//        stage.show();
//    }

//    public void openWindow(String rootName, Stage stage, MenuController controller,
//                           String title, int width, int height) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName + ".fxml"));
//        Pane pane = (Pane) loader.load();
//        Scene scene = new Scene(pane);
//        stage.setScene(scene);
//        stage.setMinWidth(width);
//        stage.setMinHeight(height);
//        stage.setMaxWidth(width);
//        stage.setMaxHeight(height);
//        stage.setResizable(false);
//        stage.setTitle(title);
//        stage.getIcons().add(new Image("img/icons/icon.png"));
//        controller = (MenuController) loader.getController();
//        controller.initialize(stage);
//        if (rootName.equals("loginMenu.fxml")) {
//            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
//            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
//        }
//        stage.show();
//    }
public void openWindow(String rootName, Stage stage, MenuController controller, String title, int width, int height) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName + ".fxml"));
    Pane pane = (Pane) loader.load();
    Scene scene = new Scene(pane);
    stage.setScene(scene);
    stage.setMinWidth(width);
    stage.setMinHeight(height);
    stage.setWidth(stage.getWidth());
    stage.setHeight(stage.getHeight());
    stage.setMaxWidth(width);
    stage.setMaxHeight(height);
    stage.setResizable(false);
    stage.setTitle(title);
    stage.getIcons().add(new Image("img/icons/icon.png"));
    controller = (MenuController) loader.getController();
    controller.initialize(stage);
    if (rootName.equals("doctor/loginMenu")) {
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }
    stage.show();
}
    public void openWindowResizable(String rootName, Stage stage, MenuController controller,
                                    String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName + ".fxml"));
        Pane pane = (Pane) loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        stage.setWidth(stage.getWidth());
        stage.setHeight(stage.getHeight());
        stage.setMaxWidth(sSize.getWidth());
        stage.setMaxHeight(sSize.getHeight());
        stage.setResizable(true);
        stage.setTitle(title);
        controller = (MenuController) loader.getController();
        controller.initialize(stage);
        stage.show();
    }

    public void openWindowResizable(String rootName, Stage stage, MenuController controller,
                                    ArrayList<Page> pages, int row, String action,
                                    String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName + ".fxml"));
        Pane pane = (Pane) loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        stage.setMaxWidth(sSize.getWidth());
        stage.setMaxHeight(sSize.getHeight());
        stage.setResizable(true);
        stage.setTitle(title);
        controller = (MenuController) loader.getController();
        controller.initialize(pages, row, stage, action);
        stage.show();
    }


    public void openNewModalWindow(String rootName, Stage stage, MenuController controller,
                                   ObservableList<Patient> patientObservableList,
                                   TableView<Patient> tableView_PatientTable,
                                   String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName + ".fxml"));
        Pane pane = (Pane) loader.load();
        Stage newWindow = new Stage();
        Scene scene = new Scene(pane);
        newWindow.setScene(scene);
        newWindow.setMinWidth(minWidth);
        newWindow.setMinHeight(minHeight);
        newWindow.setMaxWidth(sSize.getWidth());
        newWindow.setMaxHeight(sSize.getHeight());
        newWindow.setResizable(false);
        newWindow.setTitle(title);
        newWindow.getIcons().add(new Image("img/icons/icon.png"));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        controller = (MenuController) loader.getController();
        controller.initialize(stage, newWindow, patientObservableList, tableView_PatientTable);
        newWindow.show();
    }

    public void openNewModalWindow(String rootName, Stage stage, MenuController controller,
                                   String title, boolean change, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName + ".fxml"));
        Pane pane = (Pane) loader.load();
        Stage newWindow = new Stage();
        Scene scene = new Scene(pane);
        newWindow.setScene(scene);
        newWindow.setMinWidth(minWidth);
        newWindow.setMinHeight(minHeight);
        newWindow.setMaxWidth(sSize.getWidth());
        newWindow.setMaxHeight(sSize.getHeight());
        newWindow.setResizable(false);
        newWindow.setTitle(title);
        newWindow.getIcons().add(new Image("img/icons/icon.png"));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        controller = (MenuController) loader.getController();
        controller.initialize(stage, newWindow, change);
        newWindow.show();
    }

    public void openNewModalWindow(String rootName, Stage stage, MenuController controller,
                                   String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName + ".fxml"));
        Pane pane = (Pane) loader.load();
        Stage newWindow = new Stage();
        Scene scene = new Scene(pane);
        newWindow.setScene(scene);
        newWindow.setMinWidth(minWidth);
        newWindow.setMinHeight(minHeight);
        newWindow.setMaxWidth(minWidth);
        newWindow.setMaxHeight(minHeight);

//        newWindow.setMaxWidth(sSize.getWidth());
//        newWindow.setMaxHeight(sSize.getHeight());
        newWindow.setResizable(false);
        newWindow.setTitle(title);
        newWindow.getIcons().add(new Image("img/icons/icon.png"));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        controller = (MenuController) loader.getController();
        controller.initialize(stage, newWindow);
        newWindow.show();
    }
}
