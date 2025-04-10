package entity.list;

import entity.user.Manager;

public class ManagerList extends ModelList<Manager> {
  private static String filePath;
  
  public ManagerList(String filePath) {
    super(filePath);
  }

  public static ManagerList getInstance() {
    return new ManagerList(filePath);
  }

  public String getFilePath() {
    return filePath;
  }
}
