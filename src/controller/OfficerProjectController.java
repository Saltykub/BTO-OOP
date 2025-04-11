package controller;

import java.util.List;

import entity.list.ApplicantList;
import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.Request;
import entity.user.Applicant;
import entity.user.ApplicationStatus;

public class OfficerProjectController {
    private static String officerID;

    public static void setOfficerID(String ID) {
        officerID = ID;
    }

    public static void viewRegistrableProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        for (Project project : list) {
            // TODO: add date checker logic
            if (!project.getApplicantID().contains(officerID) && project.getVisibility()) {
                System.out.println(project);
            }
        }
    }

    public static void viewApplicantApplicationStatus() {

    }
    
    public static void viewApplicantApplicationStatus(String projectID) {

    }

    public static void viewApplicantApplicationStatus(ApplicationStatus status) {

    }

    public static void viewApplicantApplicationStatus(String projectID, ApplicationStatus status) {

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