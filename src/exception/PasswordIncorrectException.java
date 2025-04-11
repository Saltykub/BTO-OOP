package exception;

public class PasswordIncorrectException extends Exception {
    public PasswordIncorrectException() {
        super("Password is incorrect.");
    }
}