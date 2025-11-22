package bank.ui.controller;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

/**
 * Controller that shows the snapshot audit report.
 */
public class AuditDialogController extends BaseController {
    @FXML
    private TableView<String> snapshotTable;
    @FXML
    private TableColumn<String, String> lineColumn;
    @FXML
    private Label messageLabel;

    @Override
    protected void onReady() {
        lineColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        refreshData();
    }

    @FXML
    private void handleRefresh() {
        refreshData();
    }

    private void refreshData() {
        try {
            getBankService().saveSnapshot();
            List<String> lines = getBankService().readSnapshotLines();
            snapshotTable.setItems(FXCollections.observableArrayList(lines));
            messageLabel.setText("Snapshot refreshed.");
        } catch (IOException ex) {
            messageLabel.setText("Failed to read snapshot: " + ex.getMessage());
        }
    }
}

