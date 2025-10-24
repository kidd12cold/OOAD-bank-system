package com.bankapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class TransactionsController {

    @FXML private ListView<String> transactionsListView;
    @FXML private Button refreshButton;

    @FXML
    private void initialize() {
        // load transaction list via Controller/Service
    }

    @FXML
    private void onRefresh() {
        // TODO: request transaction history from Controller/DAO layer
        System.out.println("Refresh transactions (delegate to service)");
    }
}