package boundary;

import java.util.Map;

import entity.list.ManagerList;
import entity.project.FlatType;
import entity.project.Project;
import entity.user.Applicant;
import entity.user.UserType;

public class Display {
    
    public static void displayApplicant(Applicant user){
        System.out.println("------------------------ Applicant Info -------------------------");
        System.out.println("Name: " + user.getName());
        System.out.println("NRIC: " + user.getUserID());
        System.out.println("Age: " + user.getAge());
        System.out.println("Marital Status: " + user.getMaritalStatus());
        System.out.println("Flat Type: " + user.getAppliedFlat()); 
        System.out.println("-------------------------------------------------------------------");
    }

    public static void displayProject(Project project, UserType user){
        System.out.println("------------------------ Project Info --------------------------");
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
}
