public class BTOApplication extends Request {
    private ApprovedStatus applicationStatus;

    public ApprovedStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApprovedStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
