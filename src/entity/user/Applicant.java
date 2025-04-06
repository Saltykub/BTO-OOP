package entity.user;

import java.util.HashMap;
import java.util.Map;
import entity.project.Project;

public class Applicant extends User {
    private Project project;
    private Map<String, ApplicationStatus> applicationStatus;

    public Applicant() {
        super();
        this.project = null;
        this.applicationStatus = new HashMap<>();
    }

    public Applicant(String userID, String name, String hashedPassword, int age, MaritalStatus maritalStatus) {
        super(userID, name, hashedPassword, age, maritalStatus);
        this.applicationStatus = new HashMap<>();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
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
}
