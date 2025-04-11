package controller;

import java.util.List;

import entity.project.FlatType;
import entity.project.Project;
import entity.request.*;
import entity.user.Applicant;
import entity.user.MaritalStatus;
import entity.list.ApplicantList;
import entity.list.ProjectList;
import entity.list.RequestList;

public class ApplicantController {
    private static String applicantID;

    public static void setApplicantID(String ID) {
        applicantID = ID;
    }

    public static void viewApplicableProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        Applicant applicant = ApplicantList.getInstance().getByID(applicantID);
        for (Project project : list) {
            if (project.getVisibility() && !project.getOfficerID().contains(applicantID)) {
                boolean available = true;
                if (applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE && project.getAvailableUnit().get(FlatType.TWO_ROOM) == 0) {
                    available = false;
                }
                else if (applicant.getAge() >= 21 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
                    available = false;
                }
                else if (applicant.getAge() < 21) {
                    available = false;
                }
                if (available) System.out.println(project);
            }
        }
    }

    public static void viewAppliedProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        for (Project project : list) {
            if (project.getApplicantID().contains(applicantID) && project.getVisibility()) {
                System.out.println(project);
            }
        }
    }

    public static void applyProject(String projectID) {
        RequestList.getInstance().add(new BTOApplication(IDController.newRequestID(), RequestType.BTO_APPLICATION, applicantID, projectID, RequestStatus.PENDING));
    }

    public static void withdrawApplication() {
        RequestList.getInstance().add(new BTOWithdrawal(IDController.newRequestID(), RequestType.BTO_WITHDRAWAL, applicantID, ApplicantList.getInstance().getByID(applicantID).getProject(), RequestStatus.PENDING));
    }
    
    public static void query(String text) {
        RequestList.getInstance().add(new Enquiry(IDController.newRequestID(), RequestType.ENQUIRY, applicantID, ApplicantList.getInstance().getByID(applicantID).getProject(), RequestStatus.PENDING, text));
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
        RequestList.getInstance().update(requestID, new Enquiry(requestID, RequestType.ENQUIRY, applicantID, ApplicantList.getInstance().getByID(applicantID).getProject(), RequestStatus.PENDING, text));
    }

    public static void deleteQuery(String requestID) {
        RequestList.getInstance().delete(requestID);
    }
}
