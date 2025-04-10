package controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import entity.project.FlatType;
import entity.project.Project;

public class ManagerProjectController {
    private static String managerID;
    
    public static void setManagerID(String ID) {
        managerID = ID;
    }
    
    public static void createProject(String projectID, String name, List<String> neighbourhood,Map<FlatType, Integer> availableUnit, 
                              Map<FlatType, Integer> price, Date openDate, Date closeDate, int availableOfficer) {
      Project newProject = new Project(projectID, name, neighbourhood, availableUnit, price, openDate, closeDate, managerID, availableOfficer, true);
    }
    
    public static void editProject(String projectID, Project project) {
      // Implementation to be done
    }
    public static void deleteProject(String projectID) {
      //projectID = null;
    }
    public static void toggleVisibility(String projectID) {
      //projectID.setVisibility(not projectID.getVisibility());
    }
    public static void viewOfficerRegistrationStatus() {
      //officerID
    }
    public static void viewProjectList(String managerID) {
      //managerID.getProject();
    }
    public static void generateReport(Project filtered) {
      //to be done
    }
}
