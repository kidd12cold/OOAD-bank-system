package com.bankapp.domain;

public class InvestmentAccount extends Account {

    // Constructor — must match the class name exactly
    public InvestmentAccount(String accountID, double balance, String branch) {
        super(accountID, balance, branch);
        if (balance < 500.0) {
            throw new IllegalArgumentException("Investment accounts require a minimum opening deposit of BWP500.00");
        }
    }

    @Override
    public void applyMonthlyInterest() {
        // Spec: Investment monthly interest = 5% => 0.05
        double interestRate = 0.05;
        double interest = this.balance * interestRate;
        this.balance += interest;
        System.out.println("Investment " + getAccountID() + ": Applied monthly interest of "
            + String.format("%.2f", interest) + ". New Balance: " + String.format("%.2f", balance));
    }

    @Override
    public double CalculateInterest(double amount) {
        // Return monthly interest amount at 5%
        return amount * 0.05;
    }
}