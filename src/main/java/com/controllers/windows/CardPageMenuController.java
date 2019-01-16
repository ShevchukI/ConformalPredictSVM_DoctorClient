package com.controllers.windows;

import com.tools.Placeholder;
import javafx.event.ActionEvent;

/**
 * Created by Admin on 14.01.2019.
 */
public class CardPageMenuController extends MenuController {

    private Placeholder placeholder = new Placeholder();

    public void getPlaceholderAlert(ActionEvent event){
        placeholder.getAlert();
    }
}
