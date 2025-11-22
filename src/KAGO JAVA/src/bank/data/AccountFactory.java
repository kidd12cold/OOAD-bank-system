package bank.data;

import java.time.LocalDate;

import bank.model.Account;
import bank.model.AccountType;
import bank.model.ChequeAccount;
import bank.model.Customer;
import bank.model.IndividualCustomer;
import bank.model.InvestmentAccount;
import bank.model.SavingsAccount;
import bank.util.IdGenerator;

/**
 * Creates accounts while enforcing business rules.
 */
public final class AccountFactory {
    private AccountFactory() {
    }

    public static Account createAccount(AccountType type, Customer owner, double openingBalance,
                                        String employerName) {
        String number = "AC-" + IdGenerator.newId().substring(0, 8);
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(number, owner, openingBalance);
            case INVESTMENT:
                return new InvestmentAccount(number, owner, openingBalance);
            case CHEQUE:
                if (owner instanceof IndividualCustomer) {
                    IndividualCustomer individual = (IndividualCustomer) owner;
                    if (!individual.isEmployed()) {
                        throw new IllegalArgumentException("Cheque account requires employment.");
                    }
                    if (employerName != null && !employerName.isEmpty()) {
                        individual.setEmployerName(employerName);
                    }
                } else if (!owner.isCompany()) {
                    throw new IllegalArgumentException("Cheque account needs employment or company.");
                }
                return new ChequeAccount(number, owner, openingBalance);
            default:
                throw new IllegalArgumentException("Unsupported account type.");
        }
    }

    public static Account rebuildAccount(AccountType type, Customer owner, String accountNumber,
                                         double balance, LocalDate openedDate) {
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(accountNumber, owner, balance, openedDate, false);
            case INVESTMENT:
                return new InvestmentAccount(accountNumber, owner, balance, openedDate, false);
            case CHEQUE:
                return new ChequeAccount(accountNumber, owner, balance, openedDate, false);
            default:
                throw new IllegalArgumentException("Unsupported account type.");
        }
    }
}

