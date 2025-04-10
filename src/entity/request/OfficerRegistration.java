package entity.request;

public class OfficerRegistration extends Request {
    private ApprovedStatus registrationStatus;

    public OfficerRegistration(String requestID, RequestType requestType, String userID, String projectID, RequestStatus requestStatus) {
        super(requestID, requestType, userID, projectID, requestStatus);
        this.registrationStatus = ApprovedStatus.PENDING;
    }

    public ApprovedStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(ApprovedStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}
