package bank.util;

import java.util.UUID;

/**
 * Generates simple identifiers.
 */
public final class IdGenerator {
    private IdGenerator() {
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }
}

