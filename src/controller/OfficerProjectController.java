package controller;

import java.util.List;

import entity.list.ApplicantList;
import entity.list.OfficerList;
import entity.list.ProjectList;
import entity.project.FlatType;
import entity.project.Project;
import entity.user.Applicant;
import entity.user.ApplicationStatus;
import entity.user.Manager;
import entity.user.Officer;
import entity.user.User;
import entity.user.UserType;
import utils.Display;

/**
 * Controller responsible for handling project-related operations performed by an Officer.
 * This includes viewing projects available for registration, viewing applicant statuses within assigned projects,
 * booking flats for successful applicants, and generating receipts/reports related to booked flats.
 * It operates using the context of the currently logged-in officer's ID.
 */
public class OfficerProjectController {
    /**
     * Stores the user ID of the officer currently interacting with the system.
     * This ID is typically set by the {@link AccountController} upon successful login of an Officer.
     */
    private static String officerID;

    /**
     * Sets the officer ID for the current session context.
     * Subsequent officer-specific operations in this controller will use this ID.
     *
     * @param ID The user ID of the currently logged-in officer.
     */
    public static void setOfficerID(String ID) {
        officerID = ID;
    }

    /**
     * Displays a list of projects that the current officer may be eligible to register for.
     * Filters the global project list using {@link FilterController}.
     * Further filters based on:
     * - Project visibility.
     * - Officer is not already an applicant for the project.
     * - The project's dates do not overlap with any project the officer is already registered for.
     */
    public static void viewRegistrableProject() {
        List<Project> list = ProjectList.getInstance().getAll();
        list = FilterController.filteredList(list);
        List<String> officerProject = OfficerList.getInstance().getByID(officerID).getOfficerProject();
        boolean has = false;
        for (Project project : list) {
            if (!project.getApplicantID().contains(officerID) && project.getVisibility()) {
                boolean can = true;
                for (String id : officerProject) {
                    Project p = ProjectList.getInstance().getByID(id);
                    if (p.getCloseDate().isBefore(project.getOpenDate()) || project.getCloseDate().isBefore(p.getOpenDate())) continue;
                    can = false;
                }
                if (can) {
                    has = true;
                    Display.displayProject(project,UserType.OFFICER,null);
                }
            }
        }
        if (!has) System.out.println("There is no registrable project.");
    }

    /**
     * Displays the application status for all applicants across all projects the current officer is registered for.
     * Checks if the officer is registered for any projects before proceeding.
     */
    public static void viewApplicantApplicationStatus() {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> list = officer.getOfficerProject();
        if (!checkValidProject(list)) return;
        if (list.isEmpty()) {
            System.out.println("You haven't registered to any project.");
            return;
        }
        for (String id : list) {
            Project project = ProjectList.getInstance().getByID(id);
            Display.displayProject(project, UserType.OFFICER,null);
            for (Applicant applicant : ApplicantList.getInstance().getAll()) {
                if (applicant.getProject() != null && applicant.getProject().equals(id)) {
                    System.out.println("Status: " + applicant.getApplicationStatusByID(id).coloredString());
                    Display.displayApplicant(applicant, false);
                }
            }
        }
    }

    /**
     * Displays the application status for all applicants associated with a specific project ID.
     * Note: This method does not explicitly check if the current officer is assigned to this project.
     *
     * @param projectID The ID of the project for which to view applicant statuses.
     */
    public static void viewApplicantApplicationStatus(String projectID) {
        Project project = ProjectList.getInstance().getByID(projectID);
        Display.displayProject(project, UserType.OFFICER,null);
        for (Applicant applicant : ApplicantList.getInstance().getAll()) {
            if (applicant.getProject() != null && applicant.getProject().equals(projectID)) {
                System.out.println("Status: " + applicant.getApplicationStatusByID(projectID).coloredString());
                Display.displayApplicant(applicant, false);
            }
        } 
    }

    /**
     * Displays applicants with a specific {@link ApplicationStatus} across all projects
     * the current officer is registered for.
     *
     * @param status The specific {@link ApplicationStatus} to filter by.
     */
    public static void viewApplicantApplicationStatus(ApplicationStatus status) {
        Officer officer = OfficerList.getInstance().getByID(officerID);
        List<String> list = officer.getOfficerProject();
        if(!checkValidProject(list)) return;
        for (String id : list) {
            Project project = ProjectList.getInstance().getByID(id);
            Display.displayProject(project, UserType.OFFICER,null);
            for (Applicant applicant : ApplicantList.getInstance().getAll()) {
                if (applicant.getProject() != null && applicant.getProject().equals(id) && applicant.getApplicationStatusByID(id) == status) {
                    System.out.println("Status: " + applicant.getApplicationStatusByID(id).coloredString());
                    Display.displayApplicant(applicant, false);
                }
            }
        }
    }

    /**
     * Displays applicants with a specific {@link ApplicationStatus} for a specific project ID.
     * Note: This method does not explicitly check if the current officer is assigned to this project.
     *
     * @param projectID The ID of the project to filter by.
     * @param status    The specific {@link ApplicationStatus} to filter by.
     */
    public static void viewApplicantApplicationStatus(String projectID, ApplicationStatus status) {
        Project project = ProjectList.getInstance().getByID(projectID);
        Display.displayProject(project, UserType.OFFICER,null);
        for (Applicant applicant : ApplicantList.getInstance().getAll()) {
            if (applicant.getProject() == projectID && applicant.getApplicationStatusByID(projectID) == status) {
                Display.displayApplicant(applicant, false);
                System.out.println("Status: " + applicant.getApplicationStatusByID(projectID).coloredString());
            }
        } 
    }

    /**
     * Books a flat for a specified applicant.
     * Performs checks:
     * - Applicant ID is valid and corresponds to an Applicant (not Manager/Officer).
     * - Applicant has applied to a project.
     * - The current officer is registered for the applicant's project.
     * - Applicant's status is SUCCESSFUL (not already BOOKED or PENDING/UNSUCCESSFUL).
     * - The flat type applied for has available units.
     * If checks pass, updates the project's available units, adds the applicant to the project's booked list,
     * and updates the applicant's status to BOOKED.
     *
     * @param applicantID The ID of the applicant for whom to book the flat.
     */
    public static void bookFlat(String applicantID) {
        // no need to pass project because applicant can has only 1 project 
        User user = ApplicantList.getInstance().getByID(applicantID);
        if ((user instanceof Manager) || user == null) {
            System.out.println("Invalid applicant ID.");
            return;
        }
        Applicant applicant = (Applicant)user;
        String projectID = applicant.getProject();
        Officer officer = OfficerList.getInstance().getByID(officerID);
        if (projectID == null || !officer.getOfficerProject().contains(projectID)) {
            System.out.println("You are not allowed to book flat for applicant in other's project.");
            return;
        }
        if (applicant.getApplicationStatusByID(projectID) == ApplicationStatus.BOOKED) {
            System.out.println("Flat has been booked for this applicant already.");
            return; 
        }
        else if (applicant.getApplicationStatusByID(projectID) != ApplicationStatus.SUCCESSFUL) {
            System.out.println("This applicant hasn't been approved yet.");
            return; 
        }
        Project project = ProjectList.getInstance().getByID(projectID);
        FlatType flat = applicant.getAppliedFlatByID(projectID);
        int availableUnit = project.getAvailableUnit().get(flat);
        if (availableUnit > 0) {
            project.setAvailableUnit(flat, availableUnit - 1);
            project.addApplicantID(applicantID);
            applicant.setApplicationStatusByID(projectID, ApplicationStatus.BOOKED);
        }
        ProjectList.getInstance().update(projectID, project);
        ApplicantList.getInstance().update(applicantID, applicant);
        System.out.println("Successfully booked a flat for this applicant.");
    }

    /**
     * Generates a 'receipt' by displaying details of all applicants who have successfully BOOKED a flat
     * across all projects the current officer is registered for.
     * Also displays the project details for context.
     */
    public static void generateReceipt() {
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectId = o.getOfficerProject();
        if(!checkValidProject(projectId)) return;
        for(String id: projectId){
            Project p = ProjectList.getInstance().getByID(id);
            List<String> applicantID = p.getApplicantID();
            for(String ida:applicantID){
                Applicant applicant = ApplicantList.getInstance().getByID(ida);
                if(applicant.getApplicationStatus().get(id) != ApplicationStatus.BOOKED) continue;
                Display.displayApplicant(applicant, false);
            }
            Display.displayProject(p,UserType.OFFICER,null);
        }
    }

    /**
     * Generates a 'receipt' for a specific applicant ID.
     * Displays details if the applicant has BOOKED status and is associated with a project
     * the current officer is registered for.
     *
     * @param applicantID The ID of the applicant for whom to generate the receipt.
     */
    public static void generateReceiptByApplicant(String applicantID) {
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectId = o.getOfficerProject();
        if(!checkValidProject(projectId)) return;
        for(String id: projectId){
            Project p = ProjectList.getInstance().getByID(id);
            List<String> aID = p.getApplicantID();
            for(String ida:aID){
                if(ida.equals(applicantID)){
                    Applicant applicant = ApplicantList.getInstance().getByID(ida);
                    if(applicant.getApplicationStatus().get(id) != ApplicationStatus.BOOKED) continue;
                    Display.displayProject(p,UserType.OFFICER,null);
                    Display.displayApplicant(applicant, false);
                    return;
                }
            }
        }
        System.out.println("Applicant not found in your registered project");
    }

    /**
     * Generates a 'receipt' by displaying details of all applicants who have successfully BOOKED a flat
     * for a specific project ID, provided the current officer is registered for that project.
     *
     * @param projectID The ID of the project for which to generate receipts.
     */
    public static void generateReceiptByProject(String projectID) {
        Officer o = OfficerList.getInstance().getByID(officerID);
        List<String> projectId = o.getOfficerProject();
        if(!checkValidProject(projectId)) return;
        for(String id: projectId){
            if(id.equals(projectID)){
                Project p = ProjectList.getInstance().getByID(id);
                List<String> aID = p.getApplicantID();
                Display.displayProject(p,UserType.OFFICER,null);
                for(String ida:aID){
                    Applicant applicant = ApplicantList.getInstance().getByID(ida);
                    if(applicant.getApplicationStatus().get(id) != ApplicationStatus.BOOKED) continue;
                    Display.displayApplicant(applicant, false);
                }
                return;
            }
           
        }
        System.out.println("Project not found in your registered project");
    }
    /**
     * Helper method to check if the officer is associated with any projects.
     * Prints a message to the console if the officer is not registered for any projects.
     * Suggestion: Could be made private if only used within this class.
     *
     * @param projectId The list of project IDs the officer is registered for.
     * @return true if the list is not null and not empty, false otherwise.
     */
    public static boolean checkValidProject(List<String> projectId){
        if(projectId.isEmpty()) {
            System.out.println("You don't have registered project");
            return false;
        }
        return true;
    }
}