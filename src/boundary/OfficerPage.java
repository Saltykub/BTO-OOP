package boundary;

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

public class OfficerPage {

    // Page management methods
    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.lineSeparator);
        System.out.println(
                        "  __  ____  ____  __  ___  ____  ____    ____   __    ___  ____ \n" + //
                        " /  \\(  __)(  __)(  )/ __)(  __)(  _ \\  (  _ \\ / _\\  / __)(  __)\n" + //
                        "(  O )) _)  ) _)  )(( (__  ) _)  )   /   ) __//    \\( (_ \\ ) _) \n" + //
                        " \\__/(__)  (__)  (__)\\___)(____)(__\\_)  (__)  \\_/\\_/ \\___/(____)\n" + //
                        "");
        System.out.println(UIController.lineSeparator);
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

    public static void viewQuery() {
        ApplicantController.viewQuery();
        UIController.loopOfficer();
    }

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

    public static void viewRegistrationStatus() {
        OfficerRequestController.viewRegistrationStatus();
        UIController.loopOfficer();
    }

    public static void viewEnquiries() {
        OfficerRequestController.viewEnquiries();
        UIController.loopOfficer();
    }

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
            UIController.loopOfficer();;
            return;
        }
        System.out.print("Enter your answer: ");
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
