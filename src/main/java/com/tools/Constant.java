package com.tools;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Base64;


public class Constant {
    //Hazelcast
    private final static String KEY = "key";
    private final static String VECTOR = "vector";
    private final static String LOGIN = "login";
    private final static String PASSWORD = "password";

    //String matches
    private final static String PHONEREG = "[+]{0,1}[0-9]{3,20}";
    private final static String EMAILREG = "[a-zA-Z0-9]+[@][a-z]+[.]{0,1}[a-z]{1,3}";
    private final static String WHEIGHT = "[0-9]{1,3}[.]{0,1}[0-9]{1,3}";
    private final static String DATE_PICKER = "[0-9]{2}.[0-9]{2}.[0-9]{4}";

    //Icons
    private final static String SIGN_IN_BUTTON_ICON = "/img/icons/signIn.png";
    private final static String APPLICATION_ICON = "img/icons/icon.png";
    private final static String ADD_ICON = "img/icons/add.png";
    private final static String CANCEL_ICON = "img/icons/cancel.png";
    private final static String DELETE_ICON = "img/icons/delete.png";
    private final static String INFO_ICON = "img/icons/info.png";
    private final static String OK_ICON = "img/icons/ok.png";
    private final static String RETURN_ICON = "img/icons/return.png";
    private final static String RUN_ICON = "img/icons/run.png";
    private final static String SEARCH_ICON = "img/icons/search.png";
    private final static String EDIT_ICON = "img/icons/edit.png";

    //FXML root
    private final static String DIAGNOSTIC_MENU_ROOT = "fxml/diagnostic/diagnosticMenu.fxml";
    private final static String QUICK_DIAGNOSTIC_CHOICE_ROOT = "fxml/diagnostic/quickDiagnosticChoice.fxml";

    private final static String CHANGE_NAME_ROOT = "fxml/doctor/changeName.fxml";
    private final static String CHANGE_PASSWORD_ROOT = "fxml/doctor/changePassword.fxml";
    private final static String LOGIN_MENU_ROOT = "fxml/doctor/loginMenu.fxml";

    private final static String MAIN_MENU_ROOT = "fxml/menu/mainMenu.fxml";
    private final static String MENU_BAR_ROOT = "fxml/doctor/menuBar.fxml";

    private final static String ADD_PATIENT_AND_RECORD_MENU_ROOT = "fxml/patient/addPatientAndRecordMenu.fxml";
    private final static String ADD_PATIENT_MENU_ROOT = "fxml/patient/addPatientMenu-.fxml";
    private final static String ADD_RECORD_MENU_ROOT = "fxml/patient/addRecordMenu-.fxml";
    private final static String CARD_MENU_ROOT = "fxml/patient/cardMenu.fxml";
    private final static String CARD_PAGE_MENU_ROOT = "fxml/patient/cardPageMenu.fxml";

    //Miscellaneous
    private static final int OBJECT_ON_PAGE = 30;
    private final static String BORDER_COLOR_INHERIT = "-fx-border-color: inherit";
    private final static String BORDER_COLOR_RED = "-fx-border-color: red";

    private final static String LOCALHOST_URL = "http://localhost:8888";
    private final static String DOCTOR_URL = "/doctor-system/doctor";
    private final static String URL = LOCALHOST_URL + DOCTOR_URL;

    public static String getUrl(){
        return URL;
    }

    public static HttpResponse crudEntity(HttpEntity httpEntity, HttpPost post, HttpGet get, HttpPut put, HttpDelete delete) {
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString((Constant.getAuth()[0] + ":" + Constant.getAuth()[1]).getBytes());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpRequestBase request = null;
        if (post != null) {

            post.setHeader("Content-Type", "application/json");
            if(httpEntity!=null) {
                post.setEntity(httpEntity);
            }
            request = post;

        } else if (put != null) {

            put.setHeader("Content-Type", "application/json");
            if(httpEntity!=null) {
                put.setEntity(httpEntity);
            }
            request = put;

        } else if (get != null) {
            request = get;
        } else if (delete != null) {
            delete.setHeader("Content-Type", "application/json");
            request = delete;
        }
        request.addHeader("Authorization", basicAuthPayload);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (HttpHostConnectException e) {
            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String[] getAuth() {
        String[] auth = new String[2];
        String login = Encryptor.decrypt(HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(KEY).toString(),
                HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(VECTOR).toString(), HazelCastMap.getMapByName(HazelCastMap.getUserMapName()).get(LOGIN).toString());
        String password = Encryptor.decrypt(HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(KEY).toString(),
                HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get(VECTOR).toString(), HazelCastMap.getMapByName(HazelCastMap.getUserMapName()).get(PASSWORD).toString());
        auth[0] = login;
        auth[1] = password;
        return auth;
    }


    public static String responseToString(HttpResponse response) throws IOException {
        return  EntityUtils.toString(response.getEntity());
    }



    public static void run(Runnable treatment) {
        if(treatment == null) throw new IllegalArgumentException("The treatment to perform can not be null");
        if(Platform.isFxApplicationThread()) treatment.run();
        else Platform.runLater(treatment);
    }



    public static void getAlert(String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean checkStatusCode(int statusCode) {
        switch (statusCode) {
            case 200:
                return true;
            case 201:
                return true;
            case 401:
                Constant.getAlert(null, "Unauthorized: login or password incorrect!", Alert.AlertType.ERROR);
                return false;
            case 423:
                Constant.getAlert(null, "Not allow!", Alert.AlertType.ERROR);
                return false;
            case 504:
                Constant.getAlert(null, "Connection to the server is missing!", Alert.AlertType.ERROR);
                return false;
            default:
                return false;
        }
    }


    public static String getPHONEREG() {
        return PHONEREG;
    }

    public static String getEMAILREG() {
        return EMAILREG;
    }

    public static String getWHEIGHT() {
        return WHEIGHT;
    }

    public static String getSignInButtonIcon() {
        return SIGN_IN_BUTTON_ICON;
    }

    public static String getApplicationIcon() {
        return APPLICATION_ICON;
    }

    public static String getAddIcon() {
        return ADD_ICON;
    }

    public static String getCancelIcon() {
        return CANCEL_ICON;
    }

    public static String getDeleteIcon() {
        return DELETE_ICON;
    }

    public static String getInfoIcon() {
        return INFO_ICON;
    }

    public static String getOkIcon() {
        return OK_ICON;
    }

    public static String getReturnIcon() {
        return RETURN_ICON;
    }

    public static String getRunIcon() {
        return RUN_ICON;
    }

    public static String getSearchIcon() {
        return SEARCH_ICON;
    }

    public static String getEditIcon() {
        return EDIT_ICON;
    }

    public static String getDiagnosticMenuRoot() {
        return DIAGNOSTIC_MENU_ROOT;
    }

    public static String getQuickDiagnosticChoiceRoot() {
        return QUICK_DIAGNOSTIC_CHOICE_ROOT;
    }

    public static String getChangeNameRoot() {
        return CHANGE_NAME_ROOT;
    }

    public static String getChangePasswordRoot() {
        return CHANGE_PASSWORD_ROOT;
    }

    public static String getLoginMenuRoot() {
        return LOGIN_MENU_ROOT;
    }

    public static String getMainMenuRoot() {
        return MAIN_MENU_ROOT;
    }

    public static String getMenuBarRoot() {
        return MENU_BAR_ROOT;
    }

    public static String getAddPatientAndRecordMenuRoot() {
        return ADD_PATIENT_AND_RECORD_MENU_ROOT;
    }

    public static String getAddPatientMenuRoot() {
        return ADD_PATIENT_MENU_ROOT;
    }

    public static String getAddRecordMenuRoot() {
        return ADD_RECORD_MENU_ROOT;
    }

    public static String getCardMenuRoot() {
        return CARD_MENU_ROOT;
    }

    public static String getCardPageMenuRoot() {
        return CARD_PAGE_MENU_ROOT;
    }

    public static int getObjectOnPage() {
        return OBJECT_ON_PAGE;
    }

    public static String getBorderColorInherit() {
        return BORDER_COLOR_INHERIT;
    }

    public static String getBorderColorRed() {
        return BORDER_COLOR_RED;
    }

    public static String getDatePicker() {
        return DATE_PICKER;
    }
}
