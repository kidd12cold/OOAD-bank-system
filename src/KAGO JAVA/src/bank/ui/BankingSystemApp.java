package bank.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import bank.model.Account;
import bank.model.BankService;
import bank.ui.controller.AccountCreationController;
import bank.ui.controller.AmountDialogController;
import bank.ui.controller.BaseController;
import bank.ui.controller.DashboardController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Application entry point that now loads every screen from FXML files to match
 * the requested styling approach.
 */
public class BankingSystemApp extends Application {
    private Stage primaryStage;
    private BankService bankService;
    private Timeline interestTimeline;
    private DashboardController dashboardController;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        Path snapshotPath = Path.of(System.getProperty("user.home"),
                "banking-system-data", "snapshot.txt");
        String dbUrl = System.getProperty("bank.db.url", "jdbc:mysql://localhost:3306/banking_system");
        String dbUser = System.getProperty("bank.db.user", "root");
        String dbPassword = System.getProperty("bank.db.password", "password");
        bankService = new BankService(snapshotPath, dbUrl, dbUser, dbPassword);
        stage.setTitle("Digital Banking System");
        showCustomerTypeScene();
        stage.setWidth(900);
        stage.setHeight(600);
        stage.show();
        startInterestTimer();
    }

    @Override
    public void stop() {
        if (interestTimeline != null) {
            interestTimeline.stop();
        }
    }

    public void showCustomerTypeScene() {
        switchScene("customer_type.fxml", controller -> dashboardController = null);
    }

    public void showIndividualRegistrationScene() {
        switchScene("individual_registration.fxml", controller -> dashboardController = null);
    }

    public void showCompanyRegistrationScene() {
        switchScene("company_registration.fxml", controller -> dashboardController = null);
    }

    public void showLoginScene() {
        switchScene("bank_login.fxml", controller -> dashboardController = null);
    }

    public void showDashboardScene() {
        switchScene("dashboard.fxml", controller -> {
            if (controller instanceof DashboardController) {
                dashboardController = (DashboardController) controller;
            }
        });
    }

    public void logout() {
        bankService.logout();
        showLoginScene();
    }

    public void showAccountCreationDialog() {
        Stage dialog = createDialog("account_creation.fxml", controller -> {
            AccountCreationController creationController = (AccountCreationController) controller;
            creationController.setDashboardController(dashboardController);
        });
        dialog.setTitle("Create Account");
        dialog.showAndWait();
    }

    public void showAmountDialog(Account account, boolean deposit) {
        Stage dialog = createDialog("amount_dialog.fxml", controller -> {
            AmountDialogController dialogController = (AmountDialogController) controller;
            dialogController.setAccount(account);
            dialogController.setDeposit(deposit);
        });
        dialog.setTitle(deposit ? "Deposit" : "Withdraw");
        dialog.showAndWait();
    }

    public void showAuditDialog() {
        Stage dialog = createDialog("audit_dialog.fxml", null);
        dialog.setTitle("Audit Snapshot");
        dialog.show();
    }

    public void refreshDashboard() {
        if (dashboardController != null) {
            dashboardController.refresh();
        }
    }

    private void switchScene(String fxml, Consumer<BaseController> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            BaseController controller = loader.getController();
            controller.init(this, bankService);
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load view: " + fxml, ex);
        }
    }

    private Stage createDialog(String fxml, Consumer<BaseController> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            BaseController controller = loader.getController();
            controller.init(this, bankService);
            Stage dialog = new Stage();
            dialog.initOwner(primaryStage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            controller.setDialogStage(dialog);
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }
            dialog.setScene(new Scene(root));
            return dialog;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load dialog view: " + fxml, ex);
        }
    }

    private void startInterestTimer() {
        interestTimeline = new Timeline(new KeyFrame(Duration.seconds(60), event -> {
            if (bankService != null) {
                bankService.applyMonthlyInterest();
                if (dashboardController != null) {
                    dashboardController.onInterestApplied();
                }
            }
        }));
        interestTimeline.setCycleCount(Timeline.INDEFINITE);
        interestTimeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

