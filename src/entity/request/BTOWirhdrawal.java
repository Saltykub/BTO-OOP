public class BTOWithdrawal extends Request {
    private ApprovedStatus withdrawalStatus;

    public ApprovedStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(ApprovedStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }
}
