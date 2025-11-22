package bank.data;

import bank.model.Account;
import bank.model.AccountType;
import bank.model.CompanyCustomer;
import bank.model.Customer;
import bank.model.IndividualCustomer;
import bank.model.Transaction;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all JDBC interactions with the MySQL database.
 */
public class DatabaseManager {
    /**
     * JDBC connection settings injected from the service layer. These are kept
     * as simple Strings so the class can create new connections on demand.
     */
    private final String url;
    private final String username;
    private final String password;

    /**
     * Builds the manager and verifies that the MySQL driver is present. This
     * early check makes it easy to fall back when the driver is missing.
     */
    public DatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("MySQL JDBC driver not found on classpath.", ex);
        }
    }

    /**
     * Opens a new connection using the stored credentials.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Creates all schema objects if they do not exist. This keeps the setup
     * simple for beginners because no manual SQL scripts are required.
     */
    public void initializeSchema() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "customer_id VARCHAR(64) PRIMARY KEY," +
                    "type VARCHAR(16) NOT NULL," +
                    "username VARCHAR(64) NOT NULL UNIQUE," +
                    "password VARCHAR(128) NOT NULL," +
                    "address VARCHAR(255) NOT NULL," +
                    "email VARCHAR(128) NOT NULL," +
                    "phone VARCHAR(32) NOT NULL" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS individual_details (" +
                    "customer_id VARCHAR(64) PRIMARY KEY," +
                    "first_name VARCHAR(64) NOT NULL," +
                    "surname VARCHAR(64) NOT NULL," +
                    "national_id VARCHAR(64) NOT NULL UNIQUE," +
                    "date_of_birth VARCHAR(32) NOT NULL," +
                    "employed TINYINT(1) NOT NULL," +
                    "employer_name VARCHAR(128)," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS company_details (" +
                    "customer_id VARCHAR(64) PRIMARY KEY," +
                    "company_name VARCHAR(128) NOT NULL," +
                    "reg_number VARCHAR(64) NOT NULL UNIQUE," +
                    "contact_person VARCHAR(64) NOT NULL," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_number VARCHAR(64) PRIMARY KEY," +
                    "customer_id VARCHAR(64) NOT NULL," +
                    "account_type VARCHAR(16) NOT NULL," +
                    "balance DOUBLE NOT NULL," +
                    "branch VARCHAR(64) NOT NULL," +
                    "open_date DATE NOT NULL," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "transaction_id VARCHAR(64) PRIMARY KEY," +
                    "account_number VARCHAR(64) NOT NULL," +
                    "txn_date TIMESTAMP NOT NULL," +
                    "amount DOUBLE NOT NULL," +
                    "txn_type VARCHAR(32) NOT NULL," +
                    "resulting_balance DOUBLE NOT NULL," +
                    "FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE" +
                    ")");
        }
    }

    /**
     * Loads every customer, along with their accounts and transactions, from
     * the database so the in-memory model can be re-used by the UI.
     */
    public List<Customer> loadCustomers() throws SQLException {
        Map<String, Customer> customers = new LinkedHashMap<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM customers");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("customer_id");
                String type = rs.getString("type");
                String usernameValue = rs.getString("username");
                String passwordValue = rs.getString("password");
                String address = rs.getString("address");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                if ("individual".equalsIgnoreCase(type)) {
                    customers.put(id, loadIndividual(connection, id, usernameValue, passwordValue, address, email, phone));
                } else {
                    customers.put(id, loadCompany(connection, id, usernameValue, passwordValue, address, email, phone));
                }
            }
        }
        loadAccounts(customers);
        return new ArrayList<>(customers.values());
    }

    /**
     * Fetches the individual-only fields to rebuild an IndividualCustomer.
     */
    private Customer loadIndividual(Connection connection, String id, String username, String password,
                                    String address, String email, String phone) throws SQLException {
        String sql = "SELECT * FROM individual_details WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Missing individual details for customer " + id);
                }
                String firstName = rs.getString("first_name");
                String surname = rs.getString("surname");
                String nationalId = rs.getString("national_id");
                String dob = rs.getString("date_of_birth");
                boolean employed = rs.getBoolean("employed");
                String employerName = rs.getString("employer_name");
                return new IndividualCustomer(id, firstName, surname, nationalId, dob, address,
                        username, password, email, phone, employed, employerName);
            }
        }
    }

    /**
     * Fetches the company-only fields to rebuild a CompanyCustomer.
     */
    private Customer loadCompany(Connection connection, String id, String username, String password,
                                 String address, String email, String phone) throws SQLException {
        String sql = "SELECT * FROM company_details WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Missing company details for customer " + id);
                }
                String companyName = rs.getString("company_name");
                String regNumber = rs.getString("reg_number");
                String contactPerson = rs.getString("contact_person");
                return new CompanyCustomer(id, companyName, regNumber, contactPerson,
                        address, username, password, email, phone);
            }
        }
    }

    /**
     * Loads every account and attaches it to the already loaded customers.
     * Transactions are also fetched so statements show accurately in the UI.
     */
    private void loadAccounts(Map<String, Customer> customers) throws SQLException {
        String sql = "SELECT * FROM accounts";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String accountNumber = rs.getString("account_number");
                String customerId = rs.getString("customer_id");
                Customer owner = customers.get(customerId);
                if (owner == null) {
                    continue;
                }
                AccountType type = AccountType.fromName(rs.getString("account_type"));
                double balance = rs.getDouble("balance");
                LocalDate openedDate = rs.getDate("open_date").toLocalDate();
                Account account = AccountFactory.rebuildAccount(type, owner, accountNumber, balance, openedDate);
                List<Transaction> transactions = fetchTransactions(connection, accountNumber);
                account.getTransactions().clear();
                account.getTransactions().addAll(transactions);
                owner.addAccount(account);
            }
        }
    }

    /**
     * Reads the chronological transactions for a specific account.
     */
    private List<Transaction> fetchTransactions(Connection connection, String accountNumber) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY txn_date ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String txnId = rs.getString("transaction_id");
                    LocalDateTime timestamp = rs.getTimestamp("txn_date").toLocalDateTime();
                    double amount = rs.getDouble("amount");
                    String type = rs.getString("txn_type");
                    double resultingBalance = rs.getDouble("resulting_balance");
                    transactions.add(Transaction.fromDatabase(txnId, accountNumber, timestamp, amount, type, resultingBalance));
                }
            }
        }
        return transactions;
    }

    public void insertIndividual(IndividualCustomer customer) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                // First write the base customer row.
                insertCustomerRow(connection, customer.getCustomerId(), "individual",
                        customer.getUsername(), customer.getPassword(),
                        customer.getAddress(), customer.getEmail(), customer.getPhone());
                // Then insert fields specific to individuals.
                String sql = "INSERT INTO individual_details(customer_id, first_name, surname, national_id, date_of_birth, employed, employer_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, customer.getCustomerId());
                    stmt.setString(2, customer.getFirstName());
                    stmt.setString(3, customer.getSurname());
                    stmt.setString(4, customer.getNationalId());
                    stmt.setString(5, customer.getDateOfBirth());
                    stmt.setBoolean(6, customer.isEmployed());
                    stmt.setString(7, customer.getEmployerName());
                    stmt.executeUpdate();
                }
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public void insertCompany(CompanyCustomer customer) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                // Base customer row first.
                insertCustomerRow(connection, customer.getCustomerId(), "company",
                        customer.getUsername(), customer.getPassword(),
                        customer.getAddress(), customer.getEmail(), customer.getPhone());
                // Then the company specific data.
                String sql = "INSERT INTO company_details(customer_id, company_name, reg_number, contact_person) " +
                        "VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, customer.getCustomerId());
                    stmt.setString(2, customer.getCompanyName());
                    stmt.setString(3, customer.getRegistrationNumber());
                    stmt.setString(4, customer.getContactPerson());
                    stmt.executeUpdate();
                }
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Helper that writes common customer fields so both customer types reuse
     * the same method.
     */
    private void insertCustomerRow(Connection connection, String id, String type, String username,
                                   String password, String address, String email, String phone) throws SQLException {
        String sql = "INSERT INTO customers(customer_id, type, username, password, address, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, type);
            stmt.setString(3, username);
            stmt.setString(4, password);
            stmt.setString(5, address);
            stmt.setString(6, email);
            stmt.setString(7, phone);
            stmt.executeUpdate();
        }
    }

    /**
     * Inserts a new account row and also saves every starting transaction so
     * the UI immediately shows the opening deposit.
     */
    public void insertAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts(account_number, customer_id, account_type, balance, branch, open_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getOwner().getCustomerId());
            stmt.setString(3, detectAccountType(account));
            stmt.setDouble(4, account.getBalance());
            stmt.setString(5, "Main");
            stmt.setDate(6, Date.valueOf(account.getOpenedDate()));
            stmt.executeUpdate();
        }
        List<Transaction> transactions = account.getTransactions();
        for (int i = 0; i < transactions.size(); i++) {
            insertTransaction(transactions.get(i));
        }
    }

    /**
     * Updates an account balance after a deposit/withdrawal/interest event.
     */
    public void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, account.getBalance());
            stmt.setString(2, account.getAccountNumber());
            stmt.executeUpdate();
        }
    }

    /**
     * Records a single transaction row linked back to the owning account.
     */
    public void insertTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions(transaction_id, account_number, txn_date, amount, txn_type, resulting_balance) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, transaction.getAccountNumber());
            stmt.setTimestamp(3, Timestamp.valueOf(transaction.getTimestamp()));
            stmt.setDouble(4, transaction.getAmount());
            stmt.setString(5, transaction.getType());
            stmt.setDouble(6, transaction.getResultingBalance());
            stmt.executeUpdate();
        }
    }

    /**
     * Converts the account class name into the enum text stored in the
     * accounts table.
     */
    private String detectAccountType(Account account) {
        String simpleName = account.getClass().getSimpleName().replace("Account", "");
        AccountType type = AccountType.fromName(simpleName);
        return type.name();
    }
}

