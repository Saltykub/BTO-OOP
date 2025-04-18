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

public class Display {
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

    public static void displayOfficer(Officer user){
        System.out.println("-------------------------- Officer Info ---------------------------");
        System.out.println("Name: " + user.getName());
        System.out.println("NRIC: " + user.getUserID());
        System.out.println("Age: " + user.getAge());
        System.out.println("Marital Status: " + user.getMaritalStatus());
        System.out.println("Registered Projects: " + String.join(", ", user.getOfficerProject()));
        System.out.println("Registration Status:");
        for (Map.Entry<String, RegistrationStatus> entry : user.getRegistrationStatus().entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue().coloredString());
        }
        System.out.println("-------------------------------------------------------------------");
    }

    public static void displayManager(Manager user){
        System.out.println("-------------------------- Manager Info ---------------------------");
        System.out.println("Name: " + user.getName());
        System.out.println("NRIC: " + user.getUserID());
        System.out.println("Age: " + user.getAge());
        System.out.println("Marital Status: " + user.getMaritalStatus());
        System.out.println("Created Projects: " + String.join(", ", user.getProject()));
        System.out.println("-------------------------------------------------------------------");

    }

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
                    System.out.println("Status: " + enquiry.getRequestStatus());
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
