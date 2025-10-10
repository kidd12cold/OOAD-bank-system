import java.util.ArrayList;
import java.util.List;
import java.util.UUID; 


interface GUI {
    
    void showNames();
}


interface addInterest {
    
    double CalculateInterest(double amount);
}


class bank implements GUI {
    
    private List<account> allAccounts;
    private List<customer> allCustomers;

    public bank() {
        this.allAccounts = new ArrayList<>();
        this.allCustomers = new ArrayList<>();
    }

    
    
    public account authenticateAccount(String accountID, String customerID) {
        System.out.println("Bank: Attempting to authenticate Account ID " + accountID + " for Customer ID " + customerID);
        for (account acc : allAccounts) {
            if (acc.getAccountID().equals(accountID)) {
                
                return acc;
            }
        }
        return null; 
    }

    
    public void transactionHistory() {
        System.out.println("Bank: Retrieving general transaction history (Simulated).");
    }

   
    @Override
    public void showNames() {
        System.out.println("\n--- Bank GUI Simulation ---");
        System.out.println("Welcome to the Bank System!");
    }

   
    public void addAccount(customer cust, account acc) {
        this.allAccounts.add(acc);
        
        if (!this.allCustomers.contains(cust)) {
             this.allCustomers.add(cust);
        }
        System.out.println("Bank: Added " + acc.getClass().getSimpleName() + " account " + acc.getAccountID() + " for customer " + cust.getCustomerName());
    }
}


public class Main {
    public static void main(String[] args) {
        
        bank myBank = new bank();
        myBank.showNames(); 

        
        customer johnDoe = new customer("CUST-" + UUID.randomUUID().toString().substring(0, 4), "John Doe", "123 Main St", "555-1234", "TechCorp", "456 Tech Blvd");

       
        savings johnSavings = new savings("ACC-" + UUID.randomUUID().toString().substring(0, 4), 5000.00, "HQ-01");
        johnDoe.openAccount(johnSavings);
        myBank.addAccount(johnDoe, johnSavings);

        
        investment johnInvestment = new investment("ACC-" + UUID.randomUUID().toString().substring(0, 4), 10000.00, "HQ-01");
        johnDoe.openAccount(johnInvestment);
        myBank.addAccount(johnDoe, johnInvestment);

        
        cheque johnCheque = new cheque("ACC-" + UUID.randomUUID().toString().substring(0, 4), 200.00, "HQ-01", "TechCorp", "456 Tech Blvd");
        johnDoe.openAccount(johnCheque);
        myBank.addAccount(johnDoe, johnCheque);

        System.out.println("\n--- Account Operations ---");

        
        account currentAccount = johnSavings; 

        
        currentAccount.depositFunds(100.00);

       
        currentAccount.withdrawFunds(50.00);
        System.out.println("Savings Balance: $" + currentAccount.getBalance());

       
        currentAccount = johnInvestment;
        currentAccount.depositFunds(500.00);

        
        System.out.println("\n--- Applying Interest (Polymorphism via Abstract Method) ---");
        johnSavings.applyMonthlyInterest();
        johnInvestment.applyMonthlyInterest();

        
        account authenticated = myBank.authenticateAccount(johnInvestment.getAccountID(), johnDoe.getCustomerID());
        if (authenticated != null && authenticated instanceof investment) {
            System.out.println("Authenticated. Calculating interest via Interface...");
            
            addInterest invAcc = (investment) authenticated;
            double interest = invAcc.CalculateInterest(authenticated.getBalance());
            System.out.println("Calculated Interest: $" + String.format("%.2f", interest));
        }

       
        System.out.println("\nFinal Savings Balance: $" + johnSavings.getBalance());
        System.out.println("Final Investment Balance: $" + johnInvestment.getBalance());
    }
}