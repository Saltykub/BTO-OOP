package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import boundary.LoginPage;
import entity.list.ApplicantList;
import entity.list.ManagerList;
import entity.list.OfficerList;
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
import utils.UIController;

public class AccountController {
    private static String userID;

    public static String getUserID() {
        return userID;
    }

    /**
     * Sets the user ID for the currently authenticated user.
     * Typically called upon successful login and set to null upon logout.
     *
     * @param ID The user ID to set.
     */
    public static void setUserID(String ID) {
        userID = ID;
    }

    /**
     * Registers a new user in the system.
     * Checks the user ID format and if the ID is already taken.
     * Hashes the password before storing.
     * Adds the user to the appropriate lists based on their UserType:
     * - All users are added to ApplicantList.
     * - Officers and Managers are also added to OfficerList.
     * - Managers are additionally added to ManagerList.
     *
     * @param userType      The type of user to register (APPLICANT, OFFICER, MANAGER).
     * @param userID        The user ID for the new account. Must match specific format.
     * @param password      The plain text password for the new account.
     * @param name          The name of the user.
     * @param age           The age of the user.
     * @param maritalStatus The marital status of the user.
     * @throws InvalidUserFormatException If the userID does not match the required format (e.g., "^[ST]\\d{7}[A-Z]$").
     * @throws AlreadyRegisteredException If a user with the given userID already exists.
     */
    public static void register(UserType userType, String userID, String password, String name, int age, MaritalStatus maritalStatus) throws InvalidUserFormatException, AlreadyRegisteredException {
        checkUserID(userID);
        try {
            findUser(userID);
            throw new AlreadyRegisteredException();
        }
        catch (UserNotFoundException e) {
            ApplicantList.getInstance().add(new Applicant(userID, name, hashPassword(password), age, maritalStatus));
            if (userType != UserType.APPLICANT) OfficerList.getInstance().add(new Officer(userID, name, hashPassword(password), age, maritalStatus));
            if (userType == UserType.MANAGER) ManagerList.getInstance().add(new Manager(userID, name, hashPassword(password), age, maritalStatus));
            System.out.println("Registration completed.");
        }
    }

    /**
     * Finds a user by their user ID across all user type lists.
     * Searches in the order: ManagerList, OfficerList, ApplicantList.
     * Returns the first user found with the matching ID.
     *
     * @param userID The user ID to search for.
     * @return The User object (Manager, Officer, or Applicant) if found.
     * @throws UserNotFoundException If no user with the given ID is found in any list.
     */
    public static User findUser(String userID) throws UserNotFoundException {
        for (Manager m : ManagerList.getInstance().getAll()) {
            if (m.getUserID().equals(userID)) return m;
        }
        for (Officer o : OfficerList.getInstance().getAll()) {
            if (o.getUserID().equals(userID)) return o;
        }
        for (Applicant a : ApplicantList.getInstance().getAll()) {
            if (a.getUserID().equals(userID)) return a;
        }
        throw new UserNotFoundException();
    }

    /**
     * Checks if the provided plain text password matches the stored hashed password for the given user ID.
     *
     * @param userID   The user ID whose password needs checking.
     * @param password The plain text password to check.
     * @return true if the password matches.
     * @throws UserNotFoundException      If the user ID does not exist.
     * @throws PasswordIncorrectException If the provided password does not match the stored hash.
     */
    public static boolean checkPassword(String userID, String password) throws UserNotFoundException, PasswordIncorrectException {
        User user = findUser(userID);
        if (hashPassword(password).equals(user.getHashedPassword())) return true;
        else throw new PasswordIncorrectException();
    }

    /**
     * Attempts to log in a user with the provided credentials.
     * Finds the user by ID and checks if the provided password is correct.
     * If successful, sets the currently logged-in user ID and returns the User object.
     *
     * @param userID   The user ID attempting to log in.
     * @param password The plain text password provided for login.
     * @return The User object (Manager, Officer, or Applicant) upon successful login.
     * @throws UserNotFoundException      If the user ID does not exist.
     * @throws PasswordIncorrectException If the provided password does not match.
     */
    public static User login(String userID, String password) throws UserNotFoundException, PasswordIncorrectException {
        User user = findUser(userID);
        if (checkPassword(userID, password)) {
            setUserID(userID);
            return user;
        }
        else throw new PasswordIncorrectException();
    }

    /**
     * Logs out the currently authenticated user.
     * Clears the stored user ID and navigates back to the login page.
     */
    public static void logout() {
        setUserID(null);
        UIController.clearPage();
        LoginPage.welcome();
    }

    /**
     * Hashes a plain text password using SHA3-256 algorithm and encodes the result in Base64.
     * This is a one-way process used for securely storing passwords.
     *
     * @param password The plain text password to hash.
     * @return A Base64 encoded string representing the hashed password.
     * @throws RuntimeException if the SHA3-256 algorithm is not available in the Java environment.
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            return Base64.getEncoder().encodeToString(digest.digest(password.getBytes()));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA3-256 algorithm not found.", e);
        }
    }

    /**
     * Checks if the provided user ID conforms to the required format.
     * The expected format is defined by the regular expression "^[ST]\\d{7}[A-Z]$".
     * (Starts with S or T, followed by 7 digits, ending with an uppercase letter).
     *
     * @param userID The user ID string to validate.
     * @return true if the userID matches the format.
     * @throws InvalidUserFormatException If the userID does not match the required format.
     */
    private static boolean checkUserID(String userID) throws InvalidUserFormatException {
        if (userID.matches("^[ST]\\d{7}[A-Z]$")) return true;
        throw new InvalidUserFormatException();
    }

    /**
     * Changes the password for a given user ID after verifying the old password.
     * Finds the user, checks the old password, hashes the new password,
     * and updates the user object in all relevant user lists (Applicant, Officer, Manager).
     * Note: Assumes user objects in different lists (e.g., ApplicantList, OfficerList)
     * might need separate updates even if they represent the same underlying user.
     *
     * @param userID      The user ID whose password needs changing.
     * @param oldPassword The current plain text password for verification.
     * @param newPassword The new plain text password to set.
     * @throws UserNotFoundException      If the user ID does not exist.
     * @throws PasswordIncorrectException If the provided oldPassword does not match.
     */
    public static void changePassword(String userID, String oldPassword, String newPassword) throws UserNotFoundException, PasswordIncorrectException {
        User user = findUser(userID);
        if (checkPassword(userID, oldPassword)) {
            user.setHashedPassoword(hashPassword(newPassword));
            ApplicantList.getInstance().update(userID, (Applicant)user);
            if (user instanceof Officer o) OfficerList.getInstance().update(userID, o);
            else if (user instanceof Manager m) {
                OfficerList.getInstance().update(userID, (Officer)user);
                ManagerList.getInstance().update(userID, m);
            }
        }
        else throw new PasswordIncorrectException();
    }
}
