package boundary;

import controller.AccountController;
import controller.ApplicantController;
import controller.IOController;
import controller.OfficerRequestController;
import controller.OfficerProjectController;
import controller.UIController;
import entity.list.OfficerList;
import exception.ProjectNotFoundException;

public class OfficerPage {

    // Page management methods
    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.lineSeparator);
        System.out.println("Officer Page");
        System.out.println(UIController.lineSeparator);
        System.out.println("Welcome, " + OfficerList.getInstance().getByID(AccountController.getUserID()).getName() + ". Please enter your choice."
                + "\n\t1. View Applicants Projects List"
                + "\n\t2. View Applied Projects"
                + "\n\t3. Apply for Project"
                + "\n\t4. Withdraw Application"
                + "\n\t5. Make Query"
                + "\n\t6. View Query"
                + "\n\t7. Edit Query"
                + "\n\t8. Delete Query"
                + "\n\t9. View Registrable Project List"
                + "\n\t10. View Registered Project"
                + "\n\t11. Register for Project as Officer"
                + "\n\t12. View All Enquiries"
                + "\n\t13. View Enquiry by ProjectID"
                + "\n\t14. Answer Enquiries"
                + "\n\t15. View Applicant Application Status"
                + "\n\t16. Book Flat for Applicant"
                + "\n\t17. Generate Receipt"
                + "\n\t18. Generate Receipt by Applicant"
                + "\n\t19. Generate Receipt by Project"
                + "\n\t20. Sign out"
                + "\n\t21. Exit");
        System.out.print("Your choice (1-21): ");
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
            case 20 -> AccountController.logout();
            case 21 -> UIController.exit();
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
        UIController.loopOfficer();
    }

    public static void viewAppliedProject() {
        ApplicantController.viewAppliedProject();
        UIController.loopOfficer();
    }

    public static void applyProject() {
        System.out.print("Enter the project ID to apply: ");
        String projectID = IOController.nextLine();
        try {
            ApplicantController.applyProject(projectID);
            System.out.println("Applied successfully!");
            UIController.loopApplicant();
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopOfficer();
    }

    public static void withdrawApplication() {
        ApplicantController.withdrawApplication();
        UIController.loopOfficer();
    }

    public static void query() {
        System.out.print("Enter your query: ");
        String question = IOController.nextLine();
        ApplicantController.query(question);
        UIController.loopOfficer();
    }

    public static void viewQuery() {
        ApplicantController.viewQuery();
        UIController.loopOfficer();
    }

    public static void editQuery() {
        System.out.print("Enter the request ID to edit: ");
        String requestID = IOController.nextLine();
        System.out.print("Enter the new query: ");
        String newQuery = IOController.nextLine();
        ApplicantController.editQuery(requestID, newQuery);
        UIController.loopOfficer();
    }

    public static void deleteQuery() {
        System.out.print("Enter the request ID to delete: ");
        String requestID = IOController.nextLine();
        ApplicantController.deleteQuery(requestID);
        UIController.loopOfficer();
    }

    // OfficerRequestController Methods

    public static void registerProject() {
        System.out.print("Enter the project ID to register: ");
        String projectID = IOController.nextLine();
        OfficerRequestController.registerProject(projectID);
        UIController.loopOfficer();
    }

    public static void viewRegisteredProject() {
        OfficerRequestController.viewRegisteredProject();
        UIController.loopOfficer();
    }

    public static void viewEnquiries() {
        OfficerRequestController.viewEnquiries();
        UIController.loopOfficer();
    }

    public static void viewEnquiriesByProject() {
        System.out.print("Enter the project ID to view: ");
        String projectID = IOController.nextLine();
        OfficerRequestController.viewEnquiries(projectID);
        UIController.loopOfficer();
    }

    public static void answerEnquiry() {
        System.out.print("Enter the request ID to answer: ");
        String requestID = IOController.nextLine();
        System.out.print("Enter your answer:");
        String answer = IOController.nextLine();
        OfficerRequestController.answerEnquiry(requestID,answer);
        UIController.loopOfficer();
    }

    // OfficerProjectController Methods

    public static void viewRegistrableProject() {
        OfficerProjectController.viewRegistrableProject();
        UIController.loopOfficer();
    }

    public static void viewApplicantApplicationStatus() {
        OfficerProjectController.viewApplicantApplicationStatus();
        UIController.loopOfficer();
    }

    public static void bookFlat() {
        System.out.print("Enter the applicant ID to book a flat: ");
        String applicantID = IOController.nextLine();
        OfficerProjectController.bookFlat(applicantID);
        UIController.loopOfficer();
    }

    public static void generateReceipt() {
        OfficerProjectController.generateReceipt();
        UIController.loopOfficer();
    }

    public static void generateReceiptByApplicant() {
        System.out.print("Enter the applicant ID to generate receipt: ");
        String applicantID = IOController.nextLine();
        OfficerProjectController.generateReceiptByApplicant(applicantID);
        UIController.loopOfficer();
    }

    public static void generateReceiptByProject() {
        System.out.print("Enter the project ID to generate receipt: ");
        String projectID = IOController.nextLine();
        OfficerProjectController.generateReceiptByProject(projectID);
        UIController.loopOfficer();
    }
}
