package bank.model;

import java.time.LocalDate;

import java.time.LocalDate;

/**
 * Cheque account supports withdrawals but pays no interest.
 */
public class ChequeAccount extends Account implements Withdrawable {
    public ChequeAccount(String accountNumber, Customer owner, double openingBalance) {
        super(accountNumber, owner, openingBalance);
    }

    public ChequeAccount(String accountNumber, Customer owner, double balance,
                         LocalDate openedDate, boolean recordInitialDeposit) {
        super(accountNumber, owner, balance, openedDate, recordInitialDeposit);
    }

    @Override
    public void applyMonthlyInterest() {
        // No interest for cheque accounts
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal must be positive.");
        }
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        setBalance(getBalance() - amount);
        getTransactions().add(Transaction.withdrawal(getAccountNumber(), amount, getBalance()));
    }
}

