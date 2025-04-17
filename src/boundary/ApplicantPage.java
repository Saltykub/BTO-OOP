package boundary;

import controller.AccountController;
import controller.ApplicantController;
import entity.list.ApplicantList;
import entity.list.ProjectList;
import entity.project.FlatType;
import entity.project.Project;
import entity.user.Applicant;
import entity.user.MaritalStatus;
import exception.ProjectNotFoundException;
import utils.Display;
import utils.IOController;
import utils.UIController;

public class ApplicantPage {

    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.lineSeparator);
        System.out.println("Applicant Page");
        System.out.println(UIController.lineSeparator);
        System.out.println("Welcome, " + ApplicantList.getInstance().getByID(AccountController.getUserID()).getName() + ". Please enter your choice."
                + "\n\t0. View Profile"
                + "\n\t1. View Applicable Project"
                + "\n\t2. View Applied Projects"
                + "\n\t3. Apply for Project"
                + "\n\t4. Withdraw Application"
                + "\n\t5. Make Query"
                + "\n\t6. View Query"
                + "\n\t7. Edit Query"
                + "\n\t8. Delete Query"
                + "\n\t9. Sign out"
                + "\n\t10. Exit");
        System.out.print("Your choice (0-10): ");
        int option = IOController.nextInt();
        switch (option) {
            case 0 -> Display.displayApplicant(ApplicantList.getInstance().getByID(AccountController.getUserID()), true);
            case 1 -> viewApplicableProject();
            case 2 -> viewAppliedProject();
            case 3 -> applyProject();
            case 4 -> withdrawApplication();
            case 5 -> query();
            case 6 -> viewQuery();
            case 7 -> editQuery();
            case 8 -> deleteQuery();
            case 9 -> AccountController.logout();
            case 10 -> UIController.exit();
            default -> {
                System.out.println("Invalid choice. Press ENTER to try again.");
                IOController.nextLine();
                allOptions();
            }
        }
    }

    public static void viewApplicableProject() {
        ApplicantController.viewApplicableProject();
        UIController.loopApplicant();
    }

    public static void viewAppliedProject() {
        ApplicantController.viewAppliedProject();
        UIController.loopApplicant();
    }

    public static void applyProject() {
        Applicant applicant = ApplicantList.getInstance().getByID(AccountController.getUserID());
        if (applicant.getProject() != null) {
            System.out.println("You are allowed to apply for only one project.");
            UIController.loopApplicant();
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
                UIController.loopApplicant();
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
            UIController.loopApplicant();
            return;
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopApplicant();
    }

    public static void withdrawApplication() {
        System.out.print("Enter the project ID to apply: ");
        String projectID = IOController.nextLine();
        try {
            ApplicantController.withdrawApplication(projectID);
            UIController.loopApplicant();
        } catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopApplicant();
    }

    public static void query() {
        System.out.print("Enter the project ID to enquiry: ");
        String projectID = IOController.nextLine();
        if (ProjectList.getInstance().getByID(projectID) == null) {
            System.out.println(new ProjectNotFoundException().getMessage());
            UIController.loopApplicant();
            return;
        }
        System.out.print("Enter your query: ");
        String question = IOController.nextLine();
        ApplicantController.query(projectID, question);
        UIController.loopApplicant();
    }

    public static void viewQuery() {
        ApplicantController.viewQuery();
        UIController.loopApplicant();
    }

    public static void editQuery() {
        System.out.print("Enter the request ID to edit: ");
        String requestID = IOController.nextLine();
        if (!ApplicantController.checkQuery(requestID)) {
            UIController.loopApplicant();
            return;
        }
        System.out.print("Enter the new query: ");
        String newQuery = IOController.nextLine();
        ApplicantController.editQuery(requestID, newQuery);
        UIController.loopApplicant();
    }

    public static void deleteQuery() {
        System.out.print("Enter the request ID to delete: ");
        String requestID = IOController.nextLine();
        ApplicantController.deleteQuery(requestID);
        UIController.loopApplicant();
    }
}
