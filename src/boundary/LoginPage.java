package boundary;

import controller.AccountController;
import controller.ApplicantController;
import controller.FilterController;
import controller.ManagerProjectController;
import controller.ManagerRequestController;
import controller.OfficerProjectController;
import controller.OfficerRequestController;
import entity.user.Applicant;
import entity.user.Manager;
import entity.user.MaritalStatus;
import entity.user.Officer;
import entity.user.User;
import entity.user.UserType;
import exception.AlreadyRegisteredException;
import exception.InvalidUserFormatException;
import exception.PasswordIncorrectException;
import exception.UserNotFoundException;
import utils.IOController;
import utils.UIController;

public class LoginPage {
    public static void welcome() {
        UIController.clearPage();
        System.out.println("Please enter your choice to continue.");
        System.out.println("\t1. Login");
        System.out.println("\t2. Register");
        System.out.println("\t3. Change Password");
        System.out.println("\t4. Exit");
        System.out.print("Your choice (1-4): ");
        int choice = -1;
        while (choice < 1 || choice > 5) {
            choice = IOController.nextInt();
            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> changePassword();
                case 4 -> UIController.exit();
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void login() {
        UIController.clearPage();
        System.out.print("Enter ID: ");
        String userID = IOController.nextLine();
        System.out.print("Enter password: ");
        String password = IOController.readPassword();
        try {
            FilterController.init();
            User user = AccountController.login(userID, password);
            ApplicantController.setApplicantID(userID);
            if (user instanceof Manager) {
                OfficerProjectController.setOfficerID(userID);
                OfficerRequestController.setOfficerID(userID);
                ManagerProjectController.setManagerID(userID);
                ManagerRequestController.setManagerID(userID);
                ManagerPage.allOptions();
            }
            else if (user instanceof Officer) {
                OfficerProjectController.setOfficerID(userID);
                OfficerRequestController.setOfficerID(userID);
                OfficerPage.allOptions();
            }
            else if (user instanceof Applicant) ApplicantPage.allOptions();
            return;
        } catch (UserNotFoundException | PasswordIncorrectException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Press ENTER to try again, or any other key to go back.");
        String choice = IOController.nextLine();
        if (choice.isEmpty()) {
            login();
        } else {
            welcome();
        }
    }

    public static void register() {
        UIController.clearPage();
        System.out.println("Enter User Type: ");
        System.out.println("\t1. Applicant");
        System.out.println("\t2. Officer");
        System.out.println("\t3. Manager");
        System.out.print("Your choice (1-3): ");
        UserType userType = null;
        while (userType == null) {
            int type = IOController.nextInt();
            switch (type) {
                case 1 -> userType = UserType.APPLICANT;
                case 2 -> userType = UserType.OFFICER;
                case 3 -> userType = UserType.MANAGER;
                default -> System.out.println("Invalid choice. Please try again."); 
            }
        }
        System.out.print("Enter ID: ");
        String userID = IOController.nextLine();
        System.out.print("Enter password: ");
        String password = IOController.readPassword();
        System.out.print("Enter name: ");
        String name = IOController.nextLine();
        System.out.print("Enter age: ");
        int age = IOController.nextInt();
        System.out.println("Enter marital status:");
        System.out.println("\t1. Single");
        System.out.println("\t2. Married");
        System.out.print("Your choice (1-2): ");
        MaritalStatus maritalStatus = null;
        while (maritalStatus == null) {
            int marital = IOController.nextInt();
            switch (marital) {
                case 1 -> maritalStatus = MaritalStatus.SINGLE;
                case 2 -> maritalStatus = MaritalStatus.MARRIED;
                default -> System.out.println("Invalid choice. Please try again."); 
            }
        }
        try {
            AccountController.register(userType, userID, password, name, age, maritalStatus);
            System.out.println("Press ENTER to go back.");
            IOController.nextLine();
            welcome();
        } catch (InvalidUserFormatException | AlreadyRegisteredException e) {
            System.out.println(e.getMessage());
            System.out.println("Press ENTER to try again, or any other key to go back.");
            String choice = IOController.nextLine();
            if (choice.isEmpty()) {
                register();
            } else {
                welcome();
            }
        }
    }
    
    public static void changePassword() {
        UIController.clearPage();
        System.out.print("Enter ID: ");
        String userID = IOController.nextLine();
        System.out.print("Enter password: ");
        String password = IOController.readPassword();
        try {
            boolean correctPassword = AccountController.checkPassword(userID, password);
            if (correctPassword) {
                System.out.print("Enter new password: ");
                String newPassword = IOController.readPassword();
                AccountController.changePassword(userID, password, newPassword);
                System.out.println("Successfully change password!");
                System.out.println("Press any key to go back.");
                IOController.nextLine(); 
                welcome();
            }
            
        } catch (UserNotFoundException | PasswordIncorrectException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Press ENTER to try again, or any other key to go back.");
        String choice = IOController.nextLine();
        if (choice.isEmpty()) {
            changePassword();
        } else {
            welcome();
        }
    }
    
}
