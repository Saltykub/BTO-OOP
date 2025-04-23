package entity.request;
/**
 * Defines the different categories or types of requests that can be initiated
 * and processed within the housing application system.
 * This helps differentiate the purpose and handling logic for various requests.
 */
public enum RequestType {
    BTO_APPLICATION,  // Represents a BTO application request
    BTO_WITHDRAWAL,   // Represents a BTO withdrawal request
    REGISTRATION,     // Represents a registration request
    ENQUIRY,
    NONE,          // Represents an enquiry request
}
