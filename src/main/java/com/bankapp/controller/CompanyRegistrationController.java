package com.bankapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class CompanyRegistrationController {

    @FXML private TextField companyNameField;
    @FXML private TextField companyAddressField;
    @FXML private Button registerButton;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("");
    }

    @FXML
    private void onRegisterCompany() {
        String companyName = companyNameField.getText();
        String companyAddress = companyAddressField.getText();

        // TODO: delegate company registration to a Controller/Service
        statusLabel.setText("Registering company: " + companyName);
    }
}