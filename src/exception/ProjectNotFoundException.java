package exception;

public class ProjectNotFoundException extends Exception {
    public ProjectNotFoundException() {
        super("No project with this ID.");
    }
}
