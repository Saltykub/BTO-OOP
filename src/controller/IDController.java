package controller;

import java.util.List;

import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.Project;
import entity.request.Request;

public class IDController {
    private static int projectCount;
    private static int reqeustCount;

    public static void init() {
        int cnt = 0;
        List<Project> projects = ProjectList.getInstance().getAll();
        for (Project project : projects) cnt = Math.max(cnt, Integer.parseInt(project.getProjectID().substring(1)));
        setProjectCount(cnt);
        cnt = 0;
        List<Request> requests = RequestList.getInstance().getAll();
        for (Request request : requests) cnt = Math.max(cnt, Integer.parseInt(request.getRequestID().substring(1)));
        setRequestCount(cnt);
    }

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
