package com.controllers.windows.doctor;

import com.controllers.requests.DoctorController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Doctor;
import com.tools.Constant;
import com.tools.HazelCastMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by Admin on 06.01.2019.
 */
public class LoginMenuController extends MenuController {

    private int statusCode;
    private WindowsController windowsController;

    @FXML
    private TextField textField_Login;
    @FXML
    private PasswordField passwordField_Password;
    @FXML
    private Button button_SignIn;

    public void initialize(Stage stage) {
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        HazelCastMap.clearInstance();
//        mainMenuController = new MainMenuController();
        windowsController = new WindowsController();
        button_SignIn.setGraphic(new ImageView(Constant.getSignInButtonIcon()));
    }

    public void signIn(ActionEvent event) throws IOException {
        if (textField_Login.getText().equals("")) {
            Constant.getAlert(null, "Login is empty!", Alert.AlertType.ERROR);
        } else if (passwordField_Password.getText().equals("")) {
            Constant.getAlert(null, "Password is empty!", Alert.AlertType.ERROR);
        } else {
            String[] authorization = new String[2];
            authorization[0] = textField_Login.getText();
            authorization[1] = passwordField_Password.getText();
            HttpResponse response = DoctorController.getDoctorAuth(authorization);
            statusCode = response.getStatusLine().getStatusCode();
            if(checkStatusCode(statusCode)){
                HazelCastMap.fillMap(new Doctor().fromJson(response), authorization);
                windowsController.openWindow(Constant.getMainMenuRoot(), getStage(),
                        new MainMenuController(), null, true, 1100, 680);
            }
        }
    }
}
