package entity.user;
/**
 * Represents the status of an Officer's registration request for a specific housing project.
 * Defines the possible states during the registration approval process.
 */
public enum RegistrationStatus {
    /**
     * The officer has submitted a request to register for the project, and it is awaiting review by a manager.
     */
    PENDING,
    /**
     * The officer's registration request has been approved by a manager; the officer is assigned to the project.
     */
    APPROVED,
    /**
     * The officer's registration request has been rejected by a manager.
     */
    REJECTED;
    /**
     * Returns a string representation of the status, formatted with ANSI escape codes
     * for colored console output.
     * <ul>
     * <li>PENDING: Yellow</li>
     * <li>APPROVED: Green</li>
     * <li>REJECTED: Red</li>
     * </ul>
     * Note: Color display depends on the console supporting ANSI escape codes.
     *
     * @return A colorized string representation of the enum constant name.
     */
    public String coloredString() {
        return switch (this) {
            case PENDING -> "\u001B[33m" + this + "\u001B[0m";
            case APPROVED -> "\u001B[32m" + this + "\u001B[0m"; 
            case REJECTED -> "\u001B[31m" + this + "\u001B[0m";
        };
    }
}
