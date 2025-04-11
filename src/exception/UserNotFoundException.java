package exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("No user with this ID.");
    }
}
