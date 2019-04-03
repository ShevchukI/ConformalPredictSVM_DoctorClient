package com.controllers.windows.menu;

import com.controllers.windows.diagnostic.QuickDiagnosticChoiceController;
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

    private QuickDiagnosticChoiceController quickDiagnosticChoiceController;

    private WindowsController windowsController;
    private MenuController menuController;


    public void init(MenuController menuController) {
        this.menuController = menuController;
        quickDiagnosticChoiceController = new QuickDiagnosticChoiceController();
        windowsController = new WindowsController();
    }

    public void closeApplication(ActionEvent event) {
        menuController.getStage().close();
    }


    public void signOut(ActionEvent event) throws IOException {
        windowsController.openWindow("doctor/loginMenu", menuController.getStage(), loginMenuController,
                "Login menu", 400, 250);
    }

    public void changeName(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("doctor/changeName", menuController.getStage(),
                changeInfoMenuController, "Change name and surname", true, 400, 240);
    }

    public void changePassword(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("doctor/changePassword", menuController.getStage(),
                changeInfoMenuController, "Change password", false, 400, 240);
    }

    public void about(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Developer");
        alert.setContentText("By DayRo\nBy Kosades");
        alert.showAndWait();
    }


    public void quickDiagnostic(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("diagnostic/quickDiagnosticChoice", menuController.getStage(),
                quickDiagnosticChoiceController, menuController.getStage().getTitle(), 500,200);
    }
}
