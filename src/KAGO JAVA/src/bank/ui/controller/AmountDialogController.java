package bank.ui.controller;

import bank.model.Account;
import bank.model.Withdrawable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for deposit and withdrawal dialogs.
 */
public class AmountDialogController extends BaseController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private TextField amountField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button confirmButton;

    private Account account;
    private boolean deposit;

    public void setAccount(Account account) {
        this.account = account;
        updateLabels();
    }

    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
        updateLabels();
    }

    @FXML
    private void handleConfirm() {
        if (account == null) {
            messageLabel.setText("Select an account first.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive.");
            }
            if (deposit) {
                getBankService().deposit(account, amount);
            } else {
                if (!(account instanceof Withdrawable)) {
                    throw new IllegalArgumentException("Savings account does not allow withdrawals.");
                }
                getBankService().withdraw(account, amount);
            }
            getApp().refreshDashboard();
            closeDialog();
        } catch (NumberFormatException ex) {
            messageLabel.setText("Enter a valid number.");
        } catch (IllegalArgumentException ex) {
            messageLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void updateLabels() {
        if (accountLabel != null && account != null) {
            accountLabel.setText(account.getAccountNumber());
        }
        if (titleLabel != null) {
            titleLabel.setText(deposit ? "Deposit" : "Withdraw");
        }
        if (confirmButton != null) {
            confirmButton.setText(deposit ? "Confirm Deposit" : "Confirm Withdrawal");
        }
    }
}

