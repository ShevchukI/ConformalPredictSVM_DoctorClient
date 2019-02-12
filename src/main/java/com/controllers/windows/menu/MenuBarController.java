package com.controllers.windows.menu;

import com.controllers.windows.doctor.ChangeInfoMenuController;
import com.controllers.windows.doctor.LoginMenuController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
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


    public void signOut(ActionEvent event) throws IOException {
        windowsController.openWindow("doctor/loginMenu.fxml", menuController.getStage(), menuController.getInstance(), loginMenuController,
                "Login menu", 350, 190);
    }

    public void changeName(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("doctor/changeName.fxml", menuController.getStage(), menuController.getInstance(),
                changeInfoMenuController, "Change name and surname", true, 400, 200);
    }

    public void changePassword(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("doctor/changePassword.fxml", menuController.getStage(), menuController.getInstance(),
                changeInfoMenuController, "Change password", false, 400, 200);
    }

    public void about(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Developer");
        alert.setContentText("By DayRo\nBy Kosades");
        alert.showAndWait();
    }
}
