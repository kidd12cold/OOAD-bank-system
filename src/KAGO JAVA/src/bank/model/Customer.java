package bank.model;

import java.util.ArrayList;
import java.util.List;

import bank.util.IdGenerator;

/**
 * Base customer class shared by individuals and companies. The goal is to keep
 * common fields (address, credentials, contact information) in a single place
 * while letting subclasses expose their own unique details.
 */
public abstract class Customer {
    private final String customerId;
    private final List<Account> accounts;
    private String address;
    private String username;
    private String password;
    private String email;
    private String phone;

    /**
     * Convenience constructor that generates a new id.
     */
    protected Customer(String address, String username, String password,
                       String email, String phone) {
        this(IdGenerator.newId(), address, username, password, email, phone);
    }

    /**
     * Constructor used when loading from persistence so the original id is
     * preserved.
     */
    protected Customer(String customerId, String address, String username, String password,
                       String email, String phone) {
        this.customerId = customerId == null ? IdGenerator.newId() : customerId;
        this.address = address;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.accounts = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Adds an account to the in-memory list. Persistence is handled by the
     * service layer so this method deliberately stays simple.
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Shows a short description for the dashboard header or lists.
     */
    public abstract String displayInfo();

    public abstract String getDisplayName();

    public boolean isCompany() {
        return false;
    }
}

