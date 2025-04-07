package entity.project;

public class Applicant extends User {
    private Project project;
    private Map<String, ApplicationStatus> applicationStatus;

    public Applicant(Project project) {
      this.project = project;
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

    public ApplicationStatus getApplicationStatusByID() {
        // Implementation to be done
        return applicationStatus;
    }

    public void setApplicationStatusByID(ApplicationStatus status) {
      // Implementation to be done
      this.applicationStatus = status
    }
}
