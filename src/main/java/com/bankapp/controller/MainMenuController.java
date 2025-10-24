package com.bankapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML private Button accountsButton;
    @FXML private Button transactionsButton;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() {
        // basic UI wiring
    }

    @FXML
    private void onOpenAccounts() {
        // TODO: navigation to Account Management view
        System.out.println("Navigate to Account Management");
    }

    @FXML
    private void onOpenTransactions() {
        // TODO: navigation to Transactions view
        System.out.println("Navigate to Transactions");
    }

    @FXML
    private void onLogout() {
        // TODO: perform logout via authentication controller/service
        System.out.println("Logging out (delegate to service)");
    }
}