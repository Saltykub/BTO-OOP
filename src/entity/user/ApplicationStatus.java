package entity.user;

public enum ApplicationStatus {
    PENDING, SUCCESSFUL, UNSUCCESSFUL, BOOKED;

    public String coloredString() {
        return switch (this) {
            case PENDING -> "\u001B[33m" + this + "\u001B[0m";
            case SUCCESSFUL -> "\u001B[32m" + this + "\u001B[0m"; 
            case UNSUCCESSFUL -> "\u001B[31m" + this + "\u001B[0m";
            case BOOKED -> "\u001B[34m" + this + "\u001B[0m";
        };
    }
}
