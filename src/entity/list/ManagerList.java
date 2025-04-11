package entity.list;

import entity.user.Manager;

public class ManagerList extends ModelList<Manager> {
  private static final String filePath = "data_csv/ManagerList.csv";
  
  public ManagerList(String filePath) {
    super(filePath, Manager.class);
  }

  public static ManagerList getInstance() {
    return new ManagerList(filePath);
  }

  public String getFilePath() {
    return filePath;
  }

  public Manager getByID(String ID) {
      for (Manager manager : this.getAll()) {
          if (manager.getUserID().equals(ID)) {
              return manager;
          }
      }
      return null;
  }
}
