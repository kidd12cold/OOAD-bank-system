public class cheque extends account {
   
    private String employerName;
    private String employerAddress;

    
    public cheque(String accountID, double balance, String branch, String employerName, String employerAddress) {
        
        super(accountID, balance, branch);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

   
    @Override
    public void withdrawFunds(double amount) {
        
        if (amount > 0 && this.balance - amount >= -500.00) { 
            this.balance -= amount;
            System.out.println("Cheque " + getAccountID() + ": Withdrew $" + amount + ". New Balance: $" + balance);
        } else {
            System.out.println("Cheque " + getAccountID() + ": Withdrawal failed. Exceeded overdraft limit or invalid amount.");
        }
    }

    
    @Override
    public void applyMonthlyInterest() {
       
        System.out.println("Cheque " + getAccountID() + ": No monthly interest applied.");
    }
}