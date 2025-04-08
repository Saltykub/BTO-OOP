package boundary;

import java.util.Scanner;

import controller.ApplicantController;

public class ApplicantPage {

    private String applicantID;

    Scanner scanner = new Scanner(System.in);

    public ApplicantPage() {
        // Default constructor
    }

    public ApplicantPage(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void viewProjectList() {
        ApplicantController.viewProjectList();
    }

    public void viewAppliedProject() {
        ApplicantController.viewAppliedProject();
    }

    public void applyProject() {
        System.out.println("Enter the project ID to apply: ");
        String projectID = scanner.nextLine();
        ApplicantController.applyProject(projectID);
    }

    public void withdrawApplication() {
        ApplicantController.withdrawApplication();
    }

    public void query() {
        ApplicantController.query();
    }

    public void viewQuery() {
        ApplicantController.viewQuery();
    }

    public void editQuery() {
        System.out.println("Enter the request ID to edit: ");
        String requestID = scanner.nextLine();
        ApplicantController.editQuery(requestID);
    }

    public void deleteQuery() {
        System.out.println("Enter the request ID to delete: ");
        String requestID = scanner.nextLine();
        ApplicantController.deleteQuery(requestID);
    }
}
