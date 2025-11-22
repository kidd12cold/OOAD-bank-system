package bank.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Handles login actions from the FXML-based screen.
 */
public class LoginController extends BaseController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin() {
        try {
            getBankService().login(usernameField.getText().trim(), passwordField.getText());
            getApp().showDashboardScene();
        } catch (IllegalArgumentException ex) {
            messageLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleRegisterLink() {
        getApp().showCustomerTypeScene();
    }
}

