package com.controllers.windows.patient;

import com.controllers.windows.menu.MenuController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Admin on 16.01.2019.
 */
public class RecordMenuController extends MenuController {

    private MenuController menuController;
    private ObservableList<String> sex = FXCollections.observableArrayList("Male", "Female");
    private ObservableList<String> bloodGroup = FXCollections.observableArrayList("O", "A", "B", "AB");
    private ObservableList<String> bloodType = FXCollections.observableArrayList("+", "-");
    private Tooltip tooltipError_Birthday = new Tooltip();
    private Tooltip tooltipError_Sex = new Tooltip();
    private Tooltip tooltipError_BloodGroup = new Tooltip();
    private Tooltip tooltipError_BloodType = new Tooltip();
    private Tooltip tooltipError_Weight = new Tooltip();
    private Tooltip tooltipError_Height = new Tooltip();

    @FXML
    private DatePicker datePicker_Birthday;
    @FXML
    private ChoiceBox<String> choiceBox_Sex;
    @FXML
    private ChoiceBox<String> choiceBox_BloodGroup;
    @FXML
    private ChoiceBox<String> choiceBox_BloodType;
    @FXML
    private TextField textField_Weight;
    @FXML
    private TextField textField_Height;
    @FXML
    private Tooltip tooltip_Birthday;
    @FXML
    private Tooltip tooltip_Sex;
    @FXML
    private Tooltip tooltip_BloodGroup;
    @FXML
    private Tooltip tooltip_BloodType;
    @FXML
    private Tooltip tooltip_Weight;
    @FXML
    private Tooltip tooltip_Height;

    public void init(MenuController menuController) {
        this.menuController = menuController;
    }

    public void initialize() {
        choiceBox_Sex.setItems(sex);
        choiceBox_BloodGroup.setItems(bloodGroup);
        choiceBox_BloodType.setItems(bloodType);
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(LocalDate.of(1900, Month.JANUARY, 1))) {
                            setDisable(true);
                        } else if (item.isAfter(LocalDate.now())) {
                            setDisable(true);
                        }
                    }
                };
            }
        };
        datePicker_Birthday.setDayCellFactory(dayCellFactory);
    }

    public DatePicker getDatePicker_Birthday() {
        return datePicker_Birthday;
    }

    public void setDatePicker_Birthday(DatePicker datePicker_Birthday) {
        this.datePicker_Birthday = datePicker_Birthday;
    }

    public ChoiceBox<String> getChoiceBox_Sex() {
        return choiceBox_Sex;
    }

    public void setChoiceBox_Sex(ChoiceBox<String> choiceBox_Sex) {
        this.choiceBox_Sex = choiceBox_Sex;
    }

    public ChoiceBox<String> getChoiceBox_BloodGroup() {
        return choiceBox_BloodGroup;
    }

    public void setChoiceBox_BloodGroup(ChoiceBox<String> choiceBox_BloodGroup) {
        this.choiceBox_BloodGroup = choiceBox_BloodGroup;
    }

    public ChoiceBox<String> getChoiceBox_BloodType() {
        return choiceBox_BloodType;
    }

    public void setChoiceBox_BloodType(ChoiceBox<String> choiceBox_BloodType) {
        this.choiceBox_BloodType = choiceBox_BloodType;
    }

    public Tooltip getTooltip_Birthday() {
        return tooltip_Birthday;
    }

    public void setTooltip_Birthday(Tooltip tooltip_Birthday) {
        this.tooltip_Birthday = tooltip_Birthday;
    }

    public Tooltip getTooltip_Sex() {
        return tooltip_Sex;
    }

    public void setTooltip_Sex(Tooltip tooltip_Sex) {
        this.tooltip_Sex = tooltip_Sex;
    }

    public Tooltip getTooltip_BloodGroup() {
        return tooltip_BloodGroup;
    }

    public void setTooltip_BloodGroup(Tooltip tooltip_BloodGroup) {
        this.tooltip_BloodGroup = tooltip_BloodGroup;
    }

    public Tooltip getTooltip_BloodType() {
        return tooltip_BloodType;
    }

    public void setTooltip_BloodType(Tooltip tooltip_BloodType) {
        this.tooltip_BloodType = tooltip_BloodType;
    }

    public Tooltip getTooltip_Weight() {
        return tooltip_Weight;
    }

    public void setTooltip_Weight(Tooltip tooltip_Weight) {
        this.tooltip_Weight = tooltip_Weight;
    }

    public Tooltip getTooltip_Height() {
        return tooltip_Height;
    }

    public void setTooltip_Height(Tooltip tooltip_Height) {
        this.tooltip_Height = tooltip_Height;
    }

    public Tooltip getTooltipError_Birthday() {
        return tooltipError_Birthday;
    }

    public void setTooltipError_Birthday(Tooltip tooltipError_Birthday) {
        this.tooltipError_Birthday = tooltipError_Birthday;
    }

    public Tooltip getTooltipError_Sex() {
        return tooltipError_Sex;
    }

    public void setTooltipError_Sex(Tooltip tooltipError_Sex) {
        this.tooltipError_Sex = tooltipError_Sex;
    }

    public Tooltip getTooltipError_BloodGroup() {
        return tooltipError_BloodGroup;
    }

    public void setTooltipError_BloodGroup(Tooltip tooltipError_BloodGroup) {
        this.tooltipError_BloodGroup = tooltipError_BloodGroup;
    }

    public Tooltip getTooltipError_BloodType() {
        return tooltipError_BloodType;
    }

    public void setTooltipError_BloodType(Tooltip tooltipError_BloodType) {
        this.tooltipError_BloodType = tooltipError_BloodType;
    }

    public Tooltip getTooltipError_Weight() {
        return tooltipError_Weight;
    }

    public void setTooltipError_Weight(Tooltip tooltipError_Weight) {
        this.tooltipError_Weight = tooltipError_Weight;
    }

    public Tooltip getTooltipError_Height() {
        return tooltipError_Height;
    }

    public void setTooltipError_Height(Tooltip tooltipError_Height) {
        this.tooltipError_Height = tooltipError_Height;
    }

    public TextField getTextField_Weight() {
        return textField_Weight;
    }

    public void setTextField_Weight(TextField textField_Weight) {
        this.textField_Weight = textField_Weight;
    }

    public TextField getTextField_Height() {
        return textField_Height;
    }

    public void setTextField_Height(TextField textField_Height) {
        this.textField_Height = textField_Height;
    }
}
