import boundary.LoginPage;
import controller.IDController;

public class Main {
    public static void main(String[] args) {
        IDController.init();
        LoginPage.welcome();
    }
}
