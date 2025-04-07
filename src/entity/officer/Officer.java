package entity.project

public class Officer extends Applicant {
  public enum RegistrationStatus {
    PENDING;
    SUCCESSFUL;
    UNSUCCECESSFUL;
  }
  
  private List<Project> officeProject;
  private Map<String, RegistrationStatus> registrationStatus;
  
  // Getters and setters
  public List<Project> getOfficeProject() {
    return officeProject;
  }
  
  public void setOfficeProject(List<Project> officeProject) {
    this.officeProject = officeProject;
  }
  
  public Map<String, RegistrationStatus> getRegistrationStatus() {
    return registrationStatus;
  }
  
  public void setRegistrationStatus(Map<String, RegistrationStatus> registrationStatus) {
    this.registrationStatus = registrationStatus;
  }
}
