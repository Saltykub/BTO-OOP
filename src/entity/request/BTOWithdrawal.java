package entity.request;

public class BTOWithdrawal extends Request {
    private ApprovedStatus withdrawalStatus;

    public BTOWithdrawal(String requestID, RequestType requestType, String userID, String projectID, RequestStatus requestStatus){
        super(requestID, requestType, userID, projectID, requestStatus);
        this.withdrawalStatus = ApprovedStatus.PENDING;
    }
    public ApprovedStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(ApprovedStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }
}
