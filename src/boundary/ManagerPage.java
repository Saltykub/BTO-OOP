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

public class ManagerPage {
    String requestID;

    // Page management methods
    public static void allOptions() {
        UIController.clearPage();
        System.out.println(UIController.lineSeparator);
        System.out.println(
                        " _  _   __   __ _   __    ___  ____  ____    ____   __    ___  ____ \n" + //
                        "( \\/ ) / _\\ (  ( \\ / _\\  / __)(  __)(  _ \\  (  _ \\ / _\\  / __)(  __)\n" + //
                        "/ \\/ \\/    \\/    //    \\( (_ \\ ) _)  )   /   ) __//    \\( (_ \\ ) _) \n" + //
                        "\\_)(_/\\_/\\_/\\_)__)\\_/\\_/ \\___/(____)(__\\_)  (__)  \\_/\\_/ \\___/(____)\n" + //
                        "");
        System.out.println(UIController.lineSeparator);
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
        System.out.print("Enter the manager ID to view thier created projects (Press ENTER to view yours): ");
        String managerID = IOController.nextLine();
        if (managerID.isEmpty()) managerID = AccountController.getUserID();
        try{
            ManagerProjectController.viewProjectList(managerID);
        }  catch (ProjectNotFoundException e) {
            System.out.println(e.getMessage());
        }
        UIController.loopManager();
    }

    public static void viewRequest() {
        ManagerRequestController.viewRequest();
        UIController.loopManager();
    }

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
        try {
            ManagerProjectController.deleteProject(projectID);
        } catch (ProjectNotFoundException e){
            System.out.println(e.getMessage());
        }
        UIController.loopManager();
    }

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

    public static void viewOfficerRegistrationStatus() {
        ManagerProjectController.viewOfficerRegistrationStatus();
        UIController.loopManager();
    }

    public static void generateReport() {
        ManagerProjectController.generateReport();  
        UIController.loopManager();
    }
}
