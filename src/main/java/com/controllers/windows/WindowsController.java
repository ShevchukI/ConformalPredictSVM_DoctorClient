package com.controllers.windows;

import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.models.Page;
import com.models.Patient;
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

    private Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();


    public void openWindow(String rootName, Stage stage, MenuController controller, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
        Pane pane = (Pane) loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setMaxWidth(width);
        stage.setMaxHeight(height);
        stage.setResizable(false);
        stage.setTitle(title);
        controller = (MenuController) loader.getController();
        controller.setStage(stage);

        if (rootName.equals("loginMenu.fxml")) {
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
        stage.show();


    }

    public void openWindowResizable(String rootName, Stage stage, MenuController controller, Doctor doctor, String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
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
        controller.setStage(stage);
        controller.initialize(doctor);
        stage.show();

    }

    public void openWindow(String rootName, Stage stage, HazelcastInstance instance, MenuController controller, Doctor doctor, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
        Pane pane = (Pane) loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setMaxWidth(width);
        stage.setMaxHeight(height);
        stage.setResizable(false);
        stage.setTitle(title);
        controller = (MenuController) loader.getController();
        controller.initialize(doctor, stage, instance);
        if (rootName.equals("loginMenu.fxml")) {
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
        stage.show();

    }

//    public void openWindowResizable(String rootName, Stage stage, HazelcastInstance instance, MenuController controller, Doctor doctor, String title, int minWidth, int minHeight) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
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
//        controller.initialize(doctor, stage, instance);
//        controller.setStage(stage);
//        controller.initialize(doctor);
//        controller.setInstance(instance);
//        stage.show();
//
//    }

    public void openWindowResizable(String rootName, Stage stage, HazelcastInstance instance, MenuController controller, Patient patient, String title, int minWidth, int minHeight) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
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
        controller.initialize(patient, stage, instance);
        stage.show();
    }

    public void openWindow(String rootName, Stage stage, HazelcastInstance instance, MenuController controller, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
        Pane pane = (Pane) loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setMaxWidth(width);
        stage.setMaxHeight(height);
        stage.setResizable(false);
        stage.setTitle(title);
        stage.getIcons().add(new Image("img/icons/icon.png"));
        controller = (MenuController) loader.getController();
        controller.initialize(stage, instance);
        if (rootName.equals("loginMenu.fxml")) {
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
        stage.show();

    }

    public void openWindowResizable(String rootName, Stage stage, HazelcastInstance instance, MenuController controller, String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
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
        controller.initialize(stage, instance);

        stage.show();
    }
///////////
    public void openWindowResizable(String rootName, Stage stage, HazelcastInstance instance, MenuController controller, int q, String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
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
        controller.initialize(stage, instance, (MainMenuController)controller);

        stage.show();
    }

///////
    public void openWindowResizable(String rootName, Stage stage, HazelcastInstance instance, MenuController controller, Patient patient, ArrayList<Page> pages, int row, String action, String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
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
        controller.initialize(patient, pages, row, stage, instance, action);
        stage.show();
    }

    public void openNewModalWindow(String rootName, Stage stage, HazelcastInstance instance, MenuController controller,
                                   String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
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
        controller.initialize(stage, instance, newWindow);
        newWindow.show();
    }

    public void openNewModalWindow(String rootName, Stage stage, HazelcastInstance instance, MenuController controller,
                                   ObservableList<Patient> patientObservableList,
                                   TableView<Patient> tableView_PatientTable, String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
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
        controller.initialize(stage, instance, newWindow, patientObservableList, tableView_PatientTable);
        newWindow.show();

    }

//    public void openNewModalWindow(String rootName, Stage stage, HazelcastInstance instance, MenuController controller,
//                                   FXMLLoader controller1,
//                                   String title, int minWidth, int minHeight) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/" + rootName));
//        Pane pane = (Pane) loader.load();
//        Stage newWindow = new Stage();
//        Scene scene = new Scene(pane);
//        newWindow.setScene(scene);
//        newWindow.setMinWidth(minWidth);
//        newWindow.setMinHeight(minHeight);
//        newWindow.setMaxWidth(sSize.getWidth());
//        newWindow.setMaxHeight(sSize.getHeight());
//        newWindow.setResizable(false);
//        newWindow.setTitle(title);
//        newWindow.getIcons().add(new Image("img/icons/icon.png"));
//        newWindow.initModality(Modality.WINDOW_MODAL);
//        newWindow.initOwner(stage);
//        controller = (MenuController) loader.getController();
//        controller.initialize(stage, instance, newWindow, controller1);
//        newWindow.show();
//    }

}
