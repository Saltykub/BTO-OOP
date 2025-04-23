package exception;
/**
 * Custom checked exception thrown during authentication processes (like login or password change verification)
 * when the provided password does not match the stored credentials for the user account.
 */
public class PasswordIncorrectException extends Exception {
    /**
     * Constructs a new {@code PasswordIncorrectException} with a default detail message.
     * The default message is "Password is incorrect.".
     */
    public PasswordIncorrectException() {
        super("Password is incorrect.");
    }
}