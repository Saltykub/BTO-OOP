package entity.list;

import entity.user.Manager;

public class ManagerList extends ModelList<Manager> {
  private String filePath;
  
  public ManagerList(String filePath) {
    super(filePath);
  }

  public String getFilePath() {
        return filePath;
  }
}
