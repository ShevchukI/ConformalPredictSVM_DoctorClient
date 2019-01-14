package com;

import com.controllers.windows.LoginMenuController;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        IMap<String, String> logmap = hz.getMap("login");


        FXMLLoader loginMenuLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/loginMenu.fxml"));
        Pane loginMenuPane = (Pane)loginMenuLoader.load();
        Scene loginMenuScene = new Scene(loginMenuPane);
        stage.setScene(loginMenuScene);
        stage.setResizable(false);
        stage.setTitle("DocClient");
        LoginMenuController loginMenuController = (LoginMenuController)loginMenuLoader.getController();
        loginMenuController.setStage(stage);
        stage.show();

    }
}