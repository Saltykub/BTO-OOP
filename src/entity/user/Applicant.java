package entity.user;

import java.util.HashMap;
import java.util.Map;

import entity.project.FlatType;

public class Applicant extends User {
    private String project;
    private Map<String, ApplicationStatus> applicationStatus;
    private Map<String, FlatType> appliedFlat;

    public Applicant() {
        super();
        this.applicationStatus = new HashMap<>();
        this.appliedFlat = new HashMap<>();
    }

    public Applicant(String userID, String name, String hashedPassword, int age, MaritalStatus maritalStatus) {
        super(userID, name, hashedPassword, age, maritalStatus);
        this.applicationStatus = new HashMap<>();
        this.appliedFlat = new HashMap<>();
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
