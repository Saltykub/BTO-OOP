package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import entity.list.ApplicantList;
import entity.list.ManagerList;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.OfficerRegistration;
import entity.request.Request;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.Manager;
import entity.user.Officer;
import entity.user.UserType;
import exception.ProjectNotFoundException;
import utils.Display;

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
            if (p.getVisibility() && !(p.getCloseDate().isBefore(openDate) || p.getOpenDate().isAfter(closeDate))) {
                System.out.println("You cannot create a new project, as you have the active project now (ProjectID: " + p.getProjectID() + ").");
                return;
            }
        }
        Project newProject = new Project(projectID, name, neighbourhood, availableUnit, price, openDate, closeDate, managerID, availableOfficer, true);
        ProjectList.getInstance().add(newProject);
        Manager manager = ManagerList.getInstance().getByID(managerID);
        List<String> p = manager.getProject();
        p.add(projectID);
        manager.setProject(p);
        ManagerList.getInstance().update(managerID,manager);
        System.out.println("Successfully created project (ProjectID: " + projectID + ").");
    }

    public static void editProject(String projectID, Project project) {
        ProjectList.getInstance().update(projectID, project);
        System.out.println("Successfully edited project (ProjectID: " + projectID + ").");
    }

    public static void deleteProject(String projectID) throws ProjectNotFoundException {
        Project project = ProjectList.getInstance().getByID(projectID);
        if(project == null) throw new ProjectNotFoundException();
        // delete project
        ProjectList.getInstance().delete(projectID); 
        // delete manager
        List<Manager> mm = ManagerList.getInstance().getAll();
        for(Manager m: mm){
            if(m.getProject().contains(projectID)){
                List<String> p = m.getProject();
                p.remove(projectID);
                m.setProject(p);
                ManagerList.getInstance().update(m.getUserID(),m);    
            }
        }
        // delete request 
        List<Request> rr = RequestList.getInstance().getAll();
        for (Request r: rr){
            if(r.getProjectID().equals(projectID)){
                RequestList.getInstance().delete(r.getRequestID());
            }
        }
        // update applicant 
        List<Applicant> aa = ApplicantList.getInstance().getAll();
        for(Applicant a: aa){
            if(a.getProject().equals(projectID)){
                a.setProject(null);
                a.setApplicationStatusByID(projectID, ApplicationStatus.UNSUCCESSFUL);
                ApplicantList.getInstance().update(a.getUserID(), a);
            }
        }
        // update officer
        List<Officer> oo = OfficerList.getInstance().getAll();
        for(Officer o: oo){
            if(o.getOfficerProject().contains(projectID)){
                List<String> p = o.getOfficerProject();
                p.remove(projectID);
                o.setOfficerProject(p);
                OfficerList.getInstance().update(o.getUserID(),o);    
            }
        }
        System.out.println("Successfully deleted project (ProjectID: " + projectID + ").");
    }

    public static void toggleVisibility(String projectID) throws ProjectNotFoundException {
        Project project = ProjectList.getInstance().getByID(projectID);
        if(project == null) throw new ProjectNotFoundException();
        project.setVisibility(!project.getVisibility());;
        ProjectList.getInstance().update(projectID, project);
        System.out.println("Successfully toggled visibility of project (ProjectID: " + projectID + ").");
    }

    public static void viewOfficerRegistrationStatus() {
        List<Request> list = RequestList.getInstance().getAll();
        for (Request request : list) {
            if (request instanceof OfficerRegistration) {
                Display.displayRequest(request, UserType.MANAGER);
            }
        }
    }

    public static void viewProjectList(String managerID) throws ProjectNotFoundException {
        Manager manager = ManagerList.getInstance().getByID(managerID);
        List<String> projects = manager.getProject();
        if(projects.isEmpty()) throw new ProjectNotFoundException();
        for (String project : projects) {
            Display.displayProject(ProjectList.getInstance().getByID(project),UserType.MANAGER,null);
        }
    }

    public static void generateReport(Project filtered) {
        //to be done
    }
}
