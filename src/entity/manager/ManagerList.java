package entity.project;

public class ManagerList extends ModelList {
  private String filePath;
  private List<Manager> managers;
  
  public ManagerList() {
    this.managers = new ArrayList<>();
  }

  public String getFilePath() {
        return filePath;
  }
}
