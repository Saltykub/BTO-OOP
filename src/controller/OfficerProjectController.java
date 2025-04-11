package controller;

import java.util.List;

import entity.list.ApplicantList;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.project.FlatType;
import entity.project.Project;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.Officer;

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
                if (can) project.print();
            }
        }
    }

    public static void viewApplicantApplicationStatus() {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> list = officer.getOfficerProject();
        for (String id : list) {
            Project project = ProjectList.getInstance().getByID(id);
            project.print();
            for (Applicant applicant : ApplicantList.getInstance().getAll()) {
                if (applicant.getProject() == id) {
                    applicant.print();
                    System.out.println("Status: " + applicant.getApplicationStatusByID(id));
                }
            }
        }
    }
    
    public static void viewApplicantApplicationStatus(String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        project.print();
        for (Applicant applicant : ApplicantList.getInstance().getAll()) {
            if (applicant.getProject() == projectID) {
                applicant.print();
                System.out.println("Status: " + applicant.getApplicationStatusByID(projectID));
            }
        } 
    }

    public static void viewApplicantApplicationStatus(ApplicationStatus status) {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> list = officer.getOfficerProject();
        for (String id : list) {
            Project project = ProjectList.getInstance().getByID(id);
            project.print();
            for (Applicant applicant : ApplicantList.getInstance().getAll()) {
                if (applicant.getProject() == id && applicant.getApplicationStatusByID(id) == status) {
                    applicant.print();
                    System.out.println("Status: " + applicant.getApplicationStatusByID(id));
                }
            }
        }
    }

    public static void viewApplicantApplicationStatus(String projectID, ApplicationStatus status) {
        Project project = ProjectList.getInstance().getByID(projectID);
        project.print();
        for (Applicant applicant : ApplicantList.getInstance().getAll()) {
            if (applicant.getProject() == projectID && applicant.getApplicationStatusByID(projectID) == status) {
                applicant.print();
                System.out.println("Status: " + applicant.getApplicationStatusByID(projectID));
            }
        } 
    }

    public static void bookFlat(String applicantID, String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        Applicant applicant = ApplicantList.getInstance().getByID(applicantID);
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

    }

    public static void generateReceiptByApplicant(String applicantID) {

    }

    public static void generateReceiptByProject(String projectID) {

    }
}