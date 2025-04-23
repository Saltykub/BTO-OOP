package controller;

import java.util.List;

import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.Project;
import entity.request.Enquiry;
import entity.request.OfficerRegistration;
import entity.request.Request;
import entity.request.RequestStatus;
import entity.request.RequestType;
import entity.user.Officer;
import entity.user.RegistrationStatus;
import entity.user.UserType;
import utils.Display;
import utils.IDController;
import utils.UIController;

/**
 * Controller responsible for handling request-related operations specific to Officers.
 * This includes registering for projects, viewing registration status and assigned projects,
 * viewing enquiries for assigned projects, and answering those enquiries.
 * It operates using the context of the currently logged-in officer's ID.
 */
public class OfficerRequestController {
    /**
     * Stores the user ID of the officer currently interacting with the system.
     * This ID is typically set by the {@link AccountController} upon successful login of an Officer.
     */
    private static String officerID;

    /**
     * Sets the officer ID for the current session context.
     * Subsequent officer-specific operations in this controller will use this ID.
     *
     * @param ID The user ID of the currently logged-in officer.
     */
    public static void setOfficerID(String ID) {
        officerID = ID;
    }

    /**
     * Submits a registration request for the current officer to join a specific project.
     * Performs checks:
     * - Project exists and is visible.
     * - Officer is not already an applicant for the project.
     * - The project's dates do not overlap with any project the officer is already registered for.
     * If checks pass, updates the officer's status for this project to PENDING and creates
     * a new {@link OfficerRegistration} request in the {@link RequestList}.
     *
     * @param projectID The ID of the project the officer wants to register for.
     */
    public static void registerProject(String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        List<String> officerProject = OfficerList.getInstance().getByID(officerID).getOfficerProject();
        if (!project.getApplicantID().contains(officerID) && project.getVisibility()) {
            boolean can = true;
            for (String id : officerProject) {
                Project p = ProjectList.getInstance().getByID(id);
                if (p.getCloseDate().isBefore(project.getOpenDate()) || project.getCloseDate().isBefore(p.getOpenDate())) continue;
                can = false;
                break;
            }
            if (can) {
                Officer officer = OfficerList.getInstance().getByID(officerID);
                officer.setRegistrationStatusByID(projectID, RegistrationStatus.PENDING);
                OfficerList.getInstance().update(officerID, officer);
                RequestList.getInstance().add(new OfficerRegistration(IDController.newRequestID(), RequestType.REGISTRATION, officerID, projectID, RequestStatus.PENDING));
                System.out.println("Successfully applied registeration.");
                return;
            }
        }
        System.out.println("You are not allowed to apply for this project.");
    }

    /**
     * Displays a list of projects the current officer is successfully registered for.
     * Applies filters set in {@link FilterController} to the list before display.
     */
    public static void viewRegisteredProject() {
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println("                        Your Projects");
        System.out.println(UIController.LINE_SEPARATOR);
        boolean has = false;
        List<String> projects = OfficerList.getInstance().getByID(officerID).getOfficerProject();
        List<Project> list = FilterController.filteredListFromID(projects);
        for (Project project : list) {
            has = true;
            Display.displayProject(project, UserType.OFFICER, null);
        }
        if (!has) System.out.println("You don't have any project.");
    }

    /**
     * Displays the history of project registration requests submitted by the current officer.
     * Shows the status of each request (e.g., PENDING, APPROVED, REJECTED).
     */
    public static void viewRegistrationStatus() {
        List<Request> list = RequestList.getInstance().getAll();
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println("                      Your Registration History");
        System.out.println(UIController.LINE_SEPARATOR);
        boolean has = false;
        for (Request request : list) {
            if (request instanceof OfficerRegistration r) {
                if (r.getUserID().equals(officerID)) {
                    has = true;
                    Display.displayRequest(request, UserType.OFFICER);
                }
            }
        }
        if (!has) System.out.println("You haven't registered to any project.");
    }

    /**
     * Displays all enquiries associated with all projects the current officer is registered for.
     */
    public static void viewEnquiries() {
        List<Request> list = RequestList.getInstance().getAll();
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectID = o.getOfficerProject();
        boolean has = false;
        for (Request request : list) {
            for (String id : projectID) {
                if (request.getProjectID().equals(id) && request.getRequestType() == RequestType.ENQUIRY) {
                    has = true;
                    Display.displayRequest(request, UserType.OFFICER);
                    break;
                }
            }
        }
        if (!has) System.out.println("There is no enquiry.");
    }

    /**
     * Displays enquiries associated with a specific project ID,
     * but only if the current officer is registered for that project.
     *
     * @param projectID The ID of the project for which to view enquiries.
     */
    public static void viewEnquiries(String projectID) {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> project = officer.getOfficerProject();
        if (!project.contains(projectID)) {
            System.out.println("You are not allowed to view enquiries of other's project.");
            return;
        }
        List<Request> list = RequestList.getInstance().getAll();
        boolean has = false;
        for (Request request : list) {
            if (request.getProjectID().equals(projectID) && request.getRequestType() == RequestType.ENQUIRY) {
                has = true;
                Display.displayRequest(request, UserType.OFFICER);
            }
        }
        if (!has) System.out.println("There is no enquiry for this project.");
    }
    /**
     * Records an answer provided by the officer for a specific enquiry.
     * Updates the {@link Enquiry} object with the answer text and sets its status to DONE.
     * Note: This method assumes the {@code requestID} corresponds to a valid Enquiry
     * and performs no validation checks itself (checks might be done in the calling boundary class).
     *
     * @param requestID The ID of the {@link Enquiry} request to answer.
     * @param text The text of the answer provided by the officer.
     */
    public static void answerEnquiry(String requestID, String text) {
        Enquiry enquiry = (Enquiry) RequestList.getInstance().getByID(requestID);
        enquiry.setAnswer(text);
        enquiry.setRequestStatus(RequestStatus.DONE);
        RequestList.getInstance().update(requestID, enquiry);
    }
}