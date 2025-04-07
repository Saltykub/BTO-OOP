package entity.project;

public class Manager extends User{
  private List<Project> project;
  
  public Manager() {
    //Implementation to be done
  }
  
  public List<Project> getProject() {
      return project;
  }

  public void setProject(List<Project> project) {
      this.project = project;
  }
}
