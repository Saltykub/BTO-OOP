package controller;

public class IDController {
    private static int projectCount;
    private static int reqeustCount;

    public static void setProjectCount(int count) {
        projectCount = count;
    }

    public static void setRequestCount(int count) {
        reqeustCount = count;
    }

    public static String newProjectID() {
        String projectID = Integer.toString(++projectCount);
        while (projectID.length() < 4) {
            projectID = "0" + projectID;
        }
        return "P" + projectID;
    }

    public static String newRequestID() {
        String requestID = Integer.toString(++reqeustCount);
        while (requestID.length() < 4) {
            requestID = "0" + requestID;
        }
        return "R" + requestID;
    }
}
