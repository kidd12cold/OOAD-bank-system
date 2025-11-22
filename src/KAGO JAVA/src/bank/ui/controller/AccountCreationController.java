package bank.ui.controller;

import bank.model.AccountType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for the modal that creates new accounts.
 */
public class AccountCreationController extends BaseController {
    @FXML
    private TextField typeField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField employerField;
    @FXML
    private Label messageLabel;

    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    private void handleCreate() {
        try {
            AccountType type = AccountType.fromName(typeField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());
            getBankService().createAccount(type, amount, employerField.getText().trim());
            if (dashboardController != null) {
                dashboardController.refresh();
            } else {
                getApp().refreshDashboard();
            }
            messageLabel.setText("Account created successfully.");
        } catch (NumberFormatException ex) {
            messageLabel.setText("Enter a valid number.");
        } catch (IllegalArgumentException ex) {
            messageLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        closeDialog();
    }
}

