package bank.model;

import java.time.LocalDateTime;

import bank.util.IdGenerator;

/**
 * Represents a single transaction entry.
 */
public class Transaction {
    private final String transactionId;
    private final String accountNumber;
    private final LocalDateTime timestamp;
    private final double amount;
    private final String type;
    private final double resultingBalance;

    private Transaction(String transactionId, String accountNumber, LocalDateTime timestamp,
                        double amount, String type, double resultingBalance) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.timestamp = timestamp;
        this.amount = amount;
        this.type = type;
        this.resultingBalance = resultingBalance;
    }

    public static Transaction deposit(String accountNumber, double amount, double resultingBalance) {
        return new Transaction(IdGenerator.newId(), accountNumber, LocalDateTime.now(),
                amount, "deposit", resultingBalance);
    }

    public static Transaction withdrawal(String accountNumber, double amount, double resultingBalance) {
        return new Transaction(IdGenerator.newId(), accountNumber, LocalDateTime.now(),
                amount, "withdrawal", resultingBalance);
    }

    public static Transaction interest(String accountNumber, double amount, double resultingBalance) {
        return new Transaction(IdGenerator.newId(), accountNumber, LocalDateTime.now(),
                amount, "interest", resultingBalance);
    }

    public static Transaction fromDatabase(String transactionId, String accountNumber,
                                           LocalDateTime timestamp, double amount,
                                           String type, double resultingBalance) {
        return new Transaction(transactionId, accountNumber, timestamp, amount, type, resultingBalance);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public double getResultingBalance() {
        return resultingBalance;
    }
}

