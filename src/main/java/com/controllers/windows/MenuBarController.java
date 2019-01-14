package com.controllers.windows;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 07.01.2019.
 */
public class MenuBarController extends MenuController{


    @Autowired
    LoginMenuController loginMenuController;

    @Autowired
    HazelcastInstance hz;
//    HazelcastInstance hz = Hazelcast.newHazelcastInstance();
    @Autowired
    IMap<String, String> logmap;
//    IMap<String, String> logmap = hz.getMap("login");

    private WindowsController windowsController = new WindowsController();


    private Placeholder placeholder = new Placeholder();

//    private MainMenuController mainController;
    private MenuController menuController;

//    public void init(MainMenuController mainMenuController) {
//        this.mainController = mainMenuController;
//    }

    public void init(MenuController menuController){
        this.menuController = menuController;
    }

    public void closeApplication(ActionEvent event) {
        //mainController.getStage().close();
        hz.shutdown();
        menuController.getStage().close();
    }

    public void getPlaceholderAlert(ActionEvent event){
        placeholder.getAlert();
    }

    public void signOut(ActionEvent event) throws IOException {
        windowsController.openWindow("loginMenu.fxml", menuController.getStage(), loginMenuController, "Login menu", 350, 190);
    }
}
