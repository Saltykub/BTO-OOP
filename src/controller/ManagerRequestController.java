package controller;

import java.util.List;

import entity.list.ApplicantList;
import entity.list.ManagerList;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.ApprovedStatus;
import entity.request.BTOApplication;
import entity.request.BTOWithdrawal;
import entity.request.Enquiry;
import entity.request.OfficerRegistration;
import entity.request.Request;
import entity.request.RequestStatus;
import entity.request.RequestType;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.Manager;
import entity.user.Officer;
import entity.user.RegistrationStatus;
import entity.user.UserType;
import utils.Display;

/**
 * Controller responsible for handling request-related operations from a Manager's perspective.
 * This includes viewing various request types (applications, withdrawals, registrations),
 * viewing enquiries associated with the manager's projects, viewing all enquiries,
 * and changing the status of different request types, often involving complex cascading logic.
 * It operates using the context of the currently logged-in manager's ID.
 */
public class ManagerRequestController {

    private static String managerID;

    /**
     * Sets the manager ID for the current session context.
     * Subsequent manager-specific operations in this controller will use this ID.
     *
     * @param ID The user ID of the currently logged-in manager.
     */
    public static void setManagerID(String ID) {
        managerID = ID;
    }
    /**
     * Stores the user ID of the manager currently interacting with the system.
    */
    public static void viewRequest() {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request.getRequestType() != RequestType.ENQUIRY) {
                Display.displayRequest(request, UserType.MANAGER);
            }
        }
    }

    /**
     * Displays specific types of requests using the APPLICANT display format.
     * The behavior depends on the boolean parameter:
     * - If {@code applicant} is true, displays BTO Applications and BTO Withdrawals.
     * - If {@code applicant} is false, displays Officer Registrations.
     * Note: The use of {@code UserType.APPLICANT} for display might be inconsistent, especially for Officer Registrations.
     *
     * @param applicant If true, display applicant-related requests (BTO Application/Withdrawal);
     * if false, display officer-related requests (Registration).
     */
    public static void viewRequest(boolean applicant) {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if ((applicant && (request.getRequestType() == RequestType.BTO_APPLICATION || request.getRequestType() == RequestType.BTO_WITHDRAWAL)) || (!applicant && request.getRequestType() == RequestType.REGISTRATION)) {
                Display.displayRequest(request, UserType.APPLICANT);
            }
        }
    }

    /**
     * Changes the overall status (e.g., PENDING, DONE) of a specific request.
     *
     * @param requestID The ID of the request to update.
     * @param status    The new {@link RequestStatus} to set.
     */
    public static void changeRequestStatus(String requestID, RequestStatus status) {
        Request request = RequestList.getInstance().getByID(requestID);
        request.setRequestStatus(status);
        RequestList.getInstance().update(requestID, request);
    }

    /**
     * Changes the approval status (PENDING, SUCCESSFUL, UNSUCCESSFUL) for specific types of applications
     * (BTO Application, BTO Withdrawal, Officer Registration) and triggers related cascading actions.
     * Also updates the overall request status to DONE if the approval status is not PENDING.
     *
     * @param requestID The ID of the application request to update.
     * @param status    The new {@link ApprovedStatus} to set.
     */
    public static void changeApplicationStatus(String requestID, ApprovedStatus status) {
        Request request = RequestList.getInstance().getByID(requestID);
        if (request instanceof BTOApplication application) {
            String projectID = request.getProjectID();
            Applicant applicant = ApplicantList.getInstance().getByID(request.getUserID());
            if (status == ApprovedStatus.SUCCESSFUL) applicant.setApplicationStatusByID(projectID, ApplicationStatus.SUCCESSFUL);
            else if (status == ApprovedStatus.UNSUCCESSFUL) applicant.setApplicationStatusByID(projectID, ApplicationStatus.UNSUCCESSFUL);
            else if (status == ApprovedStatus.PENDING) applicant.setApplicationStatusByID(projectID, ApplicationStatus.PENDING);
            ApplicantList.getInstance().update(request.getUserID(), applicant);
            application.setApplicationStatus(status);
            RequestList.getInstance().update(requestID, application);
        }
        else if (request instanceof BTOWithdrawal application) {
            if (status == ApprovedStatus.SUCCESSFUL) {
                Request bto = null;
                for (Request r : RequestList.getInstance().getAll()) {
                    if (r.getProjectID().equals(request.getProjectID()) && r.getUserID().equals(request.getUserID()) && r.getRequestType() == RequestType.BTO_APPLICATION) {
                        bto = r; 
                    }
                }
                if (bto != null) {
                    changeApplicationStatus(bto.getRequestID(), ApprovedStatus.UNSUCCESSFUL);
                    Applicant applicant = ApplicantList.getInstance().getByID(request.getUserID());
                    applicant.setProject(null);
                    String projectID = request.getProjectID();
                    if (applicant.getApplicationStatusByID(projectID) == ApplicationStatus.BOOKED) {
                        Project project = ProjectList.getInstance().getByID(projectID);
                        List<String> a = project.getApplicantID();
                        a.remove(applicant.getUserID());
                        FlatType flat = applicant.getAppliedFlatByID(projectID);
                        project.setApplicantID(a);
                        project.setAvailableUnit(flat, project.getAvailableUnit().get(flat) + 1);
                        ProjectList.getInstance().update(projectID, project);
                    }
                    applicant.setApplicationStatusByID(request.getProjectID(), ApplicationStatus.UNSUCCESSFUL);
                    ApplicantList.getInstance().update(request.getUserID(), applicant);
                }
            }
            application.setWithdrawalStatus(status);
            RequestList.getInstance().update(requestID, application);
        }
        else if (request instanceof OfficerRegistration application) {
            Officer officer = OfficerList.getInstance().getByID(request.getUserID());
            if (status == ApprovedStatus.SUCCESSFUL) {
                officer.setRegistrationStatusByID(request.getProjectID(), RegistrationStatus.APPROVED);
                Project p = ProjectList.getInstance().getByID(request.getProjectID());
                if (p.getAvailableOfficer() == 0) {
                    System.out.println("There is no available vacancy for officer in this project!");
                    return;
                }
                List<String> o = p.getOfficerID();
                if (!o.contains(officer.getUserID())) {
                    o.add(officer.getUserID());
                    p.setOfficerID(o);
                    p.setAvailableOfficer(p.getAvailableOfficer() - 1);
                    ProjectList.getInstance().update(request.getProjectID(), p);
                }
                List<String> project = officer.getOfficerProject();
                if (!project.contains(request.getProjectID())) {
                    project.add(request.getProjectID());
                    officer.setOfficerProject(project);
                }   
            }
            else if (status == ApprovedStatus.UNSUCCESSFUL) {
                officer.setRegistrationStatusByID(request.getProjectID(), RegistrationStatus.REJECTED); 
                Project p = ProjectList.getInstance().getByID(request.getProjectID());
                List<String> o = p.getOfficerID();
                if (o.contains(officer.getUserID())) {
                    o.remove(officer.getUserID());
                    p.setOfficerID(o);
                    p.setAvailableOfficer(p.getAvailableOfficer() + 1);
                    ProjectList.getInstance().update(request.getProjectID(), p);
                }
                List<String> project = officer.getOfficerProject();
                if (project.contains(request.getProjectID())) {
                    project.remove(request.getProjectID());
                    officer.setOfficerProject(project);
                }
            }
            OfficerList.getInstance().update(officer.getUserID(), officer);
            application.setRegistrationStatus(status);
            RequestList.getInstance().update(requestID, application);
        }
        System.out.println("Successfully change application status.");
        if (status != ApprovedStatus.PENDING) changeRequestStatus(requestID, RequestStatus.DONE);
        else changeRequestStatus(requestID, RequestStatus.PENDING);
    }

    /**
     * Displays all enquiries associated with the projects managed by the currently set manager.
     * Iterates through all requests, checks if it's an Enquiry, and if its project ID
     * is in the current manager's list of managed projects.
     */
    public static void viewEnquiries() {
        List<Request> list = RequestList.getInstance().getAll();
        Manager m = ManagerList.getInstance().getByID(managerID);
        List<String> projectID = m.getProject();
        boolean has = false;
        for (Request request : list) {
            for (String id : projectID) {
                if (request.getProjectID().equals(id) && request.getRequestType() == RequestType.ENQUIRY) {
                    has = true;
                    Display.displayRequest(request, UserType.MANAGER);
                    break;
                }
            }
        }
        if (!has) System.out.println("There is no enquiry.");
    }

    /**
     * Displays all Enquiry requests present in the system, regardless of the project or manager.
     * Uses the {@link UserType#MANAGER} display format.
     */
    public static void viewAllEnquiries() {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request instanceof Enquiry) {
                Display.displayRequest(request, UserType.MANAGER);
            }
        }
    }
}
