public class savings extends account {
    
    public savings(String accountID, double balance, String branch) {
        
        super(accountID, balance, branch);
    }

    @Override
    public void withdrawFunds(double amount) {
        
        if (amount > 0 && this.balance - amount >= 0) {
            this.balance -= amount;
            System.out.println("Savings " + getAccountID() + ": Withdrew $" + amount + ". New Balance: $" + balance);
        } else {
            System.out.println("Savings " + getAccountID() + ": Withdrawal failed. Insufficient funds or invalid amount.");
        }
    }

    @Override
    public void applyMonthlyInterest() {
        double interestRate = 0.001; 
        double interest = this.balance * interestRate;
        this.balance += interest;
        System.out.println("Savings " + getAccountID() + ": Applied monthly interest of $" + String.format("%.2f", interest) + ". New Balance: $" + String.format("%.2f", balance));
    }
}