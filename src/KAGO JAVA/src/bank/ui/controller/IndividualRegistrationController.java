package bank.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for the individual registration workflow.
 */
public class IndividualRegistrationController extends BaseController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField nationalIdField;
    @FXML
    private TextField dobField;
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
    private TextField employmentStatusField;
    @FXML
    private TextField employerNameField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegister() {
        try {
            boolean employed = isEmployed(employmentStatusField.getText());
            getBankService().registerIndividual(
                    firstNameField.getText().trim(),
                    surnameField.getText().trim(),
                    nationalIdField.getText().trim(),
                    dobField.getText().trim(),
                    addressField.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    usernameField.getText().trim(),
                    passwordField.getText().trim(),
                    employed,
                    employerNameField.getText().trim());
            messageLabel.setText("Registration successful. Please log in.");
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

    private boolean isEmployed(String value) {
        if (value == null) {
            return false;
        }
        String text = value.trim().toLowerCase();
        return "yes".equals(text) || "employed".equals(text) || "true".equals(text);
    }
}

