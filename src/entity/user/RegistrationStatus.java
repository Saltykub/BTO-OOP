package entity.user;

public enum RegistrationStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public String coloredString() {
        return switch (this) {
            case PENDING -> "\u001B[33m" + this + "\u001B[0m";
            case APPROVED -> "\u001B[32m" + this + "\u001B[0m"; 
            case REJECTED -> "\u001B[31m" + this + "\u001B[0m";
        };
    }
}
