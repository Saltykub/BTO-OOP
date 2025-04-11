package controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class IOController {   
    private static final Scanner scanner = new Scanner(System.in);

    public static int nextInt() {
        try {
            int ret = scanner.nextInt();
            scanner.nextLine();
            return ret;
        } catch (Exception e) {
            System.out.print("Please enter a valid integer: ");
            scanner.nextLine();
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

    public static LocalDate nextDate(){
        Integer d, m, y;
        System.out.print("Date: ");
        d = nextInt();
        System.out.print("Month: ");
        m = nextInt();
        System.out.print("Year: ");
        y = nextInt();
        try {
            LocalDate.of(y, m, d); 
            return LocalDate.of(y, m, d);
        } catch (DateTimeException e) {
            System.out.println("Please enter a valid Date.");
            return nextDate();
        } 
    }
}
