package bank.model;

/**
 * Individual customer with national id and employment info.
 */
public class IndividualCustomer extends Customer {
    private final String firstName;
    private final String surname;
    private final String nationalId;
    private final String dateOfBirth;
    private boolean employed;
    private String employerName;

    public IndividualCustomer(String firstName, String surname, String nationalId,
                              String dateOfBirth, String address, String username, String password,
                              String email, String phone, boolean employed, String employerName) {
        this(null, firstName, surname, nationalId, dateOfBirth, address, username, password, email, phone, employed, employerName);
    }

    public IndividualCustomer(String customerId, String firstName, String surname, String nationalId,
                              String dateOfBirth, String address, String username, String password,
                              String email, String phone, boolean employed, String employerName) {
        super(customerId, address, username, password, email, phone);
        this.firstName = firstName;
        this.surname = surname;
        this.nationalId = nationalId;
        this.dateOfBirth = dateOfBirth;
        this.employed = employed;
        this.employerName = employerName;
    }

    public boolean isEmployed() {
        return employed;
    }

    public void setEmployed(boolean employed) {
        this.employed = employed;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String displayInfo() {
        return firstName + " " + surname + " (" + nationalId + ")";
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + surname;
    }
}

