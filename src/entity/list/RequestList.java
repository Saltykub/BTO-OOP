public class RequestList extends ModelList {
    private String filePath;
    private List<Request> requests;

    public RequestList() {
        this.requests = new ArrayList<>();
    }

    public String getFilePath() {
        return filePath;
    }

    public Request getByID(String requestID) {
        for (Request request : requests) {
            if (request.getRequestID().equals(requestID)) {
                return request;
            }
        }
        return null;
    }
}
