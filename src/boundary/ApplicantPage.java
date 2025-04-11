package boundary;

import java.util.Scanner;

import controller.ApplicantController;

public class ApplicantPage {

    Scanner scanner = new Scanner(System.in);

    // Page management methods
    public void allOptions() {
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
        int option = scanner.nextInt();
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

    public void viewApplicableProject() {
        ApplicantController.viewApplicableProject();
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
        System.out.println("Enter your query: ");
        String question = scanner.nextLine();
        ApplicantController.query(question);
    }

    public void viewQuery() {
        ApplicantController.viewQuery();
    }

    public void editQuery() {
        System.out.println("Enter the request ID to edit: ");
        String requestID = scanner.nextLine();
        System.out.println("Enter the new query: ");
        String newQuery = scanner.nextLine();
        ApplicantController.editQuery(requestID, newQuery);
    }

    public void deleteQuery() {
        System.out.println("Enter the request ID to delete: ");
        String requestID = scanner.nextLine();
        ApplicantController.deleteQuery(requestID);
    }
}
