package bank.model;

import java.time.LocalDate;

/**
 * Investment account pays high interest and allows withdrawals.
 */
public class InvestmentAccount extends Account implements Withdrawable {
    public static final double MONTHLY_RATE = 0.05; // 5%
    public static final double MIN_OPENING = 500.0;

    public InvestmentAccount(String accountNumber, Customer owner, double openingBalance) {
        super(accountNumber, owner, 0);
        if (openingBalance < MIN_OPENING) {
            throw new IllegalArgumentException("Investment requires at least P500.");
        }
        deposit(openingBalance);
    }

    public InvestmentAccount(String accountNumber, Customer owner, double balance,
                             LocalDate openedDate, boolean recordInitialDeposit) {
        super(accountNumber, owner, balance, openedDate, recordInitialDeposit);
    }

    @Override
    public void applyMonthlyInterest() {
        creditInterest(MONTHLY_RATE);
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

