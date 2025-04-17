package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import boundary.Display;
import entity.list.ManagerList;
import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.OfficerRegistration;
import entity.request.Request;
import entity.user.Manager;
import entity.user.UserType;

public class ManagerProjectController {
    private static String managerID;
    
    public static void setManagerID(String ID) {
        managerID = ID;
    }
    
    public static void createProject(String projectID, String name, List<String> neighbourhood, Map<FlatType, Integer> availableUnit, Map<FlatType, Integer> price, LocalDate openDate, LocalDate closeDate, int availableOfficer) {
        for (Project p : ProjectList.getInstance().getAll()) {
            if (p.getName().equals(name)) {
                System.out.println("This project name is existed. Please try again.");
                return;
            }
        }
        Project newProject = new Project(projectID, name, neighbourhood, availableUnit, price, openDate, closeDate, managerID, availableOfficer, true);
        ProjectList.getInstance().add(newProject);
        System.out.println("Successfully created project (ProjectID: " + projectID + ").");
    }

    public static void editProject(String projectID, Project project) {
        ProjectList.getInstance().update(projectID, project);
    }

    public static void deleteProject(String projectID) {
        ProjectList.getInstance().delete(projectID);
    }

    public static void toggleVisibility(String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        project.setVisibility(!project.getVisibility());;
        ProjectList.getInstance().update(projectID, project);
    }

    public static void viewOfficerRegistrationStatus() {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request instanceof OfficerRegistration) {
                // TODO: print req
            }
        }
    }

    public static void viewProjectList(String managerID) {
        Manager manager = ManagerList.getInstance().getByID(managerID);
        List<String> projects = manager.getProject();
        for (String project : projects) {
            Display.displayProject(ProjectList.getInstance().getByID(project),UserType.MANAGER);
        }
    }

    public static void generateReport(Project filtered) {
        //to be done
    }
}
