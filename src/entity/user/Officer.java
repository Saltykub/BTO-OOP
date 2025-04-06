package entity.user;

import entity.project.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Officer extends Applicant {
    private List<Project> officerProject;
    private Map<String, RegistrationStatus> registrationStatus;

    // No-argument constructor
    public Officer() {
        super(); // calls Applicant’s no-arg constructor
        this.officerProject = new ArrayList<>();
        this.registrationStatus = new HashMap<>();
    }

    public List<Project> getOfficerProject() {
        return officerProject;
    }

    public void setOfficerProject(List<Project> officerProject) {
        this.officerProject = officerProject;
    }

    public Map<String, RegistrationStatus> getRegistrationStatus() {
        return registrationStatus;
    }

    public RegistrationStatus getRegistrationStatusByID(String id) {
        return registrationStatus.get(id);
    }

    public void setRegistrationStatusByID(String id, RegistrationStatus status) {
        registrationStatus.put(id, status);
    }
}
