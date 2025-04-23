package exception;
/**
 * Custom checked exception thrown when an attempt is made to register a user
 * whose user ID already exists within the system.
 */
public class AlreadyRegisteredException extends Exception {
    /**
     * Constructs a new {@code AlreadyRegisteredException} with a default detail message.
     * The default message is "This user ID is already registered.".
     */
    public AlreadyRegisteredException() {
        super("This user ID is already registerd.");
    }
}
