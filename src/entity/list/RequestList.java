package entity.list;

import entity.request.Request;

public class RequestList extends ModelList<Request> {
    private static String filePath;

    public RequestList(String filePath) {
        super(filePath, Request.class);
    }

    public static RequestList getInstance() {
        return new RequestList(filePath);
    }

    public String getFilePath() {
        return filePath;
    }

    public Request getByID(String requestID) {
        for (Request request : this.getAll()) {
            if (request.getRequestID().equals(requestID)) {
                return request;
            }
        }
        return null;
    }
}
