package exception;

public class AlreadyRegisteredException extends Exception {
    public AlreadyRegisteredException() {
        super("This user ID is already registerd.");
    }
}
