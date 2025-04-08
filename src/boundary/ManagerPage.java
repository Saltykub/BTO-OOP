package boundary;

import java.util.*;
import java.time.*;

public class ManagerPage {
    Scanner scanner = new Scanner(System.in);
    String requestID;
    public ManagerPage() {
        // To be implemented
    }
    
    // Page management methods
    public void viewRegisteredProject() {
        OfficerRequestController.viewRegisteredProject();
    }
    public void viewEnquiries() {
        OfficerRequestController.viewEnquiries();
    }
    public void answerEnquiries() {
        ManagerRequestController.answerenquiries();
    }
    public void viewProjectList() {
        ManagerProjectController.viewProjectList();
    }
    public void viewApplicantApplicationStatus() {
        OfficerProjectController.viewApplicantApplicationStatus();
    }
    public void viewRequest() {
        ManagerRequestController.viewRequest();
    }
    public void changeRequestStatus() {
        System.out.print("Request ID: ");
        requestID = scanner.next();
        ManagerRequestController.changeRequestStatus(requestID);
    }
    public void changeApplicantApplication() {
        System.out.print("Request ID: ");
        requestID = scanner.next();
        ManagerRequestController.changeApplicantApplication(requestID);
    }
    public void viewAllEnquiries() {
        ManagerRequestController.viewAllEnquiries();
    }
    public void createProject() {
        projectID = IDController.newProjectID;
        System.out.print("Name: ");
        String name = scanner.next();
        System.out.print("Neighbourhood: ");
        String neighbourhood = scanner.next();
        requestID = IDController.newRequestID;
        System.out.print("Type of Flat (2/3): ");
        int flatType = scanner.nextInt();
        //Implement flat type mapping
        System.out.print("Number of units: ");
        int availableUnits = scanner.nextInt();
        System.out.print("Price: ");
        int price = scanner.nextInt();
        while (true) {
            System.out.print("Enter open date (YYYY-MM-DD): ");
            try {
                openDate date = LocalDate.parse(scanner.nextLine(), formatter);
                System.out.println("Valid date: " + date);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format! Please try again.");
            }
        }
        while (true) {
            System.out.print("Enter close date (YYYY-MM-DD): ");
            try {
                closeDate date = LocalDate.parse(scanner.nextLine(), formatter);
                System.out.println("Valid date: " + date);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format! Please try again.");
            }
        }
        System.out.print("Available Officer (1-10): ");
        int availableOfficer = scanner.nextInt();

        ManagerProjectController.createProject(projectID,name,neighbourhood,quantity,price,openDate,closeDate,officerID,managerID,availableOfficer);
    }
    public void editProject() {
        System.out.print("Request ID: ");
        requestID = scanner.next();
        ManagerProjectController.editProject(requestID,ProjectList.getByID(requestID))
    }
    public void deleteProject() {
        System.out.print("Request ID: ");
        requestID = scanner.next();
        ManagerProjectController.deleteProject(requestID);
    }
    public void toggleVisibility() {
        System.out.print("Request ID: ");
        requestID = scanner.next();
        ManagerProjectController.toggleVisibility(requestID);
    }
    public void viewOfficerRegistrationStatus() {
        ManagerProjectController.viewOfficerRegistrationStatus();
    }
    public void generateReport() {
        System.out.print("Request ID: ");
        requestID = scanner.next();
        ManagerProjectController.generateReport(requestID);
    }
}
