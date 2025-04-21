package entity.user;

import java.util.HashMap;
import java.util.Map;

import entity.project.FlatType;

public class Applicant implements User {
    private String applicantID;
    private String name;
    private String hashedPassword;
    private int age;
    private MaritalStatus maritalStatus;
    private String project;
    private Map<String, ApplicationStatus> applicationStatus;
    private Map<String, FlatType> appliedFlat;

    public Applicant() {
        this.applicantID = "";
        this.name = "";
        this.hashedPassword = "";
        this.age = 0;
        this.maritalStatus = MaritalStatus.SINGLE;
        this.applicationStatus = new HashMap<>();
        this.appliedFlat = new HashMap<>();
    }

    public Applicant(String userID, String name, String hashedPassword, int age, MaritalStatus maritalStatus) {
        this.applicantID = userID;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.applicationStatus = new HashMap<>();
        this.appliedFlat = new HashMap<>();
    }

    public String getUserID() {
        return applicantID;
    }

    public void setUserID(String userID) {
        this.applicantID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassoword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Map<String, ApplicationStatus> getApplicationStatus() {
        return applicationStatus;
    }

    public ApplicationStatus getApplicationStatusByID(String projectID) {
        return applicationStatus.get(projectID);
    }

    public void setApplicationStatusByID(String projectID, ApplicationStatus status) {
        applicationStatus.put(projectID, status);
    }

    public Map<String, FlatType> getAppliedFlat() {
        return appliedFlat;
    }

    public FlatType getAppliedFlatByID(String projectID) {
        return appliedFlat.get(projectID);
    }

    public void setAppliedFlatByID(String projectID, FlatType flat) {
        appliedFlat.put(projectID, flat);
    }
}
