import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Customer> allCustomers;
    private List<Account> allAccounts;

    public Bank() {
        allCustomers = new ArrayList<>();
        allAccounts = new ArrayList<>();
    }

    public void addAccount(Customer cust, Account acc) {
        if (cust == null) {
            System.out.println("Bank: Cannot add account without a customer.");
            return;
        }

        // Secondary guard: Investment minimum opening balance
        if (acc instanceof investment && acc.getBalance() < 500.0) {
            System.out.println("Bank: Investment account requires minimum opening balance of BWP500.00. Account not added.");
            return;
        }

        // Cheque accounts only for employed customers
        if (acc instanceof cheque) {
            String emp = cust.getEmployerName();
            if (emp == null || emp.trim().isEmpty()) {
                System.out.println("Bank: Cheque accounts can only be opened for employed customers. Account not added.");
                return;
            }
        }

        // Ensure account is associated with a customer (assignment rule)
        if (!this.allCustomers.contains(cust)) {
            this.allCustomers.add(cust);
        }

        this.allAccounts.add(acc);

        System.out.println("Bank: Added " + acc.getClass().getSimpleName() + " account " 
            + acc.getAccountID() + " for customer " + cust.getCustomerName());
    }

    public List<Customer> getAllCustomers() {
        return allCustomers;
    }

    public List<Account> getAllAccounts() {
        return allAccounts;
    }
}