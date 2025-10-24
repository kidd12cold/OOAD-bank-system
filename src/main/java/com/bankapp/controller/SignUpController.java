package com.bankapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SignUpController {

    @FXML private Button individualButton;
    @FXML private Button companyButton;

    @FXML
    private void initialize() {
        // Boundary initialization (no business logic here)
    }

    @FXML
    private void onIndividualSignUp() {
        // TODO: navigate to individual sign up form
        System.out.println("Navigate to Individual SignUp");
    }

    @FXML
    private void onCompanySignUp() {
        // TODO: navigate to company registration form
        System.out.println("Navigate to Company Registration");
    }
}