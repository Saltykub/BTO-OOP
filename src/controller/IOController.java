package controller;

import java.util.Scanner;

public class IOController {   
    private static final Scanner scanner = new Scanner(System.in);

    public static int nextInt() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Please enter a valid integer.");
            return nextInt();
        }
    }

    public static String nextLine() {
        return scanner.nextLine();
    } 

    public static String readPassword() {
        String password;
        if (System.console() == null) {
            password = nextLine();
        } else {
            password = new String(System.console().readPassword());
        }
        return password;
    }
}
