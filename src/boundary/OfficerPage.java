package boundary;

import controller.AccountController;
import controller.ApplicantController;
import controller.IOController;
import controller.OfficerRequestController;
import controller.OfficerProjectController;
import controller.UIController;
import entity.list.OfficerList;

public class OfficerPage {

    // Page management methods
    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.lineSeparator);
        System.out.println("Officer Page");
        System.out.println(UIController.lineSeparator);
        System.out.println("Welcome, " + OfficerList.getInstance().getByID(AccountController.getUserID()).getName() + ". Please enter your choice.");
        System.out.println("Options:"
                + "\n1. View Applicants Projects List"
                + "\n2. View Applied Projects"
                + "\n3. Apply for Project"
                + "\n4. Withdraw Application"
                + "\n5. Make Query"
                + "\n6. View Query"
                + "\n7. Edit Query"
                + "\n8. Delete Query"
                + "\n9. View Registrable Project List"
                + "\n10. View Registered Project"
                + "\n11. Register for Project as Officer"
                + "\n12. View All Enquiries"
                + "\n13. View Enquiry by ProjectID"
                + "\n14. Answer Enquiries"
                + "\n15. View Applicant Application Status"
                + "\n16. Book Flat for Applicant"
                + "\n17. Generate Receipt"
                + "\n18. Generate Receipt by Applicant"
                + "\n19. Generate Receipt by Project");
        System.out.print("Your choice (1-8): ");
        int option = IOController.nextInt();
        switch (option) {
            case 1 -> viewApplicableProject();
            case 2 -> viewAppliedProject();
            case 3 -> applyProject();
            case 4 -> withdrawApplication();
            case 5 -> query();
            case 6 -> viewQuery();
            case 7 -> editQuery();
            case 8 -> deleteQuery();
            case 9 -> viewRegistrableProject();
            case 10 -> viewRegisteredProject();
            case 11 -> registerProject();
            case 12 -> viewEnquiries();
            case 13 -> viewEnquiriesByProject();
            case 14 -> answerEnquiry();
            case 15 -> viewApplicantApplicationStatus();
            case 16 -> bookFlat();
            case 17 -> generateReceipt();
            case 18 -> generateReceiptByApplicant();
            case 19 -> generateReceiptByProject();
            default -> {
                System.out.println("Invalid choice. Press ENTER to try again.");
                IOController.nextLine();
                allOptions();
            }
        }
    }

    // ApplicantController Methods

    public static void viewApplicableProject() {
        ApplicantController.viewApplicableProject();
    }

    public static void viewAppliedProject() {
        ApplicantController.viewAppliedProject();
    }

    public static void applyProject() {
        System.out.print("Enter the project ID to apply: ");
        String projectID = IOController.nextLine();
        ApplicantController.applyProject(projectID);
    }

    public static void withdrawApplication() {
        ApplicantController.withdrawApplication();
    }

    public static void query() {
        System.out.print("Enter your query: ");
        String question = IOController.nextLine();
        ApplicantController.query(question);
    }

    public static void viewQuery() {
        ApplicantController.viewQuery();
    }

    public static void editQuery() {
        System.out.print("Enter the request ID to edit: ");
        String requestID = IOController.nextLine();
        System.out.print("Enter the new query: ");
        String newQuery = IOController.nextLine();
        ApplicantController.editQuery(requestID, newQuery);
    }

    public static void deleteQuery() {
        System.out.print("Enter the request ID to delete: ");
        String requestID = IOController.nextLine();
        ApplicantController.deleteQuery(requestID);
    }

    // OfficerRequestController Methods

    public static void registerProject() {
        System.out.print("Enter the project ID to register: ");
        String projectID = IOController.nextLine();
        OfficerRequestController.registerProject(projectID);
    }

    public static void viewRegisteredProject() {
        OfficerRequestController.viewRegisteredProject();
    }

    public static void viewEnquiries(){
        OfficerRequestController.viewEnquiries();
    }

    public static void viewEnquiriesByProject() {
        System.out.print("Enter the project ID to view: ");
        String projectID = IOController.nextLine();
        OfficerRequestController.viewEnquiries(projectID);
    }

    public static void answerEnquiry() {
        System.out.print("Enter the request ID to answer: ");
        String requestID = IOController.nextLine();
        System.out.print("Enter your answer:");
        String answer = IOController.nextLine();
        OfficerRequestController.answerEnquiry(requestID,answer);
    }

    // OfficerProjectController Methods

    public static void viewRegistrableProject() {
        OfficerProjectController.viewRegistrableProject();
    }

    public static void viewApplicantApplicationStatus() {
        OfficerProjectController.viewApplicantApplicationStatus();
    }

    public static void bookFlat() {
        System.out.print("Enter the applicant ID to book a flat: ");
        String applicantID = IOController.nextLine();
        OfficerProjectController.bookFlat(applicantID);
    }

    public static void generateReceipt() {
        OfficerProjectController.generateReceipt();
    }

    public static void generateReceiptByApplicant() {
        System.out.print("Enter the applicant ID to generate receipt: ");
        String applicantID = IOController.nextLine();
        OfficerProjectController.generateReceiptByApplicant(applicantID);
    }

    public static void generateReceiptByProject() {
        System.out.print("Enter the project ID to generate receipt: ");
        String projectID = IOController.nextLine();
        OfficerProjectController.generateReceiptByProject(projectID);
    }
}
