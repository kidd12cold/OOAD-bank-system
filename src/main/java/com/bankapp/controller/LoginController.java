package com.bankapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        // UI initialization (no business logic)
        messageLabel.setText("");
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // TODO: delegate authentication to a Controller/Service (not here)
        messageLabel.setText("Attempting login for: " + username);
    }

    @FXML
    private void onGoToSignUp() {
        // TODO: instruct application navigator to open SignUp view
        messageLabel.setText("Open SignUp view (not implemented)");
    }
}