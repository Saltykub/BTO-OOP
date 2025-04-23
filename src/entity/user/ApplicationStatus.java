package entity.user;
/**
 * Represents the status of an applicant's application for a specific housing project.
 * Defines the possible states throughout the application lifecycle.
 */
public enum ApplicationStatus {
    PENDING, SUCCESSFUL, UNSUCCESSFUL, BOOKED;
    /**
     * Returns a string representation of the status, formatted with ANSI escape codes
     * for colored console output.
     * <ul>
     * <li>PENDING: Yellow</li>
     * <li>SUCCESSFUL: Green</li>
     * <li>UNSUCCESSFUL: Red</li>
     * <li>BOOKED: Blue</li>
     * </ul>
     * Note: Color display depends on the console supporting ANSI escape codes.
     *
     * @return A colorized string representation of the enum constant name.
     */
    public String coloredString() {
        return switch (this) {
            case PENDING -> "\u001B[33m" + this + "\u001B[0m";
            case SUCCESSFUL -> "\u001B[32m" + this + "\u001B[0m"; 
            case UNSUCCESSFUL -> "\u001B[31m" + this + "\u001B[0m";
            case BOOKED -> "\u001B[34m" + this + "\u001B[0m";
        };
    }
}
