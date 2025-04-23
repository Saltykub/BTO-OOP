package utils;

import java.util.Map;

import entity.list.ManagerList;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.BTOApplication;
import entity.request.BTOWithdrawal;
import entity.request.Enquiry;
import entity.request.OfficerRegistration;
import entity.request.Request;
import entity.request.RequestStatus;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.Manager;
import entity.user.Officer;
import entity.user.RegistrationStatus;
import entity.user.UserType;

/**
 * Utility class containing static methods for displaying formatted information
 * about various entity objects (Applicants, Officers, Managers, Projects, Requests)
 * to the standard console output. Provides different views based on context (e.g., user type).
 */
public class Display {

    
    /**
     * Displays formatted information about an Applicant.
     * If {@code profile} is true, it includes details about the applied project and application status history.
     * Always shows basic details like name, ID, age, marital status, and applied flat type if applicable.
     *
     * @param user    The {@link Applicant} object to display.
     * @param profile If true, display extended profile information including application history;
     * if false, display basic information.
     */
    public static void displayApplicant(Applicant user, boolean profile){
        System.out.println("------------------------- Applicant Info --------------------------");
        System.out.println("Name: " + user.getName());
        System.out.println("NRIC: " + user.getUserID());
        System.out.println("Age: " + user.getAge());
        System.out.println("Marital Status: " + user.getMaritalStatus());
        if(profile && user.getProject() != null) {
            System.out.println("Applied Project: " + user.getProject());
            System.out.println("Application Status:");
            for (Map.Entry<String, ApplicationStatus> entry : user.getApplicationStatus().entrySet()) {
                System.out.println("  " + entry.getKey() + " = " + entry.getValue().coloredString());
            }
        } 
        if(user.getProject()!= null) System.out.println("Flat Type: " + user.getAppliedFlat().get(user.getProject())); 
        System.out.println("-------------------------------------------------------------------");
    }

    /**
     * Displays formatted information about an Officer.
     * Includes basic details, list of registered project IDs, and registration status history.
     *
     * @param user The {@link Officer} object to display.
     */
    public static void displayOfficer(Officer user){
        System.out.println("-------------------------- Officer Info ---------------------------");
        System.out.println("Name: " + user.getName());
        System.out.println("NRIC: " + user.getUserID());
        System.out.println("Age: " + user.getAge());
        System.out.println("Marital Status: " + user.getMaritalStatus());
        if(user.getOfficerProject() != null && !user.getOfficerProject().isEmpty()) System.out.println("Registered Projects: " + String.join(", ", user.getOfficerProject()));
        System.out.println("Registration Status:");
        for (Map.Entry<String, RegistrationStatus> entry : user.getRegistrationStatus().entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue().coloredString());
        }
        System.out.println("-------------------------------------------------------------------");
    }

      /**
     * Displays formatted information about a Manager.
     * Includes basic details and a list of project IDs they created/manage.
     *
     * @param user The {@link Manager} object to display.
     */
    public static void displayManager(Manager user){
        System.out.println("-------------------------- Manager Info ---------------------------");
        System.out.println("Name: " + user.getName());
        System.out.println("NRIC: " + user.getUserID());
        System.out.println("Age: " + user.getAge());
        System.out.println("Marital Status: " + user.getMaritalStatus());
        System.out.println("Created Projects: " + String.join(", ", user.getProject()));
        System.out.println("-------------------------------------------------------------------");

    }

     /**
     * Displays formatted information about a Project.
     * The level of detail depends on the {@code user} type viewing the project.
     * The {@code flatType} parameter can filter the display of units and prices
     * (e.g., if viewing as an applicant only eligible for TWO_ROOM, THREE_ROOM details might be hidden).
     *
     * @param project  The {@link Project} object to display.
     * @param user     The {@link UserType} of the user viewing the project (determines detail level).
     * @param flatType A {@link FlatType} used for filtering display (e.g., hide details irrelevant to user eligibility).
     * If null, typically all flat type details are shown.
     */
    public static void displayProject(Project project, UserType user, FlatType flatType){
        System.out.println("------------------------- Project Info ---------------------------");
        System.out.println("Project ID: " + project.getProjectID());
        System.out.println("Name: " + project.getName());
    
        System.out.println("Neighborhood: " + String.join(", ", project.getNeighborhood()));
    
        System.out.println("Available Units:");
        for (Map.Entry<FlatType, Integer> entry : project.getAvailableUnit().entrySet()) {
            if(entry.getKey() == FlatType.THREE_ROOM && flatType == FlatType.TWO_ROOM ) continue;
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }
    
        System.out.println("Price:");
        for (Map.Entry<FlatType, Integer> entry : project.getPrice().entrySet()) {
            if(entry.getKey() == FlatType.THREE_ROOM && flatType == FlatType.TWO_ROOM ) continue;
            System.out.println("  " + entry.getKey() + " = $" + entry.getValue());
        }
    
        System.out.println("Open Date: " + project.getOpenDate());
        System.out.println("Close Date: " + project.getCloseDate());
        System.out.println("Manager Name: " + ManagerList.getInstance().getByID(project.getManagerID()).getName());
        if(user!= UserType.APPLICANT) {
            System.out.println("Available Officers: " + project.getAvailableOfficer());
            System.out.println("Officer IDs: " + String.join(", ", project.getOfficerID()));
            System.out.println("Applicant IDs: " + String.join(", ", project.getApplicantID()));
        } 
        if(user == UserType.MANAGER) System.out.println("Visible to public? " + (project.getVisibility() ? "Yes" : "No"));
        System.out.println("-------------------------------------------------------------------");
    }

    /**
     * Displays formatted information about a Request.
     * Adjusts displayed details based on the {@code userType} viewing the request
     * (e.g., hides User ID for Applicants).
     * Displays specific status or query/answer details based on the actual subclass of the Request
     * (OfficerRegistration, BTOApplication, BTOWithdrawal, Enquiry).
     *
     * @param request  The {@link Request} object (or a subclass instance) to display.
     * @param userType The {@link UserType} of the user viewing the request.
     */
    public static void displayRequest(Request request, UserType userType){
        System.out.println("------------------------- Request Info ---------------------------");
        System.out.println("Request ID: " + request.getRequestID());
        System.out.println("Type: " + request.getRequestType());
        System.out.println("Project ID: "+ request.getProjectID());
        if(userType != UserType.APPLICANT) System.out.println("User ID: " + request.getUserID());
        switch (request.getRequestType()){
            case REGISTRATION -> {
                if(request instanceof OfficerRegistration registration)System.out.println("Registration status: " + registration.getRegistrationStatus().coloredString());
            }
            case BTO_APPLICATION -> {
                if(request instanceof BTOApplication application)System.out.println("Applicantion status: " + application.getApplicationStatus().coloredString());
            }
            case BTO_WITHDRAWAL -> {
                if(request instanceof BTOWithdrawal application)System.out.println("Withdrawal status: " + application.getWithdrawalStatus().coloredString());
            }
            case ENQUIRY -> {
                if(request instanceof Enquiry enquiry) {
                    System.out.println("Status: " + enquiry.getRequestStatus().coloredString());
                    System.out.println("Query: " + enquiry.getQuery());
                    if(enquiry.getRequestStatus() != RequestStatus.PENDING) System.out.println("Answer: " + enquiry.getAnswer());
                }
            }
            default -> {
                System.out.println("default");
            }
        }
        //System.out.println("-------------------------------------------------------------------");
        System.out.println();
    }
}
