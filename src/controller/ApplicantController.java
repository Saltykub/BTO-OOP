package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import entity.project.FlatType;
import entity.project.Project;
import entity.request.*;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.MaritalStatus;
import entity.user.UserType;
import exception.ProjectNotFoundException;
import utils.Display;
import utils.IDController;
import utils.UIController;
import entity.list.ApplicantList;
import entity.list.ProjectList;
import entity.list.RequestList;

public class ApplicantController {
    private static String applicantID;

    public static void setApplicantID(String ID) {
        applicantID = ID;
    }

    public static FlatType checkApplicable(String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        Applicant applicant = ApplicantList.getInstance().getByID(applicantID);
        if (project.getVisibility() && !project.getOfficerID().contains(applicantID) && project.getOpenDate().isBefore(LocalDate.now()) && project.getCloseDate().isAfter(LocalDate.now())) {
            if (applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
                return FlatType.TWO_ROOM;
            }
            else if (applicant.getAge() >= 21 && applicant.getMaritalStatus() == MaritalStatus.MARRIED) {
                return FlatType.THREE_ROOM;
            }
        }
        return null;
    }

    public static void viewApplicableProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        list = FilterController.filteredList(list);
        boolean has = false;
        for (Project project : list) {
            FlatType flatType = checkApplicable(project.getProjectID());
            if (flatType == FlatType.TWO_ROOM) {
                has = true;
                Display.displayProject(project, UserType.APPLICANT, FlatType.THREE_ROOM);
            }
            else if (flatType == FlatType.THREE_ROOM) {
                has = true;
                Display.displayProject(project, UserType.APPLICANT, null); 
            }
        }
        if (!has) System.out.println("There is no applicable project.");
    }

    public static void viewAppliedApplication(){
        System.out.println(UIController.lineSeparator);
        System.out.println("                    Your Current Application");
        System.out.println(UIController.lineSeparator);
        boolean has = false;
        List<Request> requests = RequestList.getInstance().getAll();
        for (Request request : requests) {
            if (request.getUserID().contains(applicantID) && request.getRequestStatus() == RequestStatus.PENDING && (request.getRequestType() == RequestType.BTO_APPLICATION || request.getRequestType() == RequestType.BTO_WITHDRAWAL)) {
                has = true;
                Display.displayRequest(request, UserType.APPLICANT);
            }
        }
        if (!has) System.out.println("You haven't applied to any project.");
        has = false;
        System.out.println(UIController.lineSeparator);
        System.out.println("                     Your Application History");
        System.out.println(UIController.lineSeparator);
        for (Request request : requests) {
            if (request.getUserID().contains(applicantID) && request.getRequestStatus() == RequestStatus.DONE && (request.getRequestType() == RequestType.BTO_APPLICATION || request.getRequestType() == RequestType.BTO_WITHDRAWAL)) {
                has = true;
                Display.displayRequest(request, UserType.APPLICANT);
            }
        }
        if (!has) System.out.println("No history");
    }
    
    public static void viewAppliedProject() {
        Applicant applicant = ApplicantList.getInstance().getByID(applicantID);
        Project currentProject = ProjectList.getInstance().getByID(applicant.getProject());
        System.out.println(UIController.lineSeparator);
        System.out.println("                      Your Applied Project");
        System.out.println(UIController.lineSeparator);
        if(currentProject == null){
            System.out.println("No applied project");
        }
        else {
            FlatType flatType = checkApplicable(currentProject.getProjectID());
                if (flatType == FlatType.TWO_ROOM) {
                    System.out.println("Status: " + applicant.getApplicationStatusByID(applicant.getProject()).coloredString());
                    Display.displayProject(currentProject, UserType.APPLICANT, FlatType.THREE_ROOM);
                    
                }
                else if (flatType == FlatType.THREE_ROOM) {
                    System.out.println("Status: " + applicant.getApplicationStatusByID(applicant.getProject()).coloredString());
                    Display.displayProject(currentProject, UserType.APPLICANT, null); 
            }
        }
    }

    public static void applyProject(String projectID, FlatType applyFlat) throws ProjectNotFoundException {   
        Applicant applicant = ApplicantList.getInstance().getByID(applicantID);
        if (applicant.getProject() != null) {
            System.out.println("You are allowed to apply for only one project.");
            return;
        }
        Project project = ProjectList.getInstance().getByID(projectID);
        if (project == null) throw new ProjectNotFoundException();  
        if (checkApplicable(projectID) == null) {
            System.out.println("You are not allowed to apply for this project.");
            return;
        }
        if (project.getAvailableUnit().get(applyFlat) == 0) {
            System.out.println("There is no available unit of this flat type in this project.");
            return;
        }
        applicant.setAppliedFlatByID(projectID, applyFlat);
        applicant.setProject(projectID);
        applicant.setApplicationStatusByID(projectID, ApplicationStatus.PENDING);
        ApplicantList.getInstance().update(applicantID, applicant);
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
        if (query == null) {
            System.out.println("This request ID is not existed.");
            return false;
        }
        if (!(query instanceof Enquiry)) {
            System.out.println("This request ID is not enquiry.");
            return false;
        }
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
