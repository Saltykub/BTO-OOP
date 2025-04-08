package controller;

public class ManagerRequestController {
    private String managerID;
    
    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }
    
    public void viewRequest() {
        // To be implemented
    }
    
    public void viewRequest(boolean applicant) {
        // Overloaded implementation to be done
    }
    
    public void changeRequestStatus(String requestID) {}
    public void changeApplicantApplication(String requestID) {}
    public void viewAllEnquiries() {}
}
