package controller;

import java.util.List;

import entity.list.ProjectList;
import entity.project.Project;
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

    public static void bookFlat(String applicantID) {

    }

    public static void generateReceipt() {

    }

    public static void generateReceiptByApplicant(String applicantID) {

    }

    public static void generateReceiptByProject(String projectID) {

    }
}