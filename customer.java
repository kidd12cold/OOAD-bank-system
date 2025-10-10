import java.util.ArrayList;
import java.util.List;

public class customer implements GUI {
    
    private String customerID;
    private String customerName;
    private String address;
    private String phone;
    private String employerName;
    private String employerAddress;

    
    private List<account> accounts;

    
    public customer(String customerID, String customerName, String address, String phone, String employerName, String employerAddress) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.employerName = employerName;
        this.employerAddress = employerAddress;
        this.accounts = new ArrayList<>();
    }

    
    public void openAccount(account newAccount) {
        this.accounts.add(newAccount);
        System.out.println(customerName + " opened a new " + newAccount.getClass().getSimpleName() + " account.");
    }

    
    @Override
    public void showNames() {
        System.out.println("Customer Name: " + customerName);
    }

   
    public String getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }
}