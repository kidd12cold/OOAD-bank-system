package com.bankapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class AccountManagementController {

    @FXML private ListView<String> accountsListView;
    @FXML private TextField accountTypeField;
    @FXML private TextField initialDepositField;
    @FXML private Button createAccountButton;
    @FXML private TextField depositAmountField;
    @FXML private TextField withdrawAmountField;
    @FXML private Button depositButton;
    @FXML private Button withdrawButton;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("");
        // Load accounts list via Controller/Service (not here)
    }

    @FXML
    private void onCreateAccount() {
        String type = accountTypeField.getText();
        String deposit = initialDepositField.getText();
        // TODO: delegate to Controller/Service to create account and refresh list
        statusLabel.setText("Create account: " + type + " with deposit " + deposit);
    }

    @FXML
    private void onDeposit() {
        String amt = depositAmountField.getText();
        // TODO: delegate deposit operation to Controller/Service
        statusLabel.setText("Deposit requested: " + amt);
    }

    @FXML
    private void onWithdraw() {
        String amt = withdrawAmountField.getText();
        // TODO: delegate withdraw operation to Controller/Service (Savings should block withdraw)
        statusLabel.setText("Withdraw requested: " + amt);
    }
}