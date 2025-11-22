package bank.model;

/**
 * Company customer.
 */
public class CompanyCustomer extends Customer {
    private final String companyName;
    private final String registrationNumber;
    private final String contactPerson;

    public CompanyCustomer(String companyName, String registrationNumber, String contactPerson,
                           String address, String username, String password,
                           String email, String phone) {
        this(null, companyName, registrationNumber, contactPerson, address, username, password, email, phone);
    }

    public CompanyCustomer(String customerId, String companyName, String registrationNumber, String contactPerson,
                           String address, String username, String password,
                           String email, String phone) {
        super(customerId, address, username, password, email, phone);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.contactPerson = contactPerson;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    @Override
    public String displayInfo() {
        return companyName + " (" + registrationNumber + ")";
    }

    @Override
    public String getDisplayName() {
        return companyName;
    }

    @Override
    public boolean isCompany() {
        return true;
    }
}

