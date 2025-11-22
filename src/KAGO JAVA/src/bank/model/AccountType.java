package bank.model;

/**
 * Simple account type enum.
 */
public enum AccountType {
    SAVINGS,
    INVESTMENT,
    CHEQUE;

    public static AccountType fromName(String name) {
        for (AccountType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown account type: " + name);
    }
}

