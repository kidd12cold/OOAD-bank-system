package bank.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for company registration inputs.
 */
public class CompanyRegistrationController extends BaseController {
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField regNumberField;
    @FXML
    private TextField contactPersonField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegister() {
        try {
            getBankService().registerCompany(
                    companyNameField.getText().trim(),
                    regNumberField.getText().trim(),
                    contactPersonField.getText().trim(),
                    addressField.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    usernameField.getText().trim(),
                    passwordField.getText().trim());
            messageLabel.setText("Company registered. Please log in.");
        } catch (IllegalArgumentException ex) {
            messageLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        getApp().showCustomerTypeScene();
    }

    @FXML
    private void handleGoToLogin() {
        getApp().showLoginScene();
    }
}

