public class investment extends account implements addInterest {
    

    
    public investment(String accountID, double balance, String branch) {
        
        super(accountID, balance, branch);
    }

    
    @Override
    public void withdrawFunds(double amount) {
        
        double penalty = 0.05; 
        double totalWithdrawal = amount + (amount * penalty);

        if (amount > 0 && this.balance >= totalWithdrawal) {
            this.balance -= totalWithdrawal;
            System.out.println("Investment " + getAccountID() + ": Withdrew $" + amount + " (includes $" + (amount * penalty) + " penalty). New Balance: $" + balance);
        } else {
            System.out.println("Investment " + getAccountID() + ": Withdrawal failed. Insufficient funds or invalid amount.");
        }
    }

    
    @Override
    public void applyMonthlyInterest() {
        double interestRate = 0.005; 
        double interest = this.balance * interestRate;
        this.balance += interest;
        System.out.println("Investment " + getAccountID() + ": Applied monthly interest of $" + String.format("%.2f", interest) + ". New Balance: $" + String.format("%.2f", balance));
    }

    
    @Override
    public double CalculateInterest(double amount) {
        double annualRate = 0.06;
        return amount * (annualRate / 12); 
    }
}