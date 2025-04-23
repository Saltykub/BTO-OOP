package exception;
/**
 * Custom checked exception thrown when a provided user ID does not conform
 * to the expected format (e.g., NRIC format).
 * This indicates that the structure or pattern of the user ID is incorrect.
 */
public class InvalidUserFormatException extends Exception {
    /**
     * Constructs a new {@code InvalidUserFormatException} with a default detail message.
     * The default message is "Invalid userID format.
     UserID should be your NRIC.".
     * Note: NRIC (National Registration Identity Card) is specific to Singapore.
     */
    public InvalidUserFormatException() {
        super("Invalid userID format. UserID should be your NRIC.");
    }
}
