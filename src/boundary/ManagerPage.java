package boundary;

import java.util.*;

import controller.AccountController;
import controller.FilterController;
import controller.IDController;
import controller.IOController;
import controller.ManagerProjectController;
import controller.ManagerRequestController;
import controller.OfficerProjectController;
import controller.OfficerRequestController;
import controller.UIController;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.ApprovedStatus;
import entity.request.RequestStatus;
import exception.ProjectNotFoundException;

import java.time.*;

public class ManagerPage {
    String requestID;

    // Page management methods
    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.lineSeparator);
        System.out.println("Manager Page");
        System.out.println(UIController.lineSeparator);
        System.out.println("Welcome, " + OfficerList.getInstance().getByID(AccountController.getUserID()).getName() + ". Please enter your choice."
                + "\n\t1. View Enquiries"
                + "\n\t2. Answer Enquiries"
                + "\n\t3. View Project List"
                + "\n\t4. View Applicant Application Status"
                + "\n\t5. View Requests"
                + "\n\t6. Change Request Status"
                + "\n\t7. Change Applicant Application"
                + "\n\t8. View All Enquiries"
                + "\n\t9. Create Project"
                + "\n\t10. Edit Project"
                + "\n\t11. Delete Project"
                + "\n\t12. Toggle Visibility"
                + "\n\t13. View Officer Registration Status"
                + "\n\t14. Generate Report"
                + "\n\t15. Set up Project Filter"
                + "\n\t16. Sign out"
                + "\n\t17. Exit");
        System.out.print("Your choice (1-17): ");
        int option = IOController.nextInt();
        switch (option) {
            case 1 -> viewEnquiries();
            case 2 -> answerEnquiries();
            case 3 -> viewProjectList();
            case 4 -> viewApplicantApplicationStatus();
            case 5 -> viewRequest();
            case 6 -> changeRequestStatus();
            case 7 -> changeApplicationStatus();
            case 8 -> viewAllEnquiries();
            case 9 -> createProject();
            case 10 -> editProject();
            case 11 -> deleteProject();
            case 12 -> toggleVisibility();
            case 13 -> viewOfficerRegistrationStatus();
            case 14 -> generateReport();
            case 15 -> FilterController.setup();
            case 16 -> AccountController.logout();
            case 17 -> UIController.exit();
            default -> {
                System.out.println("Invalid choice. Press ENTER to try again.");
                IOController.nextLine();
                allOptions();
            }
        }
    }

    public static void viewRegisteredProject() {
        OfficerRequestController.viewRegisteredProject();
        UIController.loopManager();
    }

    public static void viewEnquiries() {
        OfficerRequestController.viewEnquiries();
        UIController.loopManager();
    }

    public static void answerEnquiries() {
        System.out.print("Enter the request ID to answer: ");
        String requestID = IOController.nextLine();
        System.out.print("Enter your answer:");
        String answer = IOController.nextLine();
        OfficerRequestController.answerEnquiry(requestID,answer);
        UIController.loopManager();
    }

    public static void viewProjectList() {
        System.out.println("Enter the manager ID to view thier created projects (Press ENTER to view yours): ");
        String managerID = IOController.nextLine();
        if (managerID.isEmpty()) managerID = AccountController.getUserID();
        try{
            ManagerProjectController.viewProjectList(managerID);
        }  catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopManager();
    }

    public static void viewApplicantApplicationStatus() {
        OfficerProjectController.viewApplicantApplicationStatus();
        UIController.loopManager();
    }

    public static void viewRequest() {
        ManagerRequestController.viewRequest();
        UIController.loopManager();
    }

    public static void changeRequestStatus() {
        System.out.print("Enter the request ID: ");
        String requestID = IOController.nextLine();
        System.out.println("Enter new status:");
        System.out.println("\t1. " + RequestStatus.PENDING);
        System.out.println("\t2. " + RequestStatus.DONE);
        int option = IOController.nextInt();
        while (option != 1 && option != 2) {
            System.out.print("Please enter valid choice: ");
            option = IOController.nextInt();
        }
        RequestStatus status = null;
        switch (option) {
            case 1 -> status = RequestStatus.PENDING;
            case 2 -> status = RequestStatus.DONE;
        }
        ManagerRequestController.changeRequestStatus(requestID, status);
        UIController.loopManager();
    }

    public static void changeApplicationStatus() {
        System.out.print("Enter the request ID: ");
        String requestID = IOController.nextLine();
        System.out.println("Enter new status:");
        System.out.println("\t1. " + ApprovedStatus.PENDING);
        System.out.println("\t2. " + ApprovedStatus.SUCCESSFUL);
        System.out.println("\t3. " + ApprovedStatus.UNSUCCESSFUL);
        int option = IOController.nextInt();
        while (option < 1 || option > 3) {
            System.out.print("Please enter valid choice: ");
            option = IOController.nextInt();
        }
        ApprovedStatus status = null;
        switch (option) {
            case 1 -> status = ApprovedStatus.PENDING;
            case 2 -> status = ApprovedStatus.SUCCESSFUL;
            case 3 -> status = ApprovedStatus.UNSUCCESSFUL;
        }
        ManagerRequestController.changeApplicationStatus(requestID, status);
        UIController.loopManager();
    }

    public static void viewAllEnquiries() {
        ManagerRequestController.viewAllEnquiries();
        UIController.loopManager();
    }

    public static void createProject() {
        String tmp;
        int tmpint;
        String projectID = IDController.newProjectID();

        System.out.print("Name: ");
        String name = IOController.nextLine();

        System.out.print("Number of neighbourhood: ");
        List<String> neighbourhood = new ArrayList<>();
        tmpint = IOController.nextInt();
        while (tmpint < 0) {
            System.out.print("Please enter valid number: ");
            tmpint = IOController.nextInt();
        }
        System.out.println("List of neigbourhood:");
        while (tmpint > 0) {
            System.out.print("\t: ");
            tmp = IOController.nextLine();        
            neighbourhood.add(tmp);
            tmpint--;
        }

        Map<FlatType, Integer> availableUnits = new HashMap<>();
        Map<FlatType, Integer> price = new HashMap<>();
        System.out.println("Two Room Flat: ");
        System.out.print("\tNumber of units: ");
        tmpint = IOController.nextInt();
        availableUnits.put(FlatType.TWO_ROOM, tmpint);
        System.out.print("\tPrice: ");
        tmpint = IOController.nextInt();
        price.put(FlatType.TWO_ROOM, tmpint);

        System.out.println("Three Room Flat: ");
        System.out.print("\tNumber of units: ");
        tmpint = IOController.nextInt();
        availableUnits.put(FlatType.THREE_ROOM, tmpint);
        System.out.print("\tPrice: ");
        tmpint = IOController.nextInt();
        price.put(FlatType.THREE_ROOM, tmpint);

        System.out.println("Open date:");
        LocalDate openDate = IOController.nextDate();
        System.out.println("Close date:");
        LocalDate closeDate = IOController.nextDate();

        System.out.print("Available Officer (1-10): ");
        int availableOfficer = IOController.nextInt();
        while (availableOfficer < 1 || availableOfficer > 10) {
            System.out.print("Please enter valid number: ");
            availableOfficer = IOController.nextInt();
        }

        ManagerProjectController.createProject(projectID, name, neighbourhood, availableUnits, price, openDate, closeDate, availableOfficer);
        UIController.loopManager();
    }

    public static void editProject() {
        String tmp;
        int tmpint;
        System.out.print("Project ID: ");
        String projectID = IOController.nextLine();
        Project project = ProjectList.getInstance().getByID(projectID);

        System.out.println("Press ENTER to skip.");
        System.out.print("Name: ");
        String name = IOController.nextLine();
        if (name.isEmpty()) {
            name = project.getName();
        }

        System.out.print("Number of neighbourhood: ");
        List<String> neighbourhood = new ArrayList<>();
        tmp = IOController.nextLine();
        if (tmp.isEmpty()) {
            neighbourhood = project.getNeighborhood();
        }
        else {
            tmpint = Integer.parseInt(tmp);
            while (tmpint < 0) {
                System.out.print("Please enter valid number: ");
                tmpint = IOController.nextInt();
            }
            System.out.println("List of neigbourhood:");
            while (tmpint > 0) {
                System.out.print("\t: ");
                tmp = IOController.nextLine();        
                neighbourhood.add(tmp);
            }
        }

        Map<FlatType, Integer> availableUnits = new HashMap<>();
        Map<FlatType, Integer> price = new HashMap<>();
        System.out.println("Two Room Flat: ");
        System.out.print("\tNumber of units: ");
        tmp = IOController.nextLine();
        if (tmp.isEmpty()) {
            availableUnits.put(FlatType.TWO_ROOM, project.getAvailableUnit().get(FlatType.TWO_ROOM));
        }
        else {
            tmpint = Integer.parseInt(tmp);
            availableUnits.put(FlatType.TWO_ROOM, tmpint);
        }
        System.out.print("\tPrice: ");
        tmp = IOController.nextLine();
        if (tmp.isEmpty()) {
            price.put(FlatType.TWO_ROOM, project.getPrice().get(FlatType.TWO_ROOM));
        }
        else {
            tmpint = Integer.parseInt(tmp);
            price.put(FlatType.TWO_ROOM, tmpint);
        }

        System.out.println("Three Room Flat: ");
        System.out.print("\tNumber of units: ");
        tmp = IOController.nextLine();
        if (tmp.isEmpty()) {
            availableUnits.put(FlatType.THREE_ROOM, project.getAvailableUnit().get(FlatType.THREE_ROOM));
        }
        else {
            tmpint = Integer.parseInt(tmp);
            availableUnits.put(FlatType.THREE_ROOM, tmpint);
        }
        System.out.print("\tPrice: ");
        tmp = IOController.nextLine();
        if (tmp.isEmpty()) {
            price.put(FlatType.THREE_ROOM, project.getPrice().get(FlatType.THREE_ROOM));
        }
        else {
            tmpint = Integer.parseInt(tmp);
            price.put(FlatType.THREE_ROOM, tmpint);
        }

        System.out.println("Open date:");
        LocalDate openDate = project.getOpenDate();
        tmp = IOController.nextLine();
        if (!tmp.isEmpty()) {
            openDate = IOController.nextDate();
        }

        System.out.println("Close date:");
        LocalDate closeDate = project.getCloseDate();
        tmp = IOController.nextLine();
        if (!tmp.isEmpty()) {
            closeDate = IOController.nextDate();
        }

        System.out.print("Available Officer (1-10): ");
        int availableOfficer = project.getAvailableOfficer();
        tmp = IOController.nextLine();
        if (!tmp.isEmpty()) {
            availableOfficer = IOController.nextInt();
            while (availableOfficer < 1 || availableOfficer > 10) {
                System.out.print("Please enter valid number: ");
                availableOfficer = IOController.nextInt();
            }
        }
        
        System.out.println("Visibility: ");
        System.out.println("\t1. Visible");
        System.out.println("\t2. Not visible");
        boolean visibility = project.getVisibility();
        tmp = IOController.nextLine();
        if (!tmp.isEmpty()) {
            tmpint = IOController.nextInt();
            while (tmpint < 1 || tmpint > 2) {
                System.out.print("Your choice (1-2): ");
                tmpint = IOController.nextInt();
            }
            visibility = tmpint == 1 ? true : false;
        }

        ManagerProjectController.editProject(projectID, new Project(projectID, name, neighbourhood, availableUnits, price, openDate, closeDate, ProjectList.getInstance().getByID(projectID).getManagerID(), availableOfficer, visibility));
        UIController.loopManager();
    }

    public static void deleteProject() {
        System.out.print("Project ID: ");
        String projectID = IOController.nextLine();
        ManagerProjectController.deleteProject(projectID);
        UIController.loopManager();
    }

    public static void toggleVisibility() {
        System.out.print("Project ID: ");
        String projectID = IOController.nextLine();
        ManagerProjectController.toggleVisibility(projectID);
        UIController.loopManager();
    }

    public static void viewOfficerRegistrationStatus() {
        ManagerProjectController.viewOfficerRegistrationStatus();
        UIController.loopManager();
    }

    public static void generateReport() {
        // TODO: ManagerProjectController.generateReport(requestID);
        UIController.loopManager();
    }
}
