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
    private ObservableList<String> sex;
    private ObservableList<String> bloodGroup;
    private ObservableList<String> bloodType;
    private Tooltip tooltipError_Birthday;
    private Tooltip tooltipError_Sex;
    private Tooltip tooltipError_BloodGroup;
    private Tooltip tooltipError_BloodType;
    private Tooltip tooltipError_Weight;
    private Tooltip tooltipError_Height;

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
        sex = FXCollections.observableArrayList("Male", "Female");
        bloodGroup = FXCollections.observableArrayList("O", "A", "B", "AB");
        bloodType = FXCollections.observableArrayList("+", "-");
        choiceBox_Sex.setItems(sex);
        choiceBox_BloodGroup.setItems(bloodGroup);
        choiceBox_BloodType.setItems(bloodType);
        tooltipError_Birthday = new Tooltip();
        tooltipError_Sex = new Tooltip();
        tooltipError_BloodGroup = new Tooltip();
        tooltipError_BloodType = new Tooltip();
        tooltipError_Weight = new Tooltip();
        tooltipError_Height = new Tooltip();
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

    public ChoiceBox<String> getChoiceBox_Sex() {
        return choiceBox_Sex;
    }

    public ChoiceBox<String> getChoiceBox_BloodGroup() {
        return choiceBox_BloodGroup;
    }

    public ChoiceBox<String> getChoiceBox_BloodType() {
        return choiceBox_BloodType;
    }

    public Tooltip getTooltip_Sex() {
        return tooltip_Sex;
    }

    public Tooltip getTooltip_BloodGroup() {
        return tooltip_BloodGroup;
    }

    public Tooltip getTooltip_BloodType() {
        return tooltip_BloodType;
    }

    public Tooltip getTooltip_Weight() {
        return tooltip_Weight;
    }

    public Tooltip getTooltip_Height() {
        return tooltip_Height;
    }

    public Tooltip getTooltipError_Birthday() {
        return tooltipError_Birthday;
    }

    public Tooltip getTooltipError_Sex() {
        return tooltipError_Sex;
    }

    public Tooltip getTooltipError_BloodGroup() {
        return tooltipError_BloodGroup;
    }

    public Tooltip getTooltipError_BloodType() {
        return tooltipError_BloodType;
    }

    public Tooltip getTooltipError_Weight() {
        return tooltipError_Weight;
    }

    public Tooltip getTooltipError_Height() {
        return tooltipError_Height;
    }

    public TextField getTextField_Weight() {
        return textField_Weight;
    }

    public TextField getTextField_Height() {
        return textField_Height;
    }

}
