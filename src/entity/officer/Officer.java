package entity.project;

public class Officer extends Applicant {
  public enum RegistrationStatus {
    PENDING;
    SUCCESSFUL;
    UNSUCCECESSFUL;
  }
  
  private List<Project> officerProject;
  private Map<String, RegistrationStatus> registrationStatus;

  Officer() {
    //Implementation to be done
  }
  
  public List<Project> getOfficerProject() {
    return officerProject;
  }
  
  public void setOfficerProject(List<Project> officeProjects) {
    this.officerProject = officeProject;
  }
  
  public Map<String, RegistrationStatus> getRegistrationStatus() {
    return registrationStatus;
  }

  public Map<String, RegistrationStatus> getRegistrationStatusbyID() {
    //Implementation to be done
    return registrationStatus;
  }
  
  public void setRegistrationStatusbyID(Map<String, RegistrationStatus> registrationStatus) {
    //Implementation to be done
    this.registrationStatus = registrationStatus;
  }
}
