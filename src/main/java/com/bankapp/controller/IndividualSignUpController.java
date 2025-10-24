package com.bankapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class IndividualSignUpController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField employerNameField;
    @FXML private TextField employerAddressField;
    @FXML private Button submitButton;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("");
    }

    @FXML
    private void onSubmitIndividual() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String employer = employerNameField.getText();
        String empAddress = employerAddressField.getText();

        // TODO: create Customer via Controller/Service (avoid business logic here)
        statusLabel.setText("Submitting individual signup for " + firstName + " " + lastName);
    }
}