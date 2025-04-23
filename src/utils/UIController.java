package utils;

import boundary.ApplicantPage;
import boundary.ManagerPage;
import boundary.OfficerPage;

/**
 * Utility class providing static methods for controlling common console-based
 * User Interface (UI) behaviors within the application.
 * Includes functionality for clearing the console screen, defining standard UI elements
 * like separators, looping back to main menus for different user roles, and exiting the application.
 */
public class UIController {
    /**
     * A constant string used as a visual separator in console output,
     * typically for separating different sections or headers.
     */
    public static final String LINE_SEPARATOR = "===================================================================";
    
    /**
     * Attempts to clear the console screen.
     * Uses different methods based on the detected operating system:
     * - Windows: Executes the 'cls' command via ProcessBuilder.
     * - Unix/Linux/macOS: Prints ANSI escape codes to clear the screen and move the cursor.
     * Catches and prints potential exceptions if screen clearing fails.
     */
    public static void clearPage() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                // Windows CMD
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Unix/Linux/macOS
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Error clearing screen: " + e.getMessage());
        }
    }

    /**
     * Pauses execution, prompts the user to press Enter, and then navigates
     * back to the main Applicant menu by calling {@link ApplicantPage#allOptions()}.
     * Typically called after an applicant action is completed.
     */
    public static void loopApplicant() {
        System.out.println("Press ENTER to return to main page.");
        IOController.nextLine();
        ApplicantPage.allOptions();
    }

    /**
     * Pauses execution, prompts the user to press Enter, and then navigates
     * back to the main Officer menu by calling {@link OfficerPage#allOptions()}.
     * Typically called after an officer action is completed.
     */
    public static void loopOfficer() {
        System.out.println("Press ENTER to return to main page.");
        IOController.nextLine();
        OfficerPage.allOptions();
    }

    /**
     * Pauses execution, prompts the user to press Enter, and then navigates
     * back to the main Manager menu by calling {@link ManagerPage#allOptions()}.
     * Typically called after a manager action is completed.
     */
    public static void loopManager() {
        System.out.println("Press ENTER to return to main page.");
        IOController.nextLine();
        ManagerPage.allOptions();
    }
    
    /**
     * Clears the console screen and terminates the Java Virtual Machine,
     * effectively exiting the application. Uses an exit code of 0, indicating
     * normal termination.
     */
    public static void exit() {
        UIController.clearPage();
        System.exit(0);
    }
}
