package entity.request;

public class Enquiry extends Request {
    private String query;
    private String answer;

    public Enquiry(){
        super();
    }
    
    public Enquiry(String requestID, RequestType requestType, String userID, String projectID, RequestStatus requestStatus, String query){
        super(requestID, requestType, userID, projectID, requestStatus);
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
