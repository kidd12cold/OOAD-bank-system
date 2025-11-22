package bank.ui.controller;

import javafx.fxml.FXML;

/**
 * Handles the very first screen that lets the user choose between customer
 * types or jump to login.
 */
public class CustomerTypeController extends BaseController {

    @FXML
    private void handleIndividual() {
        getApp().showIndividualRegistrationScene();
    }

    @FXML
    private void handleCompany() {
        getApp().showCompanyRegistrationScene();
    }

    @FXML
    private void handleLogin() {
        getApp().showLoginScene();
    }
}

