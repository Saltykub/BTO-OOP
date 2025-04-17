package controller;

import java.util.List;

import boundary.Display;
import entity.list.ApplicantList;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.project.FlatType;
import entity.project.Project;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.Officer;
import entity.user.UserType;

public class OfficerProjectController {
    private static String officerID;

    public static void setOfficerID(String ID) {
        officerID = ID;
    }

    public static void viewRegistrableProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        List<String> officerProject = OfficerList.getInstance().getByID(officerID).getOfficerProject();
        for (Project project : list) {
            if (!project.getApplicantID().contains(officerID) && project.getVisibility()) {
                boolean can = true;
                for (String id : officerProject) {
                    Project p = ProjectList.getInstance().getByID(id);
                    if (p.getCloseDate().isBefore(project.getOpenDate()) || project.getCloseDate().isBefore(p.getOpenDate())) continue;
                    can = false;
                }
                if (can) Display.displayProject(project,UserType.OFFICER,null);
            }
        }
    }

    public static void viewApplicantApplicationStatus() {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> list = officer.getOfficerProject();
        if(!checkValidProject(list)) return;
        IOController.nextLine();
        for (String id : list) {
            Project project = ProjectList.getInstance().getByID(id);
            Display.displayProject(project, UserType.OFFICER,null);
            for (Applicant applicant : ApplicantList.getInstance().getAll()) {
                if (applicant.getProject() == id) {
                    Display.displayApplicant(officer);
                    System.out.println("Status: " + applicant.getApplicationStatusByID(id));
                }
            }
        }
        IOController.nextLine();
    }
    
    public static void viewApplicantApplicationStatus(String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        IOController.nextLine();
        Display.displayProject(project, UserType.OFFICER,null);
        for (Applicant applicant : ApplicantList.getInstance().getAll()) {
            if (applicant.getProject() == projectID) {
                Display.displayApplicant(applicant);
                System.out.println("Status: " + applicant.getApplicationStatusByID(projectID));
            }
        } 
        IOController.nextLine();
    }

    public static void viewApplicantApplicationStatus(ApplicationStatus status) {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> list = officer.getOfficerProject();
        if(!checkValidProject(list)) return;
        IOController.nextLine();
        for (String id : list) {
            Project project = ProjectList.getInstance().getByID(id);
            Display.displayProject(project, UserType.OFFICER,null);
            for (Applicant applicant : ApplicantList.getInstance().getAll()) {
                if (applicant.getProject() == id && applicant.getApplicationStatusByID(id) == status) {
                    Display.displayApplicant(applicant);
                    System.out.println("Status: " + applicant.getApplicationStatusByID(id));
                }
            }
        }
        IOController.nextLine();
    }

    public static void viewApplicantApplicationStatus(String projectID, ApplicationStatus status) {
        Project project = ProjectList.getInstance().getByID(projectID);
        IOController.nextLine();
        Display.displayProject(project, UserType.OFFICER,null);
        for (Applicant applicant : ApplicantList.getInstance().getAll()) {
            if (applicant.getProject() == projectID && applicant.getApplicationStatusByID(projectID) == status) {
                Display.displayApplicant(applicant);
                System.out.println("Status: " + applicant.getApplicationStatusByID(projectID));
            }
        } 
        IOController.nextLine();
    }

    public static void bookFlat(String applicantID) {
        // no need to pass project because applicant can has only 1 project 
        Applicant applicant = ApplicantList.getInstance().getByID(applicantID);
        String projectID = applicant.getProject();
        Project project = ProjectList.getInstance().getByID(projectID);
        FlatType flat = applicant.getAppliedFlatByID(projectID);
        int availableUnit = project.getAvailableUnit().get(flat);
        if (availableUnit > 0) {
            project.setAvailableUnit(flat, availableUnit - 1);
            project.addApplicantID(applicantID);
            applicant.setApplicationStatusByID(projectID, ApplicationStatus.BOOKED);
        }
        ProjectList.getInstance().update(projectID, project);
        ApplicantList.getInstance().update(applicantID, applicant);
    }

    public static void generateReceipt() {
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectId = o.getOfficerProject();
        if(!checkValidProject(projectId)) return;
        for(String id: projectId){
            Project p = ProjectList.getInstance().getByID(id);
            List<String> applicantID = p.getApplicantID();
            for(String ida:applicantID){
                Applicant a = ApplicantList.getInstance().getByID(ida);
                Display.displayApplicant(a);
            }
            Display.displayProject(p,UserType.OFFICER,null);
        }
    }
    
    public static void generateReceiptByApplicant(String applicantID) {
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectId = o.getOfficerProject();
        if(!checkValidProject(projectId)) return;
        for(String id: projectId){
            Project p = ProjectList.getInstance().getByID(id);
            List<String> aID = p.getApplicantID();
            for(String ida:aID){
                if(ida == applicantID){
                    Applicant a = ApplicantList.getInstance().getByID(ida);
                    Display.displayProject(p,UserType.OFFICER,null);
                    Display.displayApplicant(a);
                    return;
                }
            }
        }
        System.out.println("Applicant not found in your registered project");
    }
    public static void generateReceiptByProject(String projectID) {
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectId = o.getOfficerProject();
        if(!checkValidProject(projectId)) return;
        for(String id: projectId){
            if(id == projectID){
                Project p = ProjectList.getInstance().getByID(id);
                List<String> aID = p.getApplicantID();
                Display.displayProject(p,UserType.OFFICER,null);
                for(String ida:aID){
                    Applicant a = ApplicantList.getInstance().getByID(ida);
                    Display.displayApplicant(a);
                }
                return;
            }
           
        }
        System.out.println("Project not found in your registered project");
    }

    public static boolean checkValidProject(List<String> projectId){
        if(projectId.isEmpty()) {
            System.out.println("You don't have registered project");
            return false;
        }
        return true;
    }
}