package com.controllers.windows;

import com.controllers.requests.DoctorController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Doctor;
import com.tools.Encryptor;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 06.01.2019.
 */
public class LoginMenuController extends MenuController {

    @Autowired
    RegistrationMenuController registrationMenuController;

    @Autowired
    MainMenuController mainMenuController;

    @Autowired
    HttpResponse response;

//    private Stage stage;

    private String key;
    private String vector;

    private DoctorController doctorController = new DoctorController();

    private Encryptor encryptor = new Encryptor();

    private int statusCode;


    private Placeholder placeholder = new Placeholder();

    private WindowsController windowsController = new WindowsController();

    @FXML
    private TextField textField_Login;

    @FXML
    private PasswordField passwordField_Password;

    public void initialize() {

    }

    public void initialize(Stage stage,HazelcastInstance hazelcastInstance){
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event ->{hazelcastInstance.getLifecycleService().shutdown();});
        setStage(stage);
        setInstance(hazelcastInstance);
        getMap().clear();
    }

    public void signIn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        if (textField_Login.getText().equals("")) {
            alert.setContentText("Login is empty!");
            alert.showAndWait();
        } else if (passwordField_Password.getText().equals("")) {
            alert.setContentText("Password is empty!");
            alert.showAndWait();
        } else {
            response = doctorController.getDoctorAuth(textField_Login.getText(), passwordField_Password.getText());

            statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
//                System.out.println("Status code: " + statusCode);

                Doctor doctor = new Doctor().fromJson(response);

                key = encryptor.genRandString();
                vector = encryptor.genRandString();

                getMap().put("key", key);
                getMap().put("vector", vector);
                getMap().put("login", encryptor.encrypt(key, vector, textField_Login.getText()));
                getMap().put("password", encryptor.encrypt(key, vector, passwordField_Password.getText()));

                getMap().put("id", doctor.getId());
                getMap().put("name", doctor.getName());
                getMap().put("surname", doctor.getSurname());
                if(doctor.getSpecialization() != null){
                    getMap().put("specId", doctor.getSpecialization().getId());
                    getMap().put("specName", doctor.getSpecialization().getName());
                }
                else {
                    getMap().put("specId", "-2");
                    getMap().put("specName", "Empty");
                }

//                System.out.println(getMap().get("key"));
//                System.out.println(getMap().get("vector"));
//                System.out.println(getMap().get("login") + " :: " + encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()));
//                System.out.println(getMap().get("password")+ " :: " + encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()));
//                System.out.println(getMap().size());


                windowsController.openWindowResizable("mainMenu.fxml", getStage(), getInstance(), mainMenuController, "Main menu", 600, 640);

//                windowsController.openWindowResizable("mainMenu.fxml", getStage(), getInstance(), mainMenuController, "Main menu", 600, 640);
            } else {

                alert.setHeaderText("Status code: " + statusCode);
                alert.setContentText("Login or password incorrect!");
                alert.showAndWait();
            }
        }
    }

    public void signUp(ActionEvent event) throws IOException {

        windowsController.openWindow("registrationMenu.fxml", getStage(), getInstance(), registrationMenuController, "Registration", 408, 460);

    }


}
