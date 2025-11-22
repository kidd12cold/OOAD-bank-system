package bank.model;

import java.time.LocalDate;

/**
 * Savings account pays small interest and blocks withdrawals.
 */
public class SavingsAccount extends Account {
    public static final double MONTHLY_RATE = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, Customer owner, double openingBalance) {
        super(accountNumber, owner, openingBalance);
    }

    public SavingsAccount(String accountNumber, Customer owner, double balance,
                          LocalDate openedDate, boolean recordInitialDeposit) {
        super(accountNumber, owner, balance, openedDate, recordInitialDeposit);
    }

    @Override
    public void applyMonthlyInterest() {
        creditInterest(MONTHLY_RATE);
    }
}

