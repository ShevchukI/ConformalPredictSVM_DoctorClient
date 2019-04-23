package com.controllers.windows.menu;

import com.controllers.windows.doctor.LoginMenuController;
import com.models.Page;
import com.models.Patient;
import com.tools.Constant;
import com.tools.HazelCastMap;
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

    public WindowsController() {
        sSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenBounds = Screen.getPrimary().getVisualBounds();
    }

    public void start(Stage stage) throws IOException {
        HazelCastMap.createInstanceAndMap();
        openWindow(Constant.getLoginMenuRoot(), stage, new LoginMenuController(),
                "Doctor client", false, 341, 236);
    }

    public void openWindowResizable(String rootName, Stage stage, MenuController controller,
                                    String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootName));
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
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootName));
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
                                   TableView<Patient> tableView_PatientTable,
                                   String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootName));
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
        controller.initialize(stage, newWindow, tableView_PatientTable);
        newWindow.show();
    }

    public void openNewModalWindow(String rootName, Stage stage, MenuController controller,
                                   String title, boolean change, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootName));
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
        newWindow.getIcons().add(new Image(Constant.getApplicationIcon()));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        controller = (MenuController) loader.getController();
        controller.initialize(stage, newWindow, change);
        newWindow.show();
    }

    public void openNewModalWindow(String rootName, Stage stage, MenuController controller,
                                   String title, int minWidth, int minHeight) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootName));
        Pane pane = (Pane) loader.load();
        Stage newWindow = new Stage();
        Scene scene = new Scene(pane);
        newWindow.setScene(scene);
        newWindow.setMinWidth(minWidth);
        newWindow.setMinHeight(minHeight);
        newWindow.setMaxWidth(minWidth);
        newWindow.setMaxHeight(minHeight);
        newWindow.setResizable(false);
        newWindow.setTitle(title);
        newWindow.getIcons().add(new Image(Constant.getApplicationIcon()));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        controller = (MenuController) loader.getController();
        controller.initialize(stage, newWindow);
        newWindow.show();
    }


    public void openWindow(String rootName, Stage stage, MenuController controller, String title, boolean resizable, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootName));
        Pane pane = (Pane) loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(resizable);
        if (resizable) {
            stage.setMinWidth(width);
            stage.setMinHeight(height);
            stage.setMaxWidth(sSize.getWidth());
            stage.setMaxHeight(sSize.getHeight());
        } else {
            stage.setMinWidth(width);
            stage.setMinHeight(height);
            stage.setMaxWidth(width);
            stage.setMaxHeight(height);
        }
        if (title != null) {
            stage.setTitle(title);
        }
        stage.getIcons().add(new Image(Constant.getApplicationIcon()));
        controller = (MenuController) loader.getController();
        controller.initialize(stage);
        stage.show();
    }
}
