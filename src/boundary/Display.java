package boundary;

import java.util.Map;

import entity.project.FlatType;
import entity.project.Project;
import entity.user.Applicant;

public class Display {
    
    public static void displayApplicant(Applicant user){
        System.out.println("===== Applicant Info =====");
        System.out.println("Name: " + user.getName());
        System.out.println("NRIC: " + user.getUserID());
        System.out.println("Age: " + user.getAge());
        System.out.println("Marital Status: " + user.getMaritalStatus());
        System.out.println("Flat Type: " + user.getAppliedFlat()); // ? 
        System.out.println("=========================");
    }

    public static void displayProject(Project project){
        System.out.println("===== Project Info =====");
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
        System.out.println("Manager ID: " + project.getManagerID());
        System.out.println("Available Officers: " + project.getAvailableOfficer());
    
        System.out.println("Officer IDs: " + String.join(", ", project.getOfficerID()));
        System.out.println("Applicant IDs: " + String.join(", ", project.getApplicantID()));
        System.out.println("Visible to public? " + (project.getVisibility() ? "Yes" : "No"));
        System.out.println("=========================");
    }
}
