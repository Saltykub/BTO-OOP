package entity.request;
/**
 * Defines the essential contract for any object that represents a request
 * within the system. Implementing classes must provide methods to get and set
 * core request attributes like ID, type, associated user and project IDs, and status.
 */
public interface Requestable {
    public String getRequestID();

    // Setter for requestID
    public void setRequestID(String requestID);

    // Getter for requestType
    public RequestType getRequestType();
    
    // Setter for requestType
    public void setRequestType(RequestType requestType);

    // Getter for userID
    public String getUserID();

    // Setter for userID
    public void setUserID(String userID); 
    
    // Getter for projectID
    public String getProjectID(); 
    
    // Setter for projectID
    public void setProjectID(String projectID);
    
    // Getter for requestStatus
    public RequestStatus getRequestStatus();
    
    // Setter for requestStatus
    public void setRequestStatus(RequestStatus requestStatus);
}
