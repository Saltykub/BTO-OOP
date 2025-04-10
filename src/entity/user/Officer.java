package entity.user;

import entity.project.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Officer extends Applicant {
    private List<Project> officerProject;
    private Map<String, RegistrationStatus> registrationStatus;

    public Officer() {
        super();
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

    public RegistrationStatus getRegistrationStatusByID(String ID) {
        return registrationStatus.get(ID);
    }

    public void setRegistrationStatusByID(String ID, RegistrationStatus status) {
        registrationStatus.put(ID, status);
    }
}
