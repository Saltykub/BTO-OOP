package utils;

import boundary.ApplicantPage;
import boundary.ManagerPage;
import boundary.OfficerPage;

public class UIController {
    public static final String LINE_SEPARATOR = "===================================================================";
    
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

    public static void loopApplicant() {
        System.out.println("Press ENTER to return to main page.");
        IOController.nextLine();
        ApplicantPage.allOptions();
    }

    public static void loopOfficer() {
        System.out.println("Press ENTER to return to main page.");
        IOController.nextLine();
        OfficerPage.allOptions();
    }

    public static void loopManager() {
        System.out.println("Press ENTER to return to main page.");
        IOController.nextLine();
        ManagerPage.allOptions();
    }
    
    public static void exit() {
        UIController.clearPage();
        System.exit(0);
    }
}
