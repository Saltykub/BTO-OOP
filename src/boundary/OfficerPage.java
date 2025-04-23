package boundary;

import java.util.List;

import controller.AccountController;
import controller.ApplicantController;
import controller.FilterController;
import controller.OfficerRequestController;
import controller.OfficerProjectController;
import entity.list.ApplicantList;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.Enquiry;
import entity.request.Request;
import entity.request.RequestStatus;
import entity.user.Applicant;
import entity.user.MaritalStatus;
import exception.ProjectNotFoundException;
import utils.Display;
import utils.IOController;
import utils.UIController;

/**
 * Represents the boundary layer for handling interactions for users logged in as Officers.
 * This page provides a combined menu allowing officers to perform actions both as an applicant
 * (applying for projects, managing personal queries) and as an officer
 * (registering for projects, managing applicant requests/enquiries, booking flats, generating receipts).
 */
public class OfficerPage {

    /**
     * Displays the main menu options available to the logged-in officer.
     * This menu includes options for both applicant-related actions and officer-specific duties.
     * Reads the officer's choice and navigates to the corresponding functionality.
     * Handles invalid input and loops back to the main menu or exits the application.
     */
    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println(
                        "  __  ____  ____  __  ___  ____  ____    ____   __    ___  ____ \n" + //
                        " /  \\(  __)(  __)(  )/ __)(  __)(  _ \\  (  _ \\ / _\\  / __)(  __)\n" + //
                        "(  O )) _)  ) _)  )(( (__  ) _)  )   /   ) __//    \\( (_ \\ ) _) \n" + //
                        " \\__/(__)  (__)  (__)\\___)(____)(__\\_)  (__)  \\_/\\_/ \\___/(____)\n" + //
                        "");
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println("Welcome, " + OfficerList.getInstance().getByID(AccountController.getUserID()).getName() + ". Please enter your choice."
                + "\n\t0. View Profile"
                + "\n\t1. View Applicable Project"
                + "\n\t2. View Applied Projects"
                + "\n\t3. Apply for Project"
                + "\n\t4. Withdraw Application"
                + "\n\t5. Make Query"
                + "\n\t6. View Your Query"
                + "\n\t7. Edit Your Query"
                + "\n\t8. Delete Your Query"
                + "\n\t9. View Registrable Project List"
                + "\n\t10. View Your Projects"
                + "\n\t11. View Your Registration Application"
                + "\n\t12. Register for Project as Officer"
                + "\n\t13. View All Enquiries"
                + "\n\t14. View Enquiry by ProjectID"
                + "\n\t15. Answer Enquiries"
                + "\n\t16. View Applicant Application Status"
                + "\n\t17. Book Flat for Applicant"
                + "\n\t18. Generate Receipt"
                + "\n\t19. Generate Receipt by Applicant"
                + "\n\t20. Generate Receipt by Project"
                + "\n\t21. Set up Project Filter"
                + "\n\t22. View Your Current Filter"
                + "\n\t23. Sign out"
                + "\n\t24. Exit");
        System.out.print("Your choice (0-24): ");
        int option = IOController.nextInt();
        switch (option) {
            case 0 -> {
                Display.displayOfficer(OfficerList.getInstance().getByID(AccountController.getUserID()));
                Display.displayApplicant(ApplicantList.getInstance().getByID(AccountController.getUserID()), true);
                UIController.loopOfficer();
            }
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
            case 11 -> viewRegistrationStatus();
            case 12 -> registerProject();
            case 13 -> viewEnquiries();
            case 14 -> viewEnquiriesByProject();
            case 15 -> answerEnquiry();
            case 16 -> viewApplicantApplicationStatus();
            case 17 -> bookFlat();
            case 18 -> generateReceipt();
            case 19 -> generateReceiptByApplicant();
            case 20 -> generateReceiptByProject();
            case 21 -> {
                FilterController.setup();
                UIController.loopOfficer();
            }
            case 22 -> {
                FilterController.displayFilter();
                UIController.loopOfficer();
            }
            case 23 -> AccountController.logout();
            case 24 -> UIController.exit();
            default -> {
                System.out.println("Invalid choice. Press ENTER to try again.");
                IOController.nextLine();
                allOptions();
            }
        }
    }

    // ========================================
    // Applicant Actions (using ApplicantController)
    // These methods allow the officer to act as an applicant.
    // ========================================

    /**
     * Displays projects that the officer (acting as an applicant) is eligible to apply for.
     * Delegates logic to {@link ApplicantController#viewApplicableProject()}.
     * Loops back to the officer menu.
     */
    public static void viewApplicableProject() {
        ApplicantController.viewApplicableProject();
        UIController.loopOfficer();
    }

    /**
     * Displays projects the officer (acting as an applicant) has applied for.
     * Delegates logic to {@link ApplicantController#viewAppliedProject()}.
     * Loops back to the officer menu.
     */
    public static void viewAppliedProject() {
        ApplicantController.viewAppliedProject();
        UIController.loopOfficer();
    }

    /**
     * Handles the process for the officer (acting as an applicant) to apply for a specific project.
     * Checks eligibility (if already applied, age, marital status). Prompts for project ID and flat type.
     * Delegates application logic to {@link ApplicantController#applyProject(String, FlatType)}.
     * Handles {@link ProjectNotFoundException}.
     * Loops back to the officer menu.
     */
    public static void applyProject() {
        Applicant applicant = ApplicantList.getInstance().getByID(AccountController.getUserID());
        if (applicant.getProject() != null) {
            System.out.println("You are allowed to apply for only one project.");
            UIController.loopOfficer();
            return;
        }
        System.out.print("Enter the project ID to apply: ");
        String projectID = IOController.nextLine();
        try {
            Project project = ProjectList.getInstance().getByID(projectID);
            if (project == null) throw new ProjectNotFoundException();
            int able = 0;
            if (applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
                able = 1;
            }
            else if (applicant.getAge() >= 21 && applicant.getMaritalStatus() == MaritalStatus.MARRIED) {
                able = 2;
            }
            if (able == 0) {
                System.out.println("You are not eligible to apply for a project.");
                UIController.loopOfficer();
                return;
            }
            System.out.println("Enter flat type: ");
            System.out.println("\t1. Two Room");
            if (able == 2) System.out.println("\t2. Three Room");
            System.out.print("Your choice: ");
            int option = IOController.nextInt();
            while (option > able || option < 1) {
                System.out.print("Please enter valid choice: ");
                option = IOController.nextInt();
            }
            FlatType applyFlat = option == 1 ? FlatType.TWO_ROOM : FlatType.THREE_ROOM;
            ApplicantController.applyProject(projectID, applyFlat);
            UIController.loopOfficer();
            return;
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopOfficer();
    }

    /**
     * Handles the process for the officer (acting as an applicant) to withdraw their project application.
     * Prompts for the project ID.
     * Delegates withdrawal logic to {@link ApplicantController#withdrawApplication(String)}.
     * Handles {@link ProjectNotFoundException}.
     * Loops back to the officer menu.
     */
    public static void withdrawApplication() {
        System.out.print("Enter the project ID to apply: ");
        String projectID = IOController.nextLine();
        try {
            ApplicantController.withdrawApplication(projectID);
            UIController.loopOfficer();
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopOfficer();
    }

    /**
     * Allows the officer (acting as an applicant) to submit a personal query about a specific project.
     * Prompts for project ID and query text. Checks if project exists.
     * Delegates query submission to {@link ApplicantController#query(String, String)}.
     * Loops back to the officer menu.
     */
    public static void query() {
        System.out.print("Enter the project ID to enquiry: ");
        String projectID = IOController.nextLine();
        if (ProjectList.getInstance().getByID(projectID) == null) {
            System.out.println(new ProjectNotFoundException().getMessage());
            UIController.loopOfficer();
            return;
        }
        System.out.print("Enter your query: ");
        String question = IOController.nextLine();
        ApplicantController.query(projectID, question);
        UIController.loopOfficer();
    }

    /**
     * Displays the personal queries submitted by the officer (acting as an applicant).
     * Delegates display logic to {@link ApplicantController#viewQuery()}.
     * Loops back to the officer menu.
     */
    public static void viewQuery() {
        ApplicantController.viewQuery();
        UIController.loopOfficer();
    }

    /**
     * Allows the officer (acting as an applicant) to edit one of their own submitted queries.
     * Prompts for the request ID of the query. Checks if the query belongs to the officer.
     * Prompts for the new query text.
     * Delegates editing logic to {@link ApplicantController#editQuery(String, String)}.
     * Loops back to the officer menu.
     */
    public static void editQuery() {
        System.out.print("Enter the request ID to edit: ");
        String requestID = IOController.nextLine();
        if (!ApplicantController.checkQuery(requestID)) {
            UIController.loopOfficer();
            return;
        }
        System.out.print("Enter the new query: ");
        String newQuery = IOController.nextLine();
        ApplicantController.editQuery(requestID, newQuery);
        UIController.loopOfficer();
    }

    /**
     * Allows the officer (acting as an applicant) to delete one of their own submitted queries.
     * Prompts for the request ID of the query.
     * Delegates deletion logic to {@link ApplicantController#deleteQuery(String)}.
     * Loops back to the officer menu.
     */
    public static void deleteQuery() {
        System.out.print("Enter the request ID to delete: ");
        String requestID = IOController.nextLine();
        ApplicantController.deleteQuery(requestID);
        UIController.loopOfficer();
    }

    // OfficerRequestController Methods

    // ========================================
    // Officer Duties - Project Registration & Status
    // ========================================

    /**
     * Allows the officer to register their interest in working on a specific project.
     * Prompts for the project ID.
     * Delegates registration logic to {@link OfficerRequestController#registerProject(String)}.
     * Loops back to the officer menu.
     */
    public static void registerProject() {
        System.out.print("Enter the project ID to register: ");
        String projectID = IOController.nextLine();
        OfficerRequestController.registerProject(projectID);
        UIController.loopOfficer();
    }

    /**
     * Displays the projects the officer is currently registered to work on.
     * Delegates display logic to {@link OfficerRequestController#viewRegisteredProject()}.
     * Loops back to the officer menu.
     */
    public static void viewRegisteredProject() {
        OfficerRequestController.viewRegisteredProject();
        UIController.loopOfficer();
    }

    /**
     * Displays the status of the officer's applications to register for projects.
     * Delegates display logic to {@link OfficerRequestController#viewRegistrationStatus()}.
     * Loops back to the officer menu.
     */
    public static void viewRegistrationStatus() {
        OfficerRequestController.viewRegistrationStatus();
        UIController.loopOfficer();
    }
    // ========================================
    // Officer Duties - Enquiry & Request Management
    // ========================================

    /**
     * Displays enquiries assigned to or related to the currently logged-in officer.
     * Delegates display logic to {@link OfficerRequestController#viewEnquiries()}.
     * Loops back to the officer menu.
     */
    public static void viewEnquiries() {
        OfficerRequestController.viewEnquiries();
        UIController.loopOfficer();
    }

    /**
     * Displays enquiries filtered by a specific project ID.
     * Prompts for the project ID. Checks if the project exists.
     * Delegates display logic to {@link OfficerRequestController#viewEnquiries(String)}.
     * Handles {@link ProjectNotFoundException}.
     * Loops back to the officer menu.
     */
    public static void viewEnquiriesByProject() {
        System.out.print("Enter the project ID to view: ");
        String projectID = IOController.nextLine();
        try {
            Project project = ProjectList.getInstance().getByID(projectID);
            if (project == null) throw new ProjectNotFoundException();
            OfficerRequestController.viewEnquiries(projectID);
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopOfficer();
    }

    /**
     * Handles the process for an officer to answer a specific enquiry.
     * Prompts for the request ID. Performs validation checks:
     * - Request existence.
     * - Request type (must be Enquiry).
     * - Enquiry status (must not be DONE).
     * - Officer's association with the project (must be registered for the project).
     * Prompts for the answer text if valid.
     * Delegates answering logic to {@link OfficerRequestController#answerEnquiry(String, String)}.
     * Loops back to the officer menu.
     */
    public static void answerEnquiry() {
        System.out.print("Enter the request ID to answer: ");
        String requestID = IOController.nextLine();
        Request query = RequestList.getInstance().getByID(requestID);
        if (query == null) {
            System.out.println("This request ID is not existed.");
            UIController.loopOfficer();
            return;
        }
        if (!(query instanceof Enquiry)) {
            System.out.println("This request ID is not enquiry.");
            UIController.loopOfficer();
            return;
        }
        if (query.getRequestStatus() == RequestStatus.DONE) {
            System.out.println("This enquiry has been answered.");
            UIController.loopOfficer();
            return;
        }
        List<String> projects = OfficerList.getInstance().getByID(AccountController.getUserID()).getOfficerProject();
        if (!projects.contains(requestID)) {
            System.out.println("You are not allowed to change application status of other's project.");
            UIController.loopOfficer();
            return;
        }
        System.out.print("Enter your answer: ");
        String answer = IOController.nextLine();
        OfficerRequestController.answerEnquiry(requestID,answer);
        UIController.loopOfficer();
    }

    // ========================================
    // Officer Duties - Applicant/Flat Management
    // ========================================
    /**
     * Displays the registrable projects of applicants, filtered for projects the officer manages.
     * Delegates display logic to {@link OfficerProjectController#viewRegistrableProject()}.
     * Loops back to the officer menu.
     */
    public static void viewRegistrableProject() {
        OfficerProjectController.viewRegistrableProject();
        UIController.loopOfficer();
    }
    /**
     * Displays the application status of applicants, filtered for projects the officer manages.
     * Delegates display logic to {@link OfficerProjectController#viewApplicantApplicationStatus()}.
     * Loops back to the officer menu.
     */
    public static void viewApplicantApplicationStatus() {
        OfficerProjectController.viewApplicantApplicationStatus();
        UIController.loopOfficer();
    }

    /**
     * Initiates the process for booking a flat for a specific applicant.
     * Prompts for the applicant's ID.
     * Delegates booking logic to {@link OfficerProjectController#bookFlat(String)}.
     * Loops back to the officer menu.
     */
    public static void bookFlat() {
        System.out.print("Enter the applicant ID to book a flat: ");
        String applicantID = IOController.nextLine();
        OfficerProjectController.bookFlat(applicantID);
        UIController.loopOfficer();
    }

    // ========================================
    // Officer Duties - Receipt Generation
    // ========================================

    /**
     * Generates a general receipt (details depend on controller implementation).
     * Delegates generation logic to {@link OfficerProjectController#generateReceipt()}.
     * Loops back to the officer menu.
     */
    public static void generateReceipt() {
        OfficerProjectController.generateReceipt();
        UIController.loopOfficer();
    }

    /**
     * Generates a receipt specifically for an applicant.
     * Prompts for the applicant's ID.
     * Delegates generation logic to {@link OfficerProjectController#generateReceiptByApplicant(String)}.
     * Loops back to the officer menu.
     */
    public static void generateReceiptByApplicant() {
        System.out.print("Enter the applicant ID to generate receipt: ");
        String applicantID = IOController.nextLine();
        OfficerProjectController.generateReceiptByApplicant(applicantID);
        UIController.loopOfficer();
    }

    /**
     * Generates receipts related to a specific project.
     * Prompts for the project ID.
     * Delegates generation logic to {@link OfficerProjectController#generateReceiptByProject(String)}.
     * Loops back to the officer menu.
     */
    public static void generateReceiptByProject() {
        System.out.print("Enter the project ID to generate receipt: ");
        String projectID = IOController.nextLine();
        OfficerProjectController.generateReceiptByProject(projectID);
        UIController.loopOfficer();
    }
}
