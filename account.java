public abstract class account {
    
    private String accountID;
    protected double balance; 
    private String branch;

    
    public account(String accountID, double balance, String branch) {
        this.accountID = accountID;
        this.balance = balance;
        this.branch = branch;
    }

    public void depositFunds(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Account " + accountID + ": Deposited $" + amount + ". New Balance: $" + balance);
        } else {
            System.out.println("Account " + accountID + ": Deposit failed. Amount must be positive.");
        }
    }

    public abstract void withdrawFunds(double amount);

    public abstract void applyMonthlyInterest();

    public double getBalance() {
        return balance;
    }

    public String getAccountID() {
        return accountID;
    }
}