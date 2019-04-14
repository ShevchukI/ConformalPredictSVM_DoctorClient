package com.controllers.windows.menu;

import com.controllers.windows.diagnostic.QuickDiagnosticChoiceController;
import com.controllers.windows.doctor.ChangeInfoMenuController;
import com.controllers.windows.doctor.LoginMenuController;
import com.tools.Constant;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * Created by Admin on 07.01.2019.
 */
public class MenuBarController extends MenuController {

//    @Autowired
//    LoginMenuController loginMenuController;
//    @Autowired
//    ChangeInfoMenuController changeInfoMenuController;

//    private QuickDiagnosticChoiceController quickDiagnosticChoiceController;

    private WindowsController windowsController;
    private MenuController menuController;


    public void init(MenuController menuController) {
        this.menuController = menuController;
//        quickDiagnosticChoiceController = new QuickDiagnosticChoiceController();
        windowsController = new WindowsController();
    }

    public void closeApplication(ActionEvent event) {
        menuController.getStage().close();
    }


    public void signOut(ActionEvent event) throws IOException {
        windowsController.openWindow(Constant.getLoginMenuRoot(),
                menuController.getStage(), new LoginMenuController(),
                "Login menu", false, 400, 250);
    }

    public void changeName(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow(Constant.getChangeNameRoot(), menuController.getStage(),
                new ChangeInfoMenuController(), "Change name and surname", true, 400, 240);
    }

    public void changePassword(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow(Constant.getChangePasswordRoot(), menuController.getStage(),
                new ChangeInfoMenuController(), "Change password", false, 400, 240);
    }

    public void about(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Developer");
        alert.setContentText("By DayRo\nBy Kosades");
        alert.showAndWait();
    }


    public void quickDiagnostic(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow(Constant.getQuickDiagnositcChoiceRoot(), menuController.getStage(),
                new QuickDiagnosticChoiceController(), menuController.getStage().getTitle(), 500,200);
    }
}
