public class SavingsAccount extends Account {

    @Override
    public void withdrawFunds(double amount) {
        // Per assignment spec: Savings accounts DO NOT allow withdrawals.
        System.out.println("Savings " + getAccountID() + ": Withdrawals are not allowed for Savings accounts.");
        // Optionally throw to make callers aware:
        // throw new UnsupportedOperationException("Withdrawals not permitted on Savings accounts.");
    }

    @Override
    public void applyMonthlyInterest() {
        // Spec: Savings monthly interest = 0.05% => 0.0005
        double interestRate = 0.0005;
        double interest = this.balance * interestRate;
        this.balance += interest;
        System.out.println("Savings " + getAccountID() + ": Applied monthly interest of " 
            + String.format("%.2f", interest) + ". New Balance: " + String.format("%.2f", balance));
    }
}