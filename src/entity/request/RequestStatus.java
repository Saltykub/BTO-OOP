package entity.request;

public enum RequestStatus {
    PENDING,
    DONE;

    public String coloredString() {
        return switch (this) {
            case PENDING -> "\u001B[33m" + this + "\u001B[0m";
            case DONE -> "\u001B[32m" + this + "\u001B[0m";
        };
    }
}
