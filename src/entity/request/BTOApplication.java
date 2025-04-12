package entity.request;

public class BTOApplication extends Request {
    private ApprovedStatus applicationStatus;

    public BTOApplication(){
        super();
    }
    
    public BTOApplication(String requestID, RequestType requestType, String userID, String projectID, RequestStatus requestStatus){
        super(requestID, requestType, userID, projectID, requestStatus);
        this.applicationStatus = ApprovedStatus.PENDING;
    }

    public ApprovedStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApprovedStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
