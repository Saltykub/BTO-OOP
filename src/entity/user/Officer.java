package entity.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Officer extends Applicant {
    private List<String> officerProject;
    private Map<String, RegistrationStatus> registrationStatus;

    public Officer() {
        super();
        this.officerProject = new ArrayList<>();
        this.registrationStatus = new HashMap<>();
    }
    
    public Officer(String userID, String name, String hashedPassword, int age, MaritalStatus maritalStatus) {
        super(userID, name, hashedPassword, age, maritalStatus);
        this.officerProject = new ArrayList<>();
        this.registrationStatus = new HashMap<>();
    }

    public List<String> getOfficerProject() {
        return officerProject;
    }

    public void setOfficerProject(List<String> officerProject) {
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
