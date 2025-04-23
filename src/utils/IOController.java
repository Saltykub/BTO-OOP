package utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Utility class providing static methods for handling console input operations.
 * Encapsulates reading integers, lines of text, passwords (securely if possible),
 * and dates from standard input (System.in). Includes basic validation and
 * re-prompting mechanisms for invalid input.
 */
public class IOController {   
    /**
     * Shared Scanner instance used for reading from System.in across all methods in this class.
     * Initialized once statically.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads the next integer value from the console input.
     * If the user enters non-integer input, it catches the exception, prints an error message
     * prompting for a valid integer, consumes the invalid input line, and recursively calls itself
     * until a valid integer is entered. Also consumes the trailing newline character after
     * successfully reading an integer.
     *
     * @return The valid integer entered by the user.
     */

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

    /**
     * Reads the next complete line of text (up to the newline character) from the console input.
     *
     * @return The line of text entered by the user.
     */
    public static String nextLine() {
        return scanner.nextLine();
    } 

    /**
     * Reads a password securely from the console, if available.
     * Attempts to use {@link System#console()} to read the password without echoing characters.
     * If the console is not available (e.g., running in some IDEs), it falls back to
     * reading a normal line of text using {@link #nextLine()}.
     *
     * @return The password entered by the user as a String. Be aware that the fallback method is not secure.
     */
    public static String readPassword() {
        String password;
        if (System.console() == null) {
            password = nextLine();
        } else {
            password = new String(System.console().readPassword());
        }
        return password;
    }

    /**
     * Prompts the user to enter a date (day, month, year separately as integers)
     * and constructs a {@link LocalDate} object.
     * If the entered combination results in an invalid date (e.g., Feb 30th),
     * it catches the {@link DateTimeException}, prints an error message, and recursively
     * calls itself until a valid date is entered.
     *
     * @return The valid {@link LocalDate} entered by the user.
     */
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
