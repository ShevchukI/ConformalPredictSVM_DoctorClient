package com.controllers.windows;

import com.tools.Placeholder;
import javafx.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 07.01.2019.
 */
public class MenuBarController extends MenuController {


    @Autowired
    LoginMenuController loginMenuController;

    @Autowired
    ChangeInfoMenuController changeInfoMenuController;

    private WindowsController windowsController = new WindowsController();


    private Placeholder placeholder = new Placeholder();


    private MenuController menuController;


    public void init(MenuController menuController) {
        this.menuController = menuController;
    }

    public void closeApplication(ActionEvent event) {
        menuController.getStage().setOnHiding(event1 -> {
            menuController.getInstance().shutdown();
        });
        menuController.getStage().close();
    }

    public void getPlaceholderAlert(ActionEvent event) {
        placeholder.getAlert();
    }

    public void signOut(ActionEvent event) throws IOException {
        windowsController.openWindow("loginMenu.fxml", menuController.getStage(), menuController.getInstance(), loginMenuController, "Login menu", 350, 190);
    }

    public void changeName(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("changeName.fxml", menuController.getStage(), menuController.getInstance(),
                changeInfoMenuController, "Change name and surname", 400, 200);
    }

    public void changePassword(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("changePassword.fxml", menuController.getStage(), menuController.getInstance(),
                changeInfoMenuController, "Change password", 400, 200);

    }
}
