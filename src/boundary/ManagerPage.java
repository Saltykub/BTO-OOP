package boundary;

import java.util.*;

import controller.AccountController;
import controller.FilterController;
import controller.ManagerProjectController;
import controller.ManagerRequestController;
import controller.OfficerRequestController;
import entity.list.ManagerList;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.list.RequestList;
import entity.project.FlatType;
import entity.project.Project;
import entity.request.ApprovedStatus;
import entity.request.Enquiry;
import entity.request.Request;
import entity.request.RequestStatus;
import entity.user.UserType;
import exception.ProjectNotFoundException;
import utils.Display;
import utils.IDController;
import utils.IOController;
import utils.UIController;

import java.time.*;

/**
 * Represents the boundary layer for handling manager interactions and displaying the manager menu.
 * This class provides static methods for managers to manage projects (create, edit, delete, toggle visibility),
 * handle requests and enquiries (view, change status, answer), manage filters, view officer status,
 * and generate reports.
 */
public class ManagerPage {
    String requestID;

    /**
     * Displays the main menu options available to the logged-in manager.
     * Reads the manager's choice and navigates to the corresponding functionality.
     * Handles invalid input and loops back to the main menu or exits the application.
     */
    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println(
                        " _  _   __   __ _   __    ___  ____  ____    ____   __    ___  ____ \n" + //
                        "( \\/ ) / _\\ (  ( \\ / _\\  / __)(  __)(  _ \\  (  _ \\ / _\\  / __)(  __)\n" + //
                        "/ \\/ \\/    \\/    //    \\( (_ \\ ) _)  )   /   ) __//    \\( (_ \\ ) _) \n" + //
                        "\\_)(_/\\_/\\_/\\_)__)\\_/\\_/ \\___/(____)(__\\_)  (__)  \\_/\\_/ \\___/(____)\n" + //
                        "");
        System.out.println(UIController.LINE_SEPARATOR);
        System.out.println("Welcome, " + OfficerList.getInstance().getByID(AccountController.getUserID()).getName() + ". Please enter your choice."
                + "\n\t0. View Profile"
                + "\n\t1. View Project List"
                + "\n\t2. Create Project"
                + "\n\t3. Edit Project"
                + "\n\t4. Delete Project"
                + "\n\t5. Toggle Visibility"
                + "\n\t6. View Requests"
                + "\n\t7. View Officer Registration Status"
                + "\n\t8. Change Application Status"
                + "\n\t9. View Enquiries"
                + "\n\t10. View All Enquiries"
                + "\n\t11. Answer Enquiries"
                + "\n\t12. Generate Report"
                + "\n\t13. Set up Project Filter"
                + "\n\t14. View Your Current Filter"
                + "\n\t15. Sign out"
                + "\n\t16. Exit");
        System.out.print("Your choice (0-16): ");
        int option = IOController.nextInt();
        switch (option) {
            case 0 -> {
                Display.displayManager(ManagerList.getInstance().getByID(AccountController.getUserID()));
                UIController.loopManager();
            }
            case 1 -> viewProjectList();
            case 2 -> createProject();
            case 3 -> editProject();
            case 4 -> deleteProject();
            case 5 -> toggleVisibility();
            case 6 -> viewRequest();
            case 7 -> viewOfficerRegistrationStatus();
            case 8 -> changeApplicationStatus();
            case 9 -> viewEnquiries();
            case 10 -> viewAllEnquiries();
            case 11 -> answerEnquiries();
            case 12 -> generateReport();
            case 13 -> {
                FilterController.setup();
                UIController.loopManager();
            }
            case 14 -> {
                FilterController.displayFilter();
                UIController.loopManager();
            }
            case 15 -> AccountController.logout();
            case 16 -> UIController.exit();    
            default -> {
                System.out.println("Invalid choice. Press ENTER to try again.");
                IOController.nextLine();
                allOptions();
            }
        }
    }

    /**
     * Displays enquiries specifically assigned to or related to the current manager's projects.
     * Delegates the display logic to {@link ManagerRequestController#viewEnquiries()}.
     * Loops back to the manager menu afterwards.
     */
    public static void viewEnquiries() {
        ManagerRequestController.viewEnquiries();
        UIController.loopManager();
    }

    /**
     * Handles the process for a manager to answer a specific enquiry.
     * Prompts for the request ID of the enquiry.
     * Performs checks:
     * - If the request ID exists.
     * - If the request is actually an Enquiry.
     * - If the enquiry has already been answered (status is DONE).
     * - If the enquiry belongs to a project managed by the current manager.
     * Prompts for the answer text if all checks pass.
     * Delegates the action of answering the enquiry to {@link OfficerRequestController#answerEnquiry(String, String)}.
     * Note: Uses OfficerRequestController; consider if ManagerRequestController is intended.
     * Loops back to the manager menu afterwards.
     */
    public static void answerEnquiries() {
        System.out.print("Enter the request ID to answer: ");
        String requestID = IOController.nextLine();
        List<String> projects = ManagerList.getInstance().getByID(AccountController.getUserID()).getProject();
        Request query = RequestList.getInstance().getByID(requestID);
        if (query == null) {
            System.out.println("This request ID is not existed.");
            UIController.loopManager();
            return;
        }
        if (!(query instanceof Enquiry)) {
            System.out.println("This request ID is not enquiry.");
            UIController.loopManager();
            return;
        }
        if (query.getRequestStatus() == RequestStatus.DONE) {
            System.out.println("This enquiry has been answered.");
            UIController.loopManager();
            return;
        }
        if (!projects.contains(query.getProjectID())) {
            System.out.println("You are not allowed to change application status of other's project.");
            UIController.loopManager();
            return;
        }
        System.out.print("Enter your answer: ");
        String answer = IOController.nextLine();
        OfficerRequestController.answerEnquiry(requestID,answer);
        UIController.loopManager();
    }

    /**
     * Displays the list of projects created by a specific manager.
     * Prompts for the manager ID. If left empty, defaults to the currently logged-in manager.
     * Delegates the display logic to {@link ManagerProjectController#viewProjectList(String)}.
     * Catches and handles {@link ProjectNotFoundException} if no projects are found for the manager.
     * Loops back to the manager menu afterwards.
     */
    public static void viewProjectList() {
        System.out.print("Enter the manager ID to view thier created projects (Press ENTER to view yours): ");
        String managerID = IOController.nextLine();
        if (managerID.isEmpty()) managerID = AccountController.getUserID();
        try{
            ManagerProjectController.viewProjectList(managerID);
        }  catch (ProjectNotFoundException e) {
            System.out.println("No projects found.");
        }
        UIController.loopManager();
    }

    /**
     * Displays requests (applications, etc.) associated with the projects managed by the current manager.
     * Delegates the display logic to {@link ManagerRequestController#viewRequest()}.
     * Loops back to the manager menu afterwards.
     */
    public static void viewRequest() {
        ManagerRequestController.viewRequest();
        UIController.loopManager();
    }

    /**
     * Allows the manager to change the approval status of an application request.
     * Prompts for the request ID.
     * Performs checks:
     * - If the request ID exists.
     * - If the request is not an Enquiry.
     * - If the request belongs to a project managed by the current manager.
     * Displays the request details and prompts for the new status (Pending, Successful, Unsuccessful).
     * Delegates the status change to {@link ManagerRequestController#changeApplicationStatus(String, ApprovedStatus)}.
     * Loops back to the manager menu afterwards.
     */
    public static void changeApplicationStatus() {
        System.out.print("Enter the request ID: ");
        String requestID = IOController.nextLine();
        Request request = RequestList.getInstance().getByID(requestID);
        if (request == null) {
            System.out.println("No request with this ID.");
            UIController.loopManager();
            return;
        }
        if (request instanceof Enquiry) {
            System.out.println("Invalid request type. This request ID is enquiry.");
            UIController.loopManager();
            return;
        }
        List<String> projects = ManagerList.getInstance().getByID(AccountController.getUserID()).getProject();
        if (!projects.contains(request.getProjectID())) {
            System.out.println("You are not allowed to change application status of other's project.");
            UIController.loopManager();
            return;
        }
        Display.displayRequest(request, UserType.MANAGER);
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

    /**
     * Displays all enquiries across all projects (presumably those the manager has access to view).
     * Delegates the display logic to {@link ManagerRequestController#viewAllEnquiries()}.
     * Loops back to the manager menu afterwards.
     */
    public static void viewAllEnquiries() {
        ManagerRequestController.viewAllEnquiries();
        UIController.loopManager();
    }

    /**
     * Handles the creation of a new project by the manager.
     * Generates a new project ID using {@link IDController#newProjectID()}.
     * Prompts the manager for all required project details:
     * - Name
     * - Neighbourhoods (list)
     * - Available units and price for Two Room and Three Room flats
     * - Open and Close dates for applications
     * - Number of available officer slots (1-10)
     * Delegates the project creation logic to {@link ManagerProjectController#createProject(String, String, List, Map, Map, LocalDate, LocalDate, int)}.
     * Loops back to the manager menu afterwards.
     */
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
        if (tmpint > 0) System.out.println("List of neigbourhood:");
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

    /**
     * Handles the editing of an existing project by the manager.
     * Prompts for the project ID to edit.
     * If the project exists, prompts for new values for each field, allowing the user to press ENTER to skip
     * and keep the existing value.
     * Collects potentially updated details for:
     * - Name, Neighbourhoods, Units/Prices for flat types, Open/Close dates, Officer slots, Visibility.
     * Constructs a new {@link Project} object with potentially mixed old and new data.
     * Delegates the update logic to {@link ManagerProjectController#editProject(String, Project)}.
     * Loops back to the manager menu afterwards.
     */
    public static void editProject() {
        String tmp;
        int tmpint;
        System.out.print("Project ID: ");
        String projectID = IOController.nextLine();
        Project project = ProjectList.getInstance().getByID(projectID);
        if(project == null) {
            System.out.println("No project with this ID.");
            UIController.loopManager();
            return;
        }
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
            if (tmpint > 0) System.out.println("List of neigbourhood:");
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

    /**
     * Handles the deletion of a project by the manager.
     * Prompts for the project ID to delete.
     * Delegates the deletion logic to {@link ManagerProjectController#deleteProject(String)}.
     * Catches and displays a message for {@link ProjectNotFoundException} if the project ID is invalid.
     * Loops back to the manager menu afterwards.
     */
    public static void deleteProject() {
        System.out.print("Project ID: ");
        String projectID = IOController.nextLine();
        try {
            ManagerProjectController.deleteProject(projectID);
        } catch (ProjectNotFoundException e){
            System.out.println(e.getMessage());
        }
        UIController.loopManager();
    }

    /**
     * Toggles the visibility status of a project (Visible - Not Visible).
     * Prompts for the project ID.
     * Delegates the toggle logic to {@link ManagerProjectController#toggleVisibility(String)}.
     * Catches and displays a message for {@link ProjectNotFoundException} if the project ID is invalid.
     * Loops back to the manager menu afterwards.
     */
    public static void toggleVisibility() {
        System.out.print("Project ID: ");
        String projectID = IOController.nextLine();
        try {
            ManagerProjectController.toggleVisibility(projectID);
        } catch(ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopManager();
    }

    /**
     * Displays the registration status of officers (details depend on controller implementation).
     * Delegates the display logic to {@link ManagerProjectController#viewOfficerRegistrationStatus()}.
     * Loops back to the manager menu afterwards.
     */
    public static void viewOfficerRegistrationStatus() {
        ManagerProjectController.viewOfficerRegistrationStatus();
        UIController.loopManager();
    }

    /**
     * Generates a report based on project or request data (specifics depend on controller implementation).
     * Delegates the report generation logic to {@link ManagerProjectController#generateReport()}.
     * Loops back to the manager menu afterwards.
     */
    public static void generateReport() {
        ManagerProjectController.generateReport();  
        UIController.loopManager();
    }
}
