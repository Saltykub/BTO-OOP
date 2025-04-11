package exception;

public class InvalidUserFormatException extends Exception {
    public InvalidUserFormatException() {
        super("Invalid userID format. UserID should be your NRIC.");
    }
}
