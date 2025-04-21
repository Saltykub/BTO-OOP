package controller;

import java.util.List;

import entity.list.ApplicantList;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.project.FlatType;
import entity.project.Project;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.Manager;
import entity.user.Officer;
import entity.user.User;
import entity.user.UserType;
import utils.Display;

public class OfficerProjectController {
    private static String officerID;

    public static void setOfficerID(String ID) {
        officerID = ID;
    }

    public static void viewRegistrableProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        list = FilterController.filteredList(list);
        List<String> officerProject = OfficerList.getInstance().getByID(officerID).getOfficerProject();
        boolean has = false;
        for (Project project : list) {
            if (!project.getApplicantID().contains(officerID) && project.getVisibility()) {
                boolean can = true;
                for (String id : officerProject) {
                    Project p = ProjectList.getInstance().getByID(id);
                    if (p.getCloseDate().isBefore(project.getOpenDate()) || project.getCloseDate().isBefore(p.getOpenDate())) continue;
                    can = false;
                }
                if (can) {
                    has = true;
                    Display.displayProject(project,UserType.OFFICER,null);
                }
            }
        }
        if (!has) System.out.println("There is no registrable project.");
    }

    public static void viewApplicantApplicationStatus() {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> list = officer.getOfficerProject();
        if (!checkValidProject(list)) return;
        if (list.isEmpty()) {
            System.out.println("You haven't registered to any project.");
            return;
        }
        for (String id : list) {
            Project project = ProjectList.getInstance().getByID(id);
            Display.displayProject(project, UserType.OFFICER,null);
            for (Applicant applicant : ApplicantList.getInstance().getAll()) {
                if (applicant.getProject() != null && applicant.getProject().equals(id)) {
                    System.out.println("Status: " + applicant.getApplicationStatusByID(id).coloredString());
                    Display.displayApplicant(applicant, false);
                }
            }
        }
    }
    
    public static void viewApplicantApplicationStatus(String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        Display.displayProject(project, UserType.OFFICER,null);
        for (Applicant applicant : ApplicantList.getInstance().getAll()) {
            if (applicant.getProject() != null && applicant.getProject().equals(projectID)) {
                System.out.println("Status: " + applicant.getApplicationStatusByID(projectID).coloredString());
                Display.displayApplicant(applicant, false);
            }
        } 
    }

    public static void viewApplicantApplicationStatus(ApplicationStatus status) {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> list = officer.getOfficerProject();
        if(!checkValidProject(list)) return;
        for (String id : list) {
            Project project = ProjectList.getInstance().getByID(id);
            Display.displayProject(project, UserType.OFFICER,null);
            for (Applicant applicant : ApplicantList.getInstance().getAll()) {
                if (applicant.getProject() != null && applicant.getProject().equals(id) && applicant.getApplicationStatusByID(id) == status) {
                    System.out.println("Status: " + applicant.getApplicationStatusByID(id).coloredString());
                    Display.displayApplicant(applicant, false);
                }
            }
        }
    }

    public static void viewApplicantApplicationStatus(String projectID, ApplicationStatus status) {
        Project project = ProjectList.getInstance().getByID(projectID);
        Display.displayProject(project, UserType.OFFICER,null);
        for (Applicant applicant : ApplicantList.getInstance().getAll()) {
            if (applicant.getProject() == projectID && applicant.getApplicationStatusByID(projectID) == status) {
                Display.displayApplicant(applicant, false);
                System.out.println("Status: " + applicant.getApplicationStatusByID(projectID).coloredString());
            }
        } 
    }

    public static void bookFlat(String applicantID) {
        // no need to pass project because applicant can has only 1 project 
        User user = ApplicantList.getInstance().getByID(applicantID);
        if ((user instanceof Manager) || user == null) {
            System.out.println("Invalid applicant ID.");
            return;
        }
        Applicant applicant = (Applicant)user;
        String projectID = applicant.getProject();
        Officer officer = OfficerList.getInstance().getByID(officerID);
        if (projectID == null || !officer.getOfficerProject().contains(projectID)) {
            System.out.println("You are not allowed to book flat for applicant in other's project.");
            return;
        }
        if (applicant.getApplicationStatusByID(projectID) == ApplicationStatus.BOOKED) {
            System.out.println("Flat has been booked for this applicant already.");
            return; 
        }
        else if (applicant.getApplicationStatusByID(projectID) != ApplicationStatus.SUCCESSFUL) {
            System.out.println("This applicant hasn't been approved yet.");
            return; 
        }
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
        System.out.println("Successfully booked a flat for this applicant.");
    }

    public static void generateReceipt() {
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectId = o.getOfficerProject();
        if(!checkValidProject(projectId)) return;
        for(String id: projectId){
            Project p = ProjectList.getInstance().getByID(id);
            List<String> applicantID = p.getApplicantID();
            for(String ida:applicantID){
                Applicant applicant = ApplicantList.getInstance().getByID(ida);
                if(applicant.getApplicationStatus().get(id) != ApplicationStatus.BOOKED) continue;
                Display.displayApplicant(applicant, false);
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
                if(ida.equals(applicantID)){
                    Applicant applicant = ApplicantList.getInstance().getByID(ida);
                    if(applicant.getApplicationStatus().get(id) != ApplicationStatus.BOOKED) continue;
                    Display.displayProject(p,UserType.OFFICER,null);
                    Display.displayApplicant(applicant, false);
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
            if(id.equals(projectID)){
                Project p = ProjectList.getInstance().getByID(id);
                List<String> aID = p.getApplicantID();
                Display.displayProject(p,UserType.OFFICER,null);
                for(String ida:aID){
                    Applicant applicant = ApplicantList.getInstance().getByID(ida);
                    if(applicant.getApplicationStatus().get(id) != ApplicationStatus.BOOKED) continue;
                    Display.displayApplicant(applicant, false);
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