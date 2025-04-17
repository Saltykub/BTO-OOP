package boundary;

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
import entity.user.UserType;

public class Display {
    
    public static void displayApplicant(Applicant user){
        System.out.println("------------------------- Applicant Info --------------------------");
        System.out.println("Name: " + user.getName());
        System.out.println("NRIC: " + user.getUserID());
        System.out.println("Age: " + user.getAge());
        System.out.println("Marital Status: " + user.getMaritalStatus());
        System.out.println("Flat Type: " + user.getAppliedFlat()); 
        System.out.println("-------------------------------------------------------------------");
    }

    public static void displayProject(Project project, UserType user){
        System.out.println("------------------------- Project Info ---------------------------");
        System.out.println("Project ID: " + project.getProjectID());
        System.out.println("Name: " + project.getName());
    
        System.out.println("Neighborhood: " + String.join(", ", project.getNeighborhood()));
    
        System.out.println("Available Units:");
        for (Map.Entry<FlatType, Integer> entry : project.getAvailableUnit().entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }
    
        System.out.println("Price:");
        for (Map.Entry<FlatType, Integer> entry : project.getPrice().entrySet()) {
            System.out.println("  " + entry.getKey() + " = $" + entry.getValue());
        }
    
        System.out.println("Open Date: " + project.getOpenDate());
        System.out.println("Close Date: " + project.getCloseDate());
        System.out.println("Manager Name: " + ManagerList.getInstance().getByID(project.getManagerID()).getName());
        if(user!= UserType.APPLICANT) System.out.println("Available Officers: " + project.getAvailableOfficer());
        if(user!= UserType.APPLICANT) System.out.println("Officer IDs: " + String.join(", ", project.getOfficerID()));
        if(user!= UserType.APPLICANT) System.out.println("Applicant IDs: " + String.join(", ", project.getApplicantID()));
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
                if(request instanceof OfficerRegistration registration)System.out.println("Registration status: " + registration.getRegistrationStatus());
            }
            case BTO_APPLICATION -> {
                if(request instanceof BTOApplication application)System.out.println("Applicantion status: " + application.getApplicationStatus());
            }
            case BTO_WITHDRAWAL -> {
                if(request instanceof BTOWithdrawal application)System.out.println("Withdrawal status: " + application.getWithdrawalStatus());
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
        System.out.println("-------------------------------------------------------------------");
    }
}
