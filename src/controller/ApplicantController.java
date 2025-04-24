package controller;

import java.time.LocalDate;
import java.util.List;

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

/**
 * Controller responsible for handling business logic related to actions performed by Applicants.
 * This includes checking project eligibility, viewing project lists, applying for projects,
 * withdrawing applications, and managing personal enquiries (view, create, edit, delete).
 * It operates based on the currently logged-in applicant's ID.
 */
public class ApplicantController {

    /**
     * Stores the user ID of the applicant currently interacting with the system.
     * This ID is typically set by the {@link AccountController} upon successful login.
     */
    private static String applicantID;

    /**
     * Sets the applicant ID for the current session context.
     * All subsequent operations in this controller will be performed for this applicant.
     *
     * @param ID The user ID of the currently logged-in applicant.
     */
    public static void setApplicantID(String ID) {
        applicantID = ID;
    }

    /**
     * Checks if the currently set applicant is eligible to apply for a given project.
     * Eligibility criteria include:
     * - Project visibility is true.
     * - Applicant is not an officer assigned to the project.
     * - Current date is within the project's application open and close dates.
     * - Applicant meets age and marital status requirements for specific flat types:
     * - Age >= 35 and Single: Eligible for TWO_ROOM.
     * - Age >= 21 and Married: Eligible for THREE_ROOM (implicitly includes TWO_ROOM).
     *
     * @param projectID The ID of the project to check eligibility for.
     * @return The maximum {@link FlatType} the applicant is eligible for (THREE_ROOM implies eligibility for TWO_ROOM as well),
     * or null if the applicant is not eligible for the project based on the criteria.
     */
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

    /**
     * Displays a list of projects that the current applicant is eligible to apply for.
     * The list is first filtered using {@link FilterController}.
     * For each eligible project, it indicates the maximum flat type the applicant can apply for.
     */
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

    /**
     * Displays the current applicant's pending and historical BTO applications and withdrawals.
     * Separates the display into current pending requests and completed request history.
     */
    public static void viewAppliedApplication(){
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println("                    Your Current Application");
        System.out.println(UIController.LINE_SEPARATOR);
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
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println("                     Your Application History");
        System.out.println(UIController.LINE_SEPARATOR);
        for (Request request : requests) {
            if (request.getUserID().contains(applicantID) && request.getRequestStatus() == RequestStatus.DONE && (request.getRequestType() == RequestType.BTO_APPLICATION || request.getRequestType() == RequestType.BTO_WITHDRAWAL)) {
                has = true;
                Display.displayRequest(request, UserType.APPLICANT);
            }
        }
        if (!has) System.out.println("No history");
    }

    /**
     * Displays details of the single project the applicant has currently applied to, if any.
     * Includes the current application status for that project.
     */
    public static void viewAppliedProject() {
        Applicant applicant = ApplicantList.getInstance().getByID(applicantID);
        Project currentProject = ProjectList.getInstance().getByID(applicant.getProject());
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println("                      Your Applied Project");
        System.out.println(UIController.LINE_SEPARATOR);
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

    /**
     * Processes a project application request for the current applicant.
     * Performs checks:
     * - Applicant hasn't already applied for another project.
     * - Project exists.
     * - Applicant is eligible for the specified project (using {@link #checkApplicable(String)}).
     * - The requested flat type has available units.
     * If all checks pass, updates the applicant's record (sets project, applied flat type, status to PENDING)
     * and creates a new {@link BTOApplication} request in the RequestList.
     *
     * @param projectID The ID of the project to apply for.
     * @param applyFlat The {@link FlatType} the applicant wants to apply for.
     * @throws ProjectNotFoundException If the specified projectID does not exist.
     */
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

    /**
     * Submits a request for the current applicant to withdraw their application from a specific project.
     * Performs checks:
     * - Project exists.
     * - Applicant has a pending application for this project.
     * - Applicant has not already submitted a withdrawal request for this project.
     * If checks pass, creates a new {@link BTOWithdrawal} request in the RequestList.
     *
     * @param projectID The ID of the project from which to withdraw the application.
     * @throws ProjectNotFoundException If the specified projectID does not exist.
     */
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

    /**
     * Submits an enquiry from the current applicant regarding a specific project.
     * Checks if the applicant is generally eligible (relevant) to enquire about the project using {@link #checkApplicable(String)}.
     * If relevant, creates a new {@link Enquiry} request in the RequestList.
     *
     * @param projectID The ID of the project the enquiry is about.
     * @param text      The text content of the enquiry.
     */
    public static void query(String projectID, String text) {
        if (checkApplicable(projectID) == null) {
            System.out.println("Unable to enquiry irrelevant project.");
            return;
        }
        RequestList.getInstance().add(new Enquiry(IDController.newRequestID(), RequestType.ENQUIRY, applicantID, projectID, RequestStatus.PENDING, text));
    }

    /**
     * Displays all enquiries previously submitted by the current applicant.
     */
    public static void viewQuery() {
        List<Request> list = RequestList.getInstance().getAll();
        boolean has = false;
        for (Request request : list) {
            if (request.getUserID().equals(applicantID) && request.getRequestType() == RequestType.ENQUIRY) {
                has = true;
                Display.displayRequest(request, UserType.APPLICANT);
            }
        }
        if(!has) System.out.println("You don't have enquries");
    }

    /**
     * Checks if the current applicant is allowed to modify (edit/delete) a specific enquiry.
     * Conditions for modification:
     * - Request ID must exist.
     * - Request must be an Enquiry.
     * - Enquiry status must be PENDING (cannot modify answered enquiries).
     * - Enquiry must belong to the current applicant.
     *
     * @param requestID The ID of the enquiry request to check.
     * @return true if the applicant is allowed to modify the enquiry, false otherwise.
     */
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
    /**
     * Edits the text content of an existing enquiry submitted by the current applicant.
     * First performs validation using {@link #checkQuery(String)}.
     * If validation passes, updates the enquiry in the RequestList with the new text.
     * @param requestID The ID of the enquiry request to check.
     * @param text The text content of the enquiry.
     */
    public static void editQuery(String requestID, String text) {
        if (!checkQuery(requestID)) return;
        RequestList.getInstance().update(requestID, new Enquiry(requestID, RequestType.ENQUIRY, applicantID, ApplicantList.getInstance().getByID(applicantID).getProject(), RequestStatus.PENDING, text));
        System.out.println("Successfully edited enquiry.");
    }

    /**
     * Deletes an existing enquiry submitted by the current applicant.
     * First performs validation using {@link #checkQuery(String)}.
     * If validation passes, removes the enquiry from the RequestList.
     *
     * @param requestID The ID of the enquiry to delete.
     */
    public static void deleteQuery(String requestID) {
        if (!checkQuery(requestID)) return;
        RequestList.getInstance().delete(requestID);
        System.out.println("Successfully deleted enquiry.");
    }
}
