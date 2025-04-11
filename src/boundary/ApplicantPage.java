package boundary;

import controller.AccountController;
import controller.ApplicantController;
import controller.IOController;
import controller.UIController;
import entity.list.ApplicantList;

public class ApplicantPage {

    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.lineSeparator);
        System.out.println("Applicant Page");
        System.out.println(UIController.lineSeparator);
        System.out.println("Welcome, " + ApplicantList.getInstance().getByID(AccountController.getUserID()).getName() + ". Please enter your choice."
                + "\n\t1. View Applicable Project"
                + "\n\t2. View Applied Projects"
                + "\n\t3. Apply for Project"
                + "\n\t4. Withdraw Application"
                + "\n\t5. Make Query"
                + "\n\t6. View Query"
                + "\n\t7. Edit Query"
                + "\n\t8. Delete Query");
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
            default -> {
                System.out.println("Invalid choice. Press ENTER to try again.");
                IOController.nextLine();
                allOptions();
            }
        }
    }

    public static void viewApplicableProject() {
        ApplicantController.viewApplicableProject();
    }

    public static void viewAppliedProject() {
        ApplicantController.viewAppliedProject();
    }

    public static void applyProject() {
        System.out.println("Enter the project ID to apply: ");
        String projectID = IOController.nextLine();
        ApplicantController.applyProject(projectID);
    }

    public static void withdrawApplication() {
        ApplicantController.withdrawApplication();
    }

    public static void query() {
        System.out.println("Enter your query: ");
        String question = IOController.nextLine();
        ApplicantController.query(question);
    }

    public static void viewQuery() {
        ApplicantController.viewQuery();
    }

    public static void editQuery() {
        System.out.println("Enter the request ID to edit: ");
        String requestID = IOController.nextLine();
        System.out.println("Enter the new query: ");
        String newQuery = IOController.nextLine();
        ApplicantController.editQuery(requestID, newQuery);
    }

    public static void deleteQuery() {
        System.out.println("Enter the request ID to delete: ");
        String requestID = IOController.nextLine();
        ApplicantController.deleteQuery(requestID);
    }
}
