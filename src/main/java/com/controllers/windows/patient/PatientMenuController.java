package com.controllers.windows.patient;

import com.controllers.windows.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * Created by Admin on 16.01.2019.
 */
public class PatientMenuController extends MenuController {

    private Tooltip tooltipError_Name;
    private Tooltip tooltipError_Surname;
    private Tooltip tooltipError_Telephone;
    private Tooltip tooltipError_Address;
    private Tooltip tooltipError_Email;
    private MenuController menuController;

    @FXML
    private TextField textField_Name;
    @FXML
    private TextField textField_Surname;
    @FXML
    private TextField textField_Telephone;
    @FXML
    private TextField textField_Address;
    @FXML
    private TextField textField_Email;
    @FXML
    private Tooltip tooltip_Name;
    @FXML
    private Tooltip tooltip_Surname;
    @FXML
    private Tooltip tooltip_Telephone;
    @FXML
    private Tooltip tooltip_Address;
    @FXML
    private Tooltip tooltip_Email;

    public void init(MenuController menuController){
        this.menuController = menuController;
        tooltipError_Name = new Tooltip();
        tooltipError_Surname = new Tooltip();
        tooltipError_Telephone = new Tooltip();
        tooltipError_Address = new Tooltip();
        tooltipError_Email = new Tooltip();
    }

    public TextField getTextField_Name() {
        return textField_Name;
    }

    public TextField getTextField_Surname() {
        return textField_Surname;
    }

    public TextField getTextField_Telephone() {
        return textField_Telephone;
    }

    public TextField getTextField_Address() {
        return textField_Address;
    }

    public TextField getTextField_Email() {
        return textField_Email;
    }

    public Tooltip getTooltip_Name() {
        return tooltip_Name;
    }

    public Tooltip getTooltip_Surname() {
        return tooltip_Surname;
    }

    public Tooltip getTooltip_Telephone() {
        return tooltip_Telephone;
    }

    public Tooltip getTooltip_Address() {
        return tooltip_Address;
    }

    public Tooltip getTooltip_Email() {
        return tooltip_Email;
    }

    public Tooltip getTooltipError_Name() {
        return tooltipError_Name;
    }

    public Tooltip getTooltipError_Surname() {
        return tooltipError_Surname;
    }

    public Tooltip getTooltipError_Telephone() {
        return tooltipError_Telephone;
    }

    public Tooltip getTooltipError_Address() {
        return tooltipError_Address;
    }

    public Tooltip getTooltipError_Email() {
        return tooltipError_Email;
    }

}
