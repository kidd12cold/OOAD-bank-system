package bank.model;

/**
 * Allows accounts that support withdrawals to offer a withdraw method.
 */
public interface Withdrawable {
    /**
     * Withdraws the requested amount if rules allow it.
     *
     * @param amount amount to withdraw
     * @throws IllegalArgumentException when rules are broken
     */
    void withdraw(double amount);
}

