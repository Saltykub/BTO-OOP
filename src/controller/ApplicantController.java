package controller;

import java.time.LocalDate;
import java.util.List;

import boundary.Display;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.*;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.MaritalStatus;
import entity.user.UserType;
import exception.ProjectNotFoundException;
import entity.list.ApplicantList;
import entity.list.ProjectList;
import entity.list.RequestList;

public class ApplicantController {
    private static String applicantID;

    public static void setApplicantID(String ID) {
        applicantID = ID;
    }

    public static boolean checkApplicable(String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        Applicant applicant = ApplicantList.getInstance().getByID(applicantID);
        if (project.getVisibility() && !project.getOfficerID().contains(applicantID) && project.getOpenDate().isBefore(LocalDate.now()) && project.getCloseDate().isAfter(LocalDate.now())) {
            if (applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE && project.getAvailableUnit().get(FlatType.TWO_ROOM) == 0) {
                return false;
            }
            else if (applicant.getAge() >= 21 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
                return false;
            }
            else if (applicant.getAge() < 21) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static void viewApplicableProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        for (Project project : list) {
            if (checkApplicable(project.getProjectID())) {
                Display.displayProject(project,UserType.APPLICANT,null);
            }
        }
    }

    public static void viewAppliedProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        for (Project project : list) {
            if (project.getApplicantID().contains(applicantID) && project.getVisibility()) {
                Display.displayProject(project,UserType.APPLICANT,null);
            }
        }
        List<Request> requests = RequestList.getInstance().getAll();
        for (Request request : requests) {
            if (request.getUserID().contains(applicantID) && request.getRequestType() == RequestType.BTO_APPLICATION) {
                Display.displayRequest(request, UserType.APPLICANT);
            }
        }
    }

    public static void applyProject(String projectID) throws ProjectNotFoundException {   
        for (Request r : RequestList.getInstance().getAll()) {
            if (r.getUserID().equals(applicantID) && r.getRequestType() == RequestType.BTO_APPLICATION) {
                System.out.println("You are allowed to apply for only one project.");
                return;
            }
        }
        if (ProjectList.getInstance().getByID(projectID) == null) throw new ProjectNotFoundException();  
        if (!checkApplicable(projectID)) {
            System.out.println("You are not allowed to apply for this project.");
            return;
        }
        RequestList.getInstance().add(new BTOApplication(IDController.newRequestID(), RequestType.BTO_APPLICATION, applicantID, projectID, RequestStatus.PENDING));
        System.out.println("Successfully applied for this project.");
    }

    public static void withdrawApplication(String projectID) throws ProjectNotFoundException {
        if (ProjectList.getInstance().getByID(projectID) == null) throw new ProjectNotFoundException();   
        boolean alr = false, can = false;  
        for (Request r : RequestList.getInstance().getAll()) {
            if (r.getProjectID().equals(projectID) && r.getUserID().equals(applicantID)) {
                if (r instanceof BTOApplication) {
                    can = true;
                }
                else if (r instanceof BTOWithdrawal) {
                    alr = true;
                }
            }
        }
        if (can && !alr) {
            RequestList.getInstance().add(new BTOWithdrawal(IDController.newRequestID(), RequestType.BTO_WITHDRAWAL, applicantID, projectID, RequestStatus.PENDING));
            System.out.println("Successfully request for withdrawal.");
        }
        else if (!can) {
            System.out.println("You haven't applied to this project.");
        }
        else if (alr) {
            System.out.println("You already applied withdrawal application for this project.");
        }
    }
    
    public static void query(String projectID, String text) {
        RequestList.getInstance().add(new Enquiry(IDController.newRequestID(), RequestType.ENQUIRY, applicantID, projectID, RequestStatus.PENDING, text));
    }

    public static void viewQuery() {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request.getUserID().equals(applicantID) && request.getRequestType() == RequestType.ENQUIRY) {
                Display.displayRequest(request, UserType.APPLICANT);
            }
        }
    }

    public static boolean checkQuery(String requestID) {
        Request query = RequestList.getInstance().getByID(requestID);
        if (query.getRequestStatus() == RequestStatus.DONE) {
            System.out.println("You are not allowed to edit successful enquiry.");
            return false;
        }
        if (!query.getUserID().equals(applicantID)) {
            System.out.println("You are not allowed to edit other's enquiry.");
            return false;
        }
        return true;
    }

    public static void editQuery(String requestID, String text) {
        if (!checkQuery(requestID)) return;
        RequestList.getInstance().update(requestID, new Enquiry(requestID, RequestType.ENQUIRY, applicantID, ApplicantList.getInstance().getByID(applicantID).getProject(), RequestStatus.PENDING, text));
        System.out.println("Successfully edited enquiry.");
    }

    public static void deleteQuery(String requestID) {
        if (!checkQuery(requestID)) return;
        RequestList.getInstance().delete(requestID);
        System.out.println("Successfully deleted enquiry.");
    }
}
