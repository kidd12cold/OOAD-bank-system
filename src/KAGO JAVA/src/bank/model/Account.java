package bank.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Base account with shared state and helpers. Concrete account types extend
 * this class and override the interest logic or withdrawal rules.
 */
public abstract class Account {
    private final String accountNumber;
    private double balance;
    private final Customer owner;
    private final LocalDate openedDate;
    private final List<Transaction> transactions;

    /**
     * Constructor used for new accounts created by the UI. It records the
     * opening deposit as a transaction.
     */
    protected Account(String accountNumber, Customer owner, double openingBalance) {
        this(accountNumber, owner, openingBalance, LocalDate.now(), true);
    }

    /**
     * Constructor used when rebuilding accounts from storage. We can decide
     * whether to create an opening transaction or simply set the balance.
     */
    protected Account(String accountNumber, Customer owner, double amount,
                      LocalDate openedDate, boolean recordInitialTransaction) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.openedDate = openedDate == null ? LocalDate.now() : openedDate;
        this.transactions = new ArrayList<>();
        if (recordInitialTransaction && amount > 0) {
            deposit(amount);
        } else {
            this.balance = amount;
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public Customer getOwner() {
        return owner;
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Deposits funds and records a transaction.
     *
     * @param amount amount to deposit
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit must be positive.");
        }
        balance += amount;
        transactions.add(Transaction.deposit(accountNumber, amount, balance));
    }

    /**
     * Overloaded helper so callers can pass integer values easily.
     *
     * @param amount integer amount to deposit
     */
    public void deposit(int amount) {
        deposit((double) amount);
    }

    /**
     * Applies monthly interest at the rate defined by child classes.
     */
    public abstract void applyMonthlyInterest();

    /**
     * Records an interest transaction and updates balance. Concrete accounts
     * simply call this helper with their monthly rate.
     */
    protected void creditInterest(double rate) {
        double interest = balance * rate;
        if (interest > 0) {
            balance += interest;
            transactions.add(Transaction.interest(accountNumber, interest, balance));
        }
    }
}

