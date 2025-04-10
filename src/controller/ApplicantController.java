package controller;

import entity.list.ApplicantList;
import entity.list.ProjectList;

public class ApplicantController {
    private static String applicantID;

    public static void setApplicantID(String id) {
        applicantID = id;
    }

    public static void viewProjectList() {
        ProjectList.getAll();
    }

    public static void applyProject(String projectID) {

    }

    public static void withdrawApplication() {

    }
    
    public static void query() {

    }

    public static void viewQuery() {

    }

    public static void editQuery(String requestID) {

    }

    public static void deleteQuery(String requestID) {

    }
}
