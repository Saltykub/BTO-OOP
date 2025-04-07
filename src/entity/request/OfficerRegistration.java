package entity.request;

public class OfficerRegistration extends Request {
    private ApprovedStatus registrationStatus;

    public ApprovedStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(ApprovedStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}
