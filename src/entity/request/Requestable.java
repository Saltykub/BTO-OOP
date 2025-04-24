package entity.request;

/**
 * Defines the essential contract for any object that represents a request
 * within the system. Implementing classes must provide methods to get and set
 * core request attributes like ID, type, associated user and project IDs, and status.
 */
public interface Requestable {
    /**
     * Gets the unique identifier for this request.
     *
     * @return The request ID string.
     */
    public String getRequestID();

    // Setter for requestID
     /**
     * Sets the unique identifier for this request.
     *
     * @param requestID The request ID string to set.
     */
    public void setRequestID(String requestID);

    // Getter for requestType
    /**
     * Gets the type of this request.
     *
     * @return The {@link RequestType} enum value indicating the request's purpose.
     */
    public RequestType getRequestType();
    
    // Setter for requestType
    /**
     * Sets the type of this request.
     *
     * @param requestType The {@link RequestType} enum value to set.
     */
    public void setRequestType(RequestType requestType);

    // Getter for userID
    /**
     * Gets the user ID associated with this request (e.g., the user who submitted it).
     *
     * @return The user ID string.
     */
    public String getUserID();

    // Setter for userID
    /**
     * Sets the user ID associated with this request.
     *
     * @param userID The user ID string to set.
     */
    public void setUserID(String userID); 
    
    // Getter for projectID
    /**
     * Gets the project ID related to this request, if applicable.
     *
     * @return The project ID string. May be null or empty if the request is not project-specific.
     */
    public String getProjectID(); 
    
    // Setter for projectID
    /**
     * Sets the project ID related to this request.
     *
     * @param projectID The project ID string to set.
     */
    public void setProjectID(String projectID);
    
    // Getter for requestStatus
    /**
     * Gets the overall processing status of this request (e.g., PENDING, DONE).
     *
     * @return The {@link RequestStatus} enum value.
     */
    public RequestStatus getRequestStatus();
    
    // Setter for requestStatus
    /**
     * Sets the overall processing status of this request.
     *
     * @param requestStatus The {@link RequestStatus} enum value to set.
     */
    public void setRequestStatus(RequestStatus requestStatus);
}
