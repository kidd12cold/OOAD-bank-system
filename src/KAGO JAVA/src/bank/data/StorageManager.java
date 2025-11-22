package bank.data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bank.model.Account;
import bank.model.Customer;
import bank.model.Transaction;

/**
 * Lightweight flat file store that keeps a snapshot of customers and accounts.
 * This keeps the project simple while still exercising Scanner and PrintWriter.
 */
public class StorageManager {
    private final Path storageFile;

    public StorageManager(Path storageFile) {
        this.storageFile = storageFile;
    }

    /**
     * Saves a snapshot of all customers for audit reporting.
     */
    public void saveSnapshot(List<Customer> customers) throws IOException {
        if (storageFile.getParent() != null && !Files.exists(storageFile.getParent())) {
            Files.createDirectories(storageFile.getParent());
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(storageFile.toFile()))) {
            for (Customer customer : customers) {
                writer.printf("CUSTOMER|%s|%s|%s|%s%n",
                        customer.getCustomerId(),
                        customer.isCompany() ? "Company" : "Individual",
                        customer.getUsername(),
                        customer.getEmail());
                for (Account account : customer.getAccounts()) {
                    writer.printf("ACCOUNT|%s|%s|%.2f|%s%n",
                            account.getAccountNumber(),
                            account.getClass().getSimpleName(),
                            account.getBalance(),
                            account.getOpenedDate());
                    for (Transaction txn : account.getTransactions()) {
                        writer.printf("TXN|%s|%s|%.2f|%.2f|%s%n",
                                txn.getTransactionId(),
                                txn.getType(),
                                txn.getAmount(),
                                txn.getResultingBalance(),
                                txn.getTimestamp());
                    }
                }
                writer.println();
            }
        }
    }

    /**
     * Reads the snapshot text so it can be shown in a report screen.
     */
    public List<String> readSnapshotLines() throws IOException {
        List<String> lines = new ArrayList<>();
        if (!Files.exists(storageFile)) {
            return lines;
        }
        try (Scanner scanner = new Scanner(storageFile)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }
        return lines;
    }
}

