package entity.list;

import entity.user.Manager;

public class ManagerList extends ModelList<Manager> {
  private static final String FILE_PATH = "data_csv/ManagerList.csv";
  
  public ManagerList(String FILE_PATH) {
    super(FILE_PATH, Manager.class);
  }

  public static ManagerList getInstance() {
    return new ManagerList(FILE_PATH);
  }

  public String getFilePath() {
    return FILE_PATH;
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
