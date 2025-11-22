package bank.ui.controller;

import java.util.List;

import bank.model.Account;
import bank.model.Transaction;
import bank.model.Withdrawable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

/**
 * Controller for the dashboard scene that displays accounts, statements, and
 * actions.
 */
public class DashboardController extends BaseController {
    @FXML
    private Label headerLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableColumn<Account, String> accountNumberColumn;
    @FXML
    private TableColumn<Account, String> accountTypeColumn;
    @FXML
    private TableColumn<Account, String> accountBalanceColumn;
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> txnDateColumn;
    @FXML
    private TableColumn<Transaction, String> txnTypeColumn;
    @FXML
    private TableColumn<Transaction, String> txnAmountColumn;
    @FXML
    private TableColumn<Transaction, String> txnBalanceColumn;
    @FXML
    private Button withdrawButton;

    @Override
    protected void onReady() {
        configureTables();
        refresh();
    }

    public void refresh() {
        updateHeader();
        List<Account> accounts = getBankService().getAccountsForActiveCustomer();
        ObservableList<Account> data = FXCollections.observableArrayList(accounts);
        accountTable.setItems(data);
        accountTable.refresh();
        if (!data.isEmpty()) {
            accountTable.getSelectionModel().select(0);
        } else {
            transactionTable.setItems(FXCollections.emptyObservableList());
        }
        updateWithdrawalButton();
    }

    public void onInterestApplied() {
        refresh();
        messageLabel.setText("Interest applied automatically.");
    }

    @FXML
    private void handleDeposit() {
        Account selected = getSelectedAccount();
        if (selected == null) {
            messageLabel.setText("Select an account first.");
            return;
        }
        getApp().showAmountDialog(selected, true);
    }

    @FXML
    private void handleWithdraw() {
        Account selected = getSelectedAccount();
        if (selected == null) {
            messageLabel.setText("Select an account first.");
            return;
        }
        if (!(selected instanceof Withdrawable)) {
            messageLabel.setText("Savings account does not allow withdrawals.");
            return;
        }
        getApp().showAmountDialog(selected, false);
    }

    @FXML
    private void handleStatement() {
        Account selected = getSelectedAccount();
        if (selected == null) {
            messageLabel.setText("Select an account first.");
            return;
        }
        populateTransactions(selected);
        messageLabel.setText("Statement refreshed.");
    }

    @FXML
    private void handleCreateAccount() {
        getApp().showAccountCreationDialog();
    }

    @FXML
    private void handleApplyInterest() {
        getBankService().applyMonthlyInterest();
        refresh();
        messageLabel.setText("Interest applied.");
    }

    @FXML
    private void handleAudit() {
        getApp().showAuditDialog();
    }

    @FXML
    private void handleLogout() {
        getApp().logout();
    }

    private void configureTables() {
        accountNumberColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getAccountNumber()));
        accountTypeColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getClass().getSimpleName()));
        accountBalanceColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.format("%.2f", param.getValue().getBalance())));
        accountTable.getSelectionModel().selectedItemProperty().addListener((obs, oldAccount, newAccount) -> {
            populateTransactions(newAccount);
            updateWithdrawalButton();
            if (newAccount instanceof Withdrawable) {
                messageLabel.setText("Withdrawals allowed.");
            } else if (newAccount != null) {
                messageLabel.setText("This account does not allow withdrawals.");
            }
        });

        txnDateColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getTimestamp().toString()));
        txnTypeColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getType()));
        txnAmountColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.format("%.2f", param.getValue().getAmount())));
        txnBalanceColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.format("%.2f", param.getValue().getResultingBalance())));
    }

    private void populateTransactions(Account account) {
        if (account == null) {
            transactionTable.setItems(FXCollections.emptyObservableList());
            return;
        }
        List<Transaction> transactions = getBankService().getTransactions(account);
        transactionTable.setItems(FXCollections.observableArrayList(transactions));
    }

    private void updateHeader() {
        if (getBankService().getActiveCustomer() == null) {
            headerLabel.setText("Welcome");
        } else {
            headerLabel.setText("Welcome, " + getBankService().getActiveCustomer().getDisplayName());
        }
    }

    private Account getSelectedAccount() {
        return accountTable.getSelectionModel().getSelectedItem();
    }

    private void updateWithdrawalButton() {
        Account current = getSelectedAccount();
        boolean allowed = current instanceof Withdrawable;
        withdrawButton.setDisable(!allowed);
    }
}

