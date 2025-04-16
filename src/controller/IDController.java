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
        List<Project> p = ProjectList.getInstance().getAll();
        setProjectCount(Integer.parseInt(p.get(p.size() - 1).getProjectID().substring(1)));
        List<Request> r = RequestList.getInstance().getAll();
        setRequestCount(Integer.parseInt(r.get(r.size() - 1).getRequestID().substring(1)));
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
