package controller;

import java.util.List;

import entity.project.Project;
import entity.request.*;
import entity.list.ApplicantList;
import entity.list.ProjectList;
import entity.list.RequestList;

public class ApplicantController {
    private static String applicantID;

    public static void setApplicantID(String ID) {
        applicantID = ID;
    }

    public static void viewProjectList() {
        List<Project> list = ProjectList.getInstance().getAll();
        for (Project project : list) {
            if (project.getApplicantID().contains(applicantID) && project.getVisibility()) {
                System.out.println(project);
            }
        }
    }

    public static void applyProject(String projectID) {
        // TODO: check condition
        RequestList.getInstance().add(new BTOApplication(IDController.newRequestID(), RequestType.BTO_APPLICATION, applicantID, projectID, RequestStatus.PENDING));
    }

    public static void withdrawApplication() {
        RequestList.getInstance().add(new BTOWithdrawal(IDController.newRequestID(), RequestType.BTO_WITHDRAWAL, applicantID, ApplicantList.getInstance().getById(applicantID).getProject().getProjectID(), RequestStatus.PENDING));
    }
    
    public static void query(String text) {
        RequestList.getInstance().add(new Enquiry(IDController.newRequestID(), RequestType.ENQUIRY, applicantID, ApplicantList.getInstance().getById(applicantID).getProject().getProjectID(), RequestStatus.PENDING, text));
    }

    public static void viewQuery() {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request.getUserID().equals(applicantID) && request.getRequestType() == RequestType.ENQUIRY) {
                System.out.println(request);
            }
        }
    }

    public static void editQuery(String requestID, String text) {
        RequestList.getInstance().update(requestID, new Enquiry(requestID, RequestType.ENQUIRY, applicantID, ApplicantList.getInstance().getById(applicantID).getProject().getProjectID(), RequestStatus.PENDING, text));
    }

    public static void deleteQuery(String requestID) {
        RequestList.getInstance().delete(requestID);
    }
}
