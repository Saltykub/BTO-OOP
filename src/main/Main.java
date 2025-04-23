package main;

import boundary.LoginPage;
import utils.IDController;
/**
 * The main entry point class for the housing application system.
 * Initializes necessary controllers and starts the user interface flow.
 */
public class Main {
    /**
     * The main method that serves as the entry point for the application execution.
     * It first initializes the ID generation system by calling {@link utils.IDController#init()}
     * to ensure unique IDs can be generated based on existing data.
     * Then, it displays the initial welcome/login screen to the user by calling
     * {@link boundary.LoginPage#welcome()}.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        IDController.init();
        LoginPage.welcome();
    }
}
