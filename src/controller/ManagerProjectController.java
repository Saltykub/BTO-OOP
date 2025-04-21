package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import entity.user.MaritalStatus;
import entity.user.Officer;
import entity.user.RegistrationStatus;
import entity.user.UserType;
import exception.ProjectNotFoundException;
import utils.Display;
import utils.IOController;
import utils.UIController;

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
            if (p.getManagerID().equals(managerID) && !(p.getCloseDate().isBefore(openDate) || p.getOpenDate().isAfter(closeDate))) {
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
            if(a.getProject() != null && a.getProject().equals(projectID)){
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
                o.setRegistrationStatusByID(projectID, RegistrationStatus.REJECTED);
                OfficerList.getInstance().update(o.getUserID(),o);    
            }
        }
        System.out.println("Successfully deleted project (ProjectID: " + projectID + ").");
    }

    public static void toggleVisibility(String projectID) throws ProjectNotFoundException {
        Project project = ProjectList.getInstance().getByID(projectID);
        if(project == null) throw new ProjectNotFoundException();
        project.setVisibility(!project.getVisibility());
        ProjectList.getInstance().update(projectID, project);
        System.out.println("Successfully toggled visibility of project (ProjectID: " + projectID + ").");
    }

    public static void viewOfficerRegistrationStatus() {
        List<Request> list = RequestList.getInstance().getAll();
        boolean has = false;
        for (Request request : list) {
            if (request instanceof OfficerRegistration) {
                has = true;
                Display.displayRequest(request, UserType.MANAGER);
            }
        }
        if (!has) System.out.println("There is no registration application.");
    }

    public static void viewProjectList(String managerID) throws ProjectNotFoundException {
        Manager manager = ManagerList.getInstance().getByID(managerID);
        List<String> projects = manager.getProject();
        if(projects.isEmpty()) throw new ProjectNotFoundException();
        List<Project> list = FilterController.filteredListFromID(projects);
        if(list.isEmpty()) throw new ProjectNotFoundException();
        for (Project project : list) {
            Display.displayProject(project, UserType.MANAGER, null);
        }
    }

    public static void generateReport() {
        System.out.print("Enter Project ID: ");
        String projectID = IOController.nextLine();
        System.out.println("Enter age: ");
        System.out.print("\tfrom:");
        int from = IOController.nextInt();
        System.out.print("\tto:");
        int to = IOController.nextInt();
        System.out.println("Enter marital status:");
        System.out.println("\t1. Single");
        System.out.println("\t2. Married");
        System.out.print("Your choice (1-2): ");
        MaritalStatus maritalStatus = null;
        while (maritalStatus == null) {
            int marital = IOController.nextInt();
            switch (marital) {
                case 1 -> maritalStatus = MaritalStatus.SINGLE;
                case 2 -> maritalStatus = MaritalStatus.MARRIED;
                default -> System.out.println("Invalid choice. Please try again."); 
            }
        }
        final MaritalStatus finalStatus = maritalStatus;
        System.out.println("Enter Flat Type:");
        System.out.println("\t1. Two Room");
        System.out.println("\t2. Three Room");
        System.out.print("Your choice (1-2): ");
        FlatType flatType = null;
        while (flatType == null) {
            int flat = IOController.nextInt();
            switch (flat) {
                case 1 -> flatType = FlatType.TWO_ROOM;
                case 2 -> flatType = FlatType.THREE_ROOM;
                default -> System.out.println("Invalid choice. Please try again."); 
            }
        }

        final FlatType finalFlatType = flatType;
        List<Applicant> report = ApplicantList.getInstance().getAll().stream()
        .filter(a -> a.getProject() != null && a.getProject().equals(projectID))
        .filter(a -> a.getAge() >= from && a.getAge() <= to)
        .filter(a -> a.getMaritalStatus() == finalStatus)
        .filter(a -> a.getAppliedFlat().get(a.getProject()) == finalFlatType)
        .collect(Collectors.toList());
        System.out.println(UIController.lineSeparator);
        System.out.println("                           Report");
        System.out.println(UIController.lineSeparator);
        for(Applicant applicant:report){
            Display.displayApplicant(applicant, false);
        }
        System.out.println(UIController.lineSeparator);
    }
}
