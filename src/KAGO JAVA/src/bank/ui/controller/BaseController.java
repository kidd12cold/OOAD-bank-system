package bank.ui.controller;

import bank.model.BankService;
import bank.ui.BankingSystemApp;
import javafx.stage.Stage;

/**
 * Simple base class to let every FXML controller access the shared
 * {@link BankService} and the main {@link BankingSystemApp}. This keeps code
 * modular without adding complex patterns.
 */
public abstract class BaseController {
    private BankingSystemApp app;
    private BankService bankService;
    private Stage dialogStage;

    public final void init(BankingSystemApp app, BankService bankService) {
        this.app = app;
        this.bankService = bankService;
        onReady();
    }

    protected void onReady() {
        // default does nothing
    }

    protected BankingSystemApp getApp() {
        return app;
    }

    protected BankService getBankService() {
        return bankService;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    protected Stage getDialogStage() {
        return dialogStage;
    }

    protected void closeDialog() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}

