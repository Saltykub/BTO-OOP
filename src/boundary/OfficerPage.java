package boundary;

import controller.ApplicantController;
import controller.OfficerRequestController;
import controller.OfficerProjectController;
import java.util.Scanner;

public class OfficerPage {

    private String officerID;

    Scanner scanner = new Scanner(System.in);

    public OfficerPage() {
        // Default constructor
    }

    public OfficerPage(String officerID) {
        this.officerID = officerID;
    }

    public void setOfficerID(String officerID) {
        this.officerID = officerID;
    }

    // ApplicantController Methods

    public void viewApplicantProjectList() {
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

    // OfficerRequestController Methods

    public void registerProject() {
        System.out.println("Enter the project ID to register: ");
        String projectID = scanner.nextLine();
        OfficerRequestController.registerProject(projectID);
    }

    public void viewRegisteredProject() {
        OfficerRequestController.viewRegisteredProject();
    }

    public void viewEnquiries() {
        OfficerRequestController.viewEnquiries();
    }

    public void viewEnquiry() {
        System.out.println("Enter the request ID to view: ");
        String requestID = scanner.nextLine();
        OfficerRequestController.viewEnquiries(requestID);
    }

    public void answerEnquiries() {
        System.out.println("Enter the request ID to answer: ");
        String requestID = scanner.nextLine();
        OfficerRequestController.answerEnquiries(requestID);
    }

    // OfficerProjectController Methods

    public void viewOfficerProjectList() {
        OfficerProjectController.viewProjectList();
    }

    public void viewApplicantApplicationStatus() {
        OfficerProjectController.viewApplicantApplicationStatus();
    }

    public void bookFlat() {
        System.out.println("Enter the applicant ID to book a flat: ");
        String applicantID = scanner.nextLine();
        OfficerProjectController.bookFlat(applicantID);
    }

    public void generateReceipt() {
        OfficerProjectController.generateReceipt();
    }

    public void generateReceiptByApplicant() {
        System.out.println("Enter the applicant ID to generate receipt: ");
        String applicantID = scanner.nextLine();
        OfficerProjectController.generateReceipt(applicantID);
    }

    public void generateReceiptByProject() {
        System.out.println("Enter the project ID to generate receipt: ");
        String projectID = scanner.nextLine();
        OfficerProjectController.generateReceipt(projectID);
    }
}
