package com;

import com.controllers.windows.LoginMenuController;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        HazelcastInstance hazelcastInstance = getInstance();

        FXMLLoader loginMenuLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/loginMenu.fxml"));
        Pane loginMenuPane = (Pane)loginMenuLoader.load();
        Scene loginMenuScene = new Scene(loginMenuPane);
        stage.setScene(loginMenuScene);
        stage.setResizable(false);
        stage.setTitle("DocClient");
        stage.getIcons().add(new Image("img/icons/icon.png"));

        LoginMenuController loginMenuController = (LoginMenuController)loginMenuLoader.getController();
        loginMenuController.initialize(stage, hazelcastInstance);
        stage.show();

    }

    public static HazelcastInstance getInstance(){
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        return  hazelcastInstance;
    }
}