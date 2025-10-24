package com.bankapp.domain;

public abstract class Account {
    protected double balance;
    private final String accountID;
    private final String branch;

    public Account(String accountID, double balance, String branch) {
        this.accountID = accountID;
        this.balance = balance;
        this.branch = branch;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getBranch() {
        return branch;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw amount must be positive");
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
    }

    // interest-related hooks for subclasses
    public abstract void applyMonthlyInterest();
    public abstract double CalculateInterest(double amount);
}