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
    public void allOptions() {
        System.out.println("Options:"
                           +"\n1. View Registered Projects"
                           +"\n2. View Enquiries"
                           +"\n3. Answer Enquiries"
                           +"\n4. View Project List"
                           +"\n5. View Applicant Application Status"
                           +"\n6. View Requests"
                           +"\n7. Change Request Status"
                           +"\n8. Change Applicant Application"
                           +"\n9. View All Enquiries"
                           +"\n10. Create Project"
                           +"\n11. Edit Project"
                           +"\n12. Delete Project"
                           +"\n13. Toggle Visibility"
                           +"\n14. View Officer Registration Status"
                           +"\n15. Generate Report");
        System.out.print("Option selection: ");
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                viewEnquiries();
                break;
            case 2:
                answerEnquiries();
                break;
            case 3:
                viewProjectList();
                break;
            case 4:
                viewApplicantApplicationStatus();
                break;
            case 5:
                viewRequest();
                break;
            case 6:
                changeRequestStatus();
            case 7:
                changeApplicantApplication();
                break;
            case 8:
                viewAllEnquiries();
                break;
            case 9:
                createProject();
                break;
            case 10:
                editProject();
                break;
            case 11:
                deleteProject();
                break;
            case 12:
                toggleVisibility();
                break;
            case 13:
                viewOfficerRegistrationStatus();
                break;
            case 14:
                viewProjectList();
                break;
            case 15:
                generateReport();
                break;
        }
        option = null;
    }
                            
            
            
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
    scanner.close()
}
