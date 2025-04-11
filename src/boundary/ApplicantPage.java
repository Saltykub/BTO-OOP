package boundary;

import controller.ApplicantController;
import controller.IOController;
import controller.UIController;

public class ApplicantPage {

    // Page management methods
    public static void allOptions() {
        System.out.println("Options:"
                + "\n1. View Applicable Project"
                + "\n2. View Applied Projects"
                + "\n3. Apply for Project"
                + "\n4. Withdraw Application"
                + "\n5. Make Query"
                + "\n6. View Query"
                + "\n7. Edit Query"
                + "\n8. Delete Query");
        System.out.print("Option selection: ");
        int option = IOController.nextInt();
        switch (option) {
            case 1:
                viewApplicableProject();
                break;
            case 2:
                viewAppliedProject();
                break;
            case 3:
                applyProject();
                break;
            case 4:
                withdrawApplication();
                break;
            case 5:
                query();
                break;
            case 6:
                viewQuery();
            case 7:
                editQuery();
                break;
            case 8:
                deleteQuery();
                break;
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
