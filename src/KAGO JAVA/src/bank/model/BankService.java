package bank.model;

import bank.data.AccountFactory;
import bank.data.DatabaseManager;
import bank.data.StorageManager;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central service that keeps customers in memory, applies account rules and
 * exposes helper methods for the UI layer. All requests from controllers pass
 * through this class so validation, persistence and business rules stay in one
 * easy-to-read place.
 */
public class BankService {
    private final List<Customer> customers;
    private final StorageManager storageManager;
    private DatabaseManager databaseManager;
    private final boolean databaseEnabled;
    private Customer activeCustomer;

    public BankService(Path storagePath, String dbUrl, String dbUsername, String dbPassword) {
        this.customers = new ArrayList<>();
        this.storageManager = new StorageManager(storagePath);
        boolean dbAvailable;
        try {
            // Try to enable the database. If anything fails we fall back to the
            // in-memory collection so the UI still works.
            this.databaseManager = new DatabaseManager(dbUrl, dbUsername, dbPassword);
            databaseManager.initializeSchema();
            customers.addAll(databaseManager.loadCustomers());
            dbAvailable = true;
        } catch (IllegalStateException | SQLException ex) {
            dbAvailable = false;
            databaseManager = null;
            System.err.println("Database disabled. Running in memory only. Reason: " + ex.getMessage());
        }
        this.databaseEnabled = dbAvailable;
    }

    public IndividualCustomer registerIndividual(String firstName, String surname, String nationalId,
                                                 String dateOfBirth, String address, String phone,
                                                 String email, String username, String password,
                                                 boolean employed, String employerName) {
        requireText(firstName, "First name");
        requireText(surname, "Surname");
        requireText(nationalId, "National ID");
        requireText(dateOfBirth, "Date of birth");
        requireText(address, "Address");
        requireText(phone, "Phone");
        requireText(email, "Email");
        requireText(username, "Username");
        requireText(password, "Password");
        ensureUniqueUsername(username);
        ensureUniqueNationalId(nationalId);
        IndividualCustomer customer = new IndividualCustomer(firstName, surname, nationalId,
                dateOfBirth, address, username, password, email, phone, employed, employerName);
        customers.add(customer);
        persistIndividual(customer);
        return customer;
    }

    public CompanyCustomer registerCompany(String companyName, String registrationNumber,
                                           String contactPerson, String address, String phone,
                                           String email, String username, String password) {
        requireText(companyName, "Company name");
        requireText(registrationNumber, "Registration number");
        requireText(contactPerson, "Contact person");
        requireText(address, "Address");
        requireText(phone, "Phone");
        requireText(email, "Email");
        requireText(username, "Username");
        requireText(password, "Password");
        ensureUniqueUsername(username);
        ensureUniqueRegistrationNumber(registrationNumber);
        CompanyCustomer customer = new CompanyCustomer(companyName, registrationNumber,
                contactPerson, address, username, password, email, phone);
        customers.add(customer);
        persistCompany(customer);
        return customer;
    }

    public void login(String username, String password) {
        Customer found = findCustomerByUsername(username);
        if (found == null || !found.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        activeCustomer = found;
    }

    public void logout() {
        activeCustomer = null;
    }

    public boolean hasActiveCustomer() {
        return activeCustomer != null;
    }

    public Customer getActiveCustomer() {
        return activeCustomer;
    }

    public Account createAccount(AccountType type, double openingBalance, String employerName) {
        if (activeCustomer == null) {
            throw new IllegalStateException("Login before creating accounts.");
        }
        // AccountFactory enforces the account-specific rules (investment minimum, cheque employment, etc).
        Account account = AccountFactory.createAccount(type, activeCustomer, openingBalance, employerName);
        activeCustomer.addAccount(account);
        persistAccount(account);
        return account;
    }

    public List<Account> getAccountsForActiveCustomer() {
        if (activeCustomer == null) {
            return new ArrayList<>();
        }
        List<Account> accounts = new ArrayList<>();
        List<Account> raw = activeCustomer.getAccounts();
        for (int i = 0; i < raw.size(); i++) {
            accounts.add(raw.get(i));
        }
        return accounts;
    }

    public void deposit(Account account, double amount) {
        if (account == null) {
            throw new IllegalArgumentException("Select an account first.");
        }
        // Account handles validation (no negative deposits) and transaction recording.
        account.deposit(amount);
        persistTransaction(account);
    }

    public void withdraw(Account account, double amount) {
        if (account == null) {
            throw new IllegalArgumentException("Select an account first.");
        }
        if (!(account instanceof Withdrawable)) {
            throw new IllegalArgumentException("This account does not support withdrawals.");
        }
        Withdrawable withdrawable = (Withdrawable) account;
        withdrawable.withdraw(amount);
        persistTransaction(account);
    }

    public void applyMonthlyInterest() {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            List<Account> accounts = customer.getAccounts();
            for (int j = 0; j < accounts.size(); j++) {
                Account account = accounts.get(j);
                account.applyMonthlyInterest();
                persistTransaction(account);
            }
        }
    }

    public void saveSnapshot() throws IOException {
        storageManager.saveSnapshot(customers);
    }

    private void persistIndividual(IndividualCustomer customer) {
        if (!databaseEnabled) {
            return;
        }
        try {
            // Store the base customer row and the individual specific details.
            databaseManager.insertIndividual(customer);
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to save individual customer.", ex);
        }
    }

    private void persistCompany(CompanyCustomer customer) {
        if (!databaseEnabled) {
            return;
        }
        try {
            // Store the base customer row and the company specific details.
            databaseManager.insertCompany(customer);
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to save company customer.", ex);
        }
    }

    private void persistAccount(Account account) {
        if (!databaseEnabled) {
            return;
        }
        try {
            // Save the new account row and its opening transactions.
            databaseManager.insertAccount(account);
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to save account.", ex);
        }
    }

    private void persistTransaction(Account account) {
        if (!databaseEnabled) {
            return;
        }
        try {
            // Update balance and log the most recent transaction so statements remain accurate.
            databaseManager.updateAccount(account);
            List<Transaction> transactions = account.getTransactions();
            if (!transactions.isEmpty()) {
                Transaction latest = transactions.get(transactions.size() - 1);
                databaseManager.insertTransaction(latest);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to save transaction.", ex);
        }
    }

    public List<String> readSnapshotLines() throws IOException {
        return storageManager.readSnapshotLines();
    }

    public List<Customer> getCustomers() {
        List<Customer> clone = new ArrayList<>();
        for (int i = 0; i < customers.size(); i++) {
            clone.add(customers.get(i));
        }
        return Collections.unmodifiableList(clone);
    }

    public Account findAccountByNumber(String accountNumber) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            List<Account> accounts = customer.getAccounts();
            for (int j = 0; j < accounts.size(); j++) {
                Account account = accounts.get(j);
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        }
        return null;
    }

    public List<Transaction> getTransactions(Account account) {
        if (account == null) {
            return new ArrayList<>();
        }
        List<Transaction> txns = account.getTransactions();
        List<Transaction> clone = new ArrayList<>();
        for (int i = 0; i < txns.size(); i++) {
            clone.add(txns.get(i));
        }
        return clone;
    }

    private Customer findCustomerByUsername(String username) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer.getUsername().equalsIgnoreCase(username)) {
                return customer;
            }
        }
        return null;
    }

    private void ensureUniqueUsername(String username) {
        if (findCustomerByUsername(username) != null) {
            throw new IllegalArgumentException("Username already in use.");
        }
    }

    private void ensureUniqueNationalId(String nationalId) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer instanceof IndividualCustomer individual) {
                if (individual.getNationalId().equalsIgnoreCase(nationalId)) {
                    throw new IllegalArgumentException("National ID already registered.");
                }
            }
        }
    }

    private void ensureUniqueRegistrationNumber(String registrationNumber) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer instanceof CompanyCustomer company) {
                if (company.getRegistrationNumber().equalsIgnoreCase(registrationNumber)) {
                    throw new IllegalArgumentException("Registration number already registered.");
                }
            }
        }
    }

    private void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
    }
}

